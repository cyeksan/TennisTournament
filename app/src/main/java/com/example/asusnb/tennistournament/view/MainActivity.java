package com.example.asusnb.tennistournament.view;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.asusnb.tennistournament.R;
import com.example.asusnb.tennistournament.model.ListModel;
import com.example.asusnb.tennistournament.model.Player;
import com.example.asusnb.tennistournament.model.InputModel;
import com.example.asusnb.tennistournament.model.Skills;
import com.example.asusnb.tennistournament.model.Tournament;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //region Members
    private StringBuilder total;

    private List<Player> playerList = new ArrayList<>();
    private List<String> surfaceList = new ArrayList<>();
    private Integer gainedExperienceOfPlayerOfTeam1 = null;
    private Integer gainedExperienceOfPlayerOfTeam2 = null;

    private Player playerOfEliminationTeam1;
    private Player playerOfEliminationTeam2;
    private List<ListModel> listOfMatchOfPlayersInLeague = new ArrayList<>();

    private Integer totalExperienceOfPlayerOfTeam1 = null;
    private Integer totalExperienceOfPlayerOfTeam2 = null;

    private List<Player> eliminationTeam1 = new ArrayList<>();
    private List<Player> eliminationTeam2 = new ArrayList<>();
    private List<Player> winnerList = new ArrayList<>();
    private List<Tournament> leagueList = new ArrayList<>();
    private List<Tournament> eliminationList = new ArrayList<>();
    private List<String> tournamentTypeList = new ArrayList<>();

    private InputModel mInputModel;

    private Player winner;
    private Player loser;

    //endregion

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //region Read from input.json:

        try (InputStream is = getResources().openRawResource(R.raw.input)) {

            BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

            total = new StringBuilder();
            for (String line; (line = br.readLine()) != null; ) {
                total.append(line);
            }

        } catch (Exception e) {

            e.printStackTrace();

        }
        //endregion

        //region Parse by using Jackson library:

        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory jsonFactory = new JsonFactory();

        try {
            JsonParser jp = jsonFactory.createJsonParser(total.toString());
            mInputModel = objectMapper.readValue(jp, InputModel.class);

        } catch (IOException e) {
            e.printStackTrace();
        }
        //endregion

            //region Create player model:

            for (int i = 0; i < mInputModel.getPlayers().size(); i++) {


                Integer playerId = mInputModel.getPlayers().get(i).getId();

                playerList.add(mInputModel.getPlayers().get(i));

                String hand = mInputModel.getPlayers().get(i).getHand();
                Integer experience = mInputModel.getPlayers().get(i).getExperience();
                Skills skills = mInputModel.getPlayers().get(i).getSkills();
                Integer claySkill = skills.getClay();
                Integer grassSkill = skills.getGrass();
                Integer hardSkill = skills.getHard();

                Log.d("Players", "playerId: " + playerId + " hand: " + hand + " experience: " + experience + " skill/clay : " + claySkill + " skill/grass: " + grassSkill + " skill/hard: " + hardSkill);

            }

            //endregion

            //region Create tournament model:

            for (int i = 0; i < mInputModel.getTournaments().size(); i++) {

                Integer tournamentId = mInputModel.getTournaments().get(i).getId();

                String surface = mInputModel.getTournaments().get(i).getSurface();
                surfaceList.add(mInputModel.getTournaments().get(i).getSurface());
                String type = mInputModel.getTournaments().get(i).getType();

                if (type.equals("elimination")) {

                    eliminationList.add(mInputModel.getTournaments().get(i));
                } else {

                    leagueList.add(mInputModel.getTournaments().get(i));

                }

                Log.d("Tournaments", "tournamentId: " + tournamentId + " surface: " + surface + " type: " + type);

                tournamentTypeList.add(mInputModel.getTournaments().get(i).getType());

            }
            //endregion

            //region Get tournament results respectively:
            for (int j = 0; j < mInputModel.getTournaments().size(); j++) {

                //region If tournament type is elimination, elimination match is played:
                if (mInputModel.getTournaments().get(j).getType().equalsIgnoreCase("elimination")) {

                    playEliminationMatchFirstTour(j);

                    playEliminationMatchOtherTours(8, j);

                    playEliminationMatchOtherTours(4, j);

                    playEliminationMatchOtherTours(2, j);

                    Log.d("tournamentWinner", "eliminationId: " + " winner " + + playerList.get(6).getId() + " gained experience: " + playerList.get(6).getGainedExperience() + " total experience: " + playerList.get(6).getExperience());

                }

                //endregion

                //region If tournament type is league, league match is played:
                else {
                    playLeagueMatch();

                    Log.d("tournamentWinner", "leagueId: " + " winner " + playerList.get(6).getId() + " experience: " + playerList.get(6).getGainedExperience() + " total experience: " + playerList.get(6).getExperience());

                }
                //endregion

            }
            //endregion

            sortByGainedExperience();

            //region Displays gained and total experience results of players in a descending order:
            TableLayout tableLayout = findViewById(R.id.finalScores);

            for (int i = 0; i < playerList.size(); i++) {

                TableRow row = new TableRow(MainActivity.this);

                TextView orderIdTv = new TextView(MainActivity.this);
                setTextViewProperties(orderIdTv);
                orderIdTv.setText(String.valueOf(i + 1));

                TextView playerIdTv = new TextView(MainActivity.this);
                setTextViewProperties(playerIdTv);
                playerIdTv.setText(playerList.get(i).getId().toString());

                TextView gainedExperienceTv = new TextView(MainActivity.this);
                setTextViewProperties(gainedExperienceTv);
                gainedExperienceTv.setText(playerList.get(i).getGainedExperience().toString());

                TextView totalExperienceTv = new TextView(MainActivity.this);
                setTextViewProperties(totalExperienceTv);
                totalExperienceTv.setText(playerList.get(i).getExperience().toString());


                row.addView(orderIdTv);
                row.addView(playerIdTv);
                row.addView(gainedExperienceTv);
                row.addView(totalExperienceTv);

                tableLayout.addView(row);

            }
            //endregion

    }

    /**
     * This method creates two teams consisting of 8 players randomly. The ith players of each team
     * play a match with each other and the winner earns a chance to play in the other elimination
     * tours.
     *
     * @param eliminationId is the id of the elimination tournament.
     **/
    private void playEliminationMatchFirstTour(int eliminationId) {

        List<Player> tempList = new ArrayList<>(playerList);

        eliminationTeam1.clear();
        eliminationTeam2.clear();
        while (eliminationTeam1.size() < 8) {

            int a = (int) (Math.random() * (tempList.size()));
            int b = (int) (Math.random() * (tempList.size()));

            if (a != b) {

                playerOfEliminationTeam1 = tempList.get(a);
                playerOfEliminationTeam2 = tempList.get(b);

                eliminationTeam1.add(playerOfEliminationTeam1);
                eliminationTeam2.add(playerOfEliminationTeam2);

                tempList.remove(playerOfEliminationTeam1);
                tempList.remove(playerOfEliminationTeam2);

            }
        }
        decideEliminationWinner(eliminationId);
    }

    /**
     * This method creates winner teams of the previous elimination phases. The ith player in the
     * winner group of the previous elimination phase, plays with the (i+1)the player.
     *
     * @param playerNumber is the number of players which win the previous elimination phase and
     *                     have right to play in this elimination phase.
     * @param eliminationId is the id of the elimination tournament.
     *
     **/
    private void playEliminationMatchOtherTours(int playerNumber, int eliminationId) {

        eliminationTeam1.clear();
        eliminationTeam2.clear();

        for (int i = 0; i < playerNumber; i = i + 2) {

            playerOfEliminationTeam1 = winnerList.get(i);

            eliminationTeam1.add(playerOfEliminationTeam1);

        }

        for (int i = 1; i < playerNumber; i = i + 2) {

            playerOfEliminationTeam2 = winnerList.get(i);

            eliminationTeam2.add(playerOfEliminationTeam2);
        }

        decideEliminationWinner(eliminationId);
    }

    /**
     * In a league tournament, there are 120 matches (C(16,2)). First players and second players
     * of the match are kept in a model. Player matches in the league is like that: P1-P2, P1-P3,
     * P1-P4,..., P14-P15, P14-P16, P15-P16. Therefore, a player matches with each player just once.
     *
     * With the "shuffle" method, matching order is turned to a random match.
     **/
    private void playLeagueMatch() {

        int playerOfLeagueTeam1 = 0;
        while (playerOfLeagueTeam1 < playerList.size()) {

            int playerOfLeagueTeam2 = playerOfLeagueTeam1;

            while (playerOfLeagueTeam2 < playerList.size()) {

                if (playerOfLeagueTeam1 != playerOfLeagueTeam2) {

                    ListModel model = new ListModel();

                    model.setFirstPlayer(playerList.get(playerOfLeagueTeam1));
                    model.setSecondPlayer(playerList.get(playerOfLeagueTeam2));

                    listOfMatchOfPlayersInLeague.add(model);

                }
                playerOfLeagueTeam2++;

            }
            playerOfLeagueTeam1++;

        }

        for (int i = 0; i < leagueList.size(); i++) {

            Collections.shuffle(listOfMatchOfPlayersInLeague);
            decideLeagueWinner(i);
        }

    }

    /**
     * This method decides the winner of the elimination tournament according to the match rules.
     *
     * @param eliminationId is the id of the elimination tournament.
     **/
    private void decideEliminationWinner(int eliminationId) {

        winnerList.clear();
        Player firstPlayer;
        Player secondPlayer;

        for (int i = 0; i < eliminationTeam1.size(); i++) {

             firstPlayer = eliminationTeam1.get(i);
             secondPlayer = eliminationTeam2.get(i);

            totalExperienceOfPlayerOfTeam1 = firstPlayer.getExperience();
            totalExperienceOfPlayerOfTeam2 = secondPlayer.getExperience();

            gainedExperienceOfPlayerOfTeam1 = 1;
            gainedExperienceOfPlayerOfTeam2 =  1;

            //region The player which has higher total experience gains experience score of 3:
            if (totalExperienceOfPlayerOfTeam1 > totalExperienceOfPlayerOfTeam2) {

                gainedExperienceOfPlayerOfTeam1 = gainedExperienceOfPlayerOfTeam1 + 3;
            }

            else {

                gainedExperienceOfPlayerOfTeam2 = gainedExperienceOfPlayerOfTeam2 + 3;
            }
            //endregion


            //region Players who use their left hands gain experience score of 2:
            if (eliminationTeam1.get(i).getHand().equals("left")) {

                gainedExperienceOfPlayerOfTeam1 = gainedExperienceOfPlayerOfTeam1 + 2;
            }

            else if (eliminationTeam2.get(i).getHand().equals("left")) {

                gainedExperienceOfPlayerOfTeam2 = gainedExperienceOfPlayerOfTeam2 + 2;
            }
            //endregion

            //region The player who has advantage over the skill at the ground type gains experience score of 4:
            switch (tournamentTypeList.get(eliminationId)) {

                case "clay":

                    if (eliminationTeam1.get(i).getSkills().getClay() > eliminationTeam2.get(i).getSkills().getClay()) {

                        gainedExperienceOfPlayerOfTeam1 = gainedExperienceOfPlayerOfTeam1 + 4;
                    }

                    else {

                        gainedExperienceOfPlayerOfTeam2 = gainedExperienceOfPlayerOfTeam2 + 4;
                    }
                    break;

                case "grass":

                    if (eliminationTeam1.get(i).getSkills().getClay() > eliminationTeam2.get(i).getSkills().getGrass()) {

                        gainedExperienceOfPlayerOfTeam1 = gainedExperienceOfPlayerOfTeam1 + 4;
                    }

                    else {

                        gainedExperienceOfPlayerOfTeam2 = gainedExperienceOfPlayerOfTeam2 + 4;
                    }

                    break;

                case "hard":

                    if (eliminationTeam1.get(i).getSkills().getClay() > eliminationTeam2.get(i).getSkills().getHard()) {

                        gainedExperienceOfPlayerOfTeam1 = gainedExperienceOfPlayerOfTeam1 + 4;
                    }

                    else {

                        gainedExperienceOfPlayerOfTeam2 = gainedExperienceOfPlayerOfTeam2 + 4;
                    }
                    break;
            }
            //endregion


            //region Probability of winning of player from Team 1 and Team 2 (the total probability is 1):
            double pr1 = (double) gainedExperienceOfPlayerOfTeam1 / (gainedExperienceOfPlayerOfTeam1 + gainedExperienceOfPlayerOfTeam2);
            double pr2 = (double) gainedExperienceOfPlayerOfTeam2 / (gainedExperienceOfPlayerOfTeam1 + gainedExperienceOfPlayerOfTeam2);
            //endregion

            double randomNumber = Math.random();

            Player winner;
            Player loser;

            //region If the probability of winning of player from Team 1 is higher than the other, it is more probable to be higher than a random number btw 0 and 1 (and vice versa):
            if (pr1 > pr2) {

                if (pr1 > randomNumber) {

                    winner = eliminationTeam1.get(i);
                    loser = eliminationTeam2.get(i);
                } else {

                    winner = eliminationTeam2.get(i);
                    loser = eliminationTeam1.get(i);
                }
            } else {

                if (pr2 > randomNumber) {

                    winner = eliminationTeam2.get(i);
                    loser = eliminationTeam1.get(i);
                } else {

                    winner = eliminationTeam1.get(i);
                    loser = eliminationTeam2.get(i);
                }
            }
            //endregion


            //region Update gained experience scores according to the winning case of players:
            if (winner == eliminationTeam1.get(i)) {

                gainedExperienceOfPlayerOfTeam1 = gainedExperienceOfPlayerOfTeam1 + 20;
                gainedExperienceOfPlayerOfTeam2 = gainedExperienceOfPlayerOfTeam2 + 10;

                totalExperienceOfPlayerOfTeam1 = winner.getExperience() + gainedExperienceOfPlayerOfTeam1;
                totalExperienceOfPlayerOfTeam2 = loser.getExperience() + gainedExperienceOfPlayerOfTeam2;

                winner.setGainedExperience(winner.getGainedExperience() + gainedExperienceOfPlayerOfTeam1);
                loser.setGainedExperience(loser.getGainedExperience() + gainedExperienceOfPlayerOfTeam2);

                winner.setExperience(totalExperienceOfPlayerOfTeam1);
                loser.setExperience(totalExperienceOfPlayerOfTeam2);

            } else {

                gainedExperienceOfPlayerOfTeam1 = gainedExperienceOfPlayerOfTeam1 + 10;
                gainedExperienceOfPlayerOfTeam2 = gainedExperienceOfPlayerOfTeam2 + 20;

                totalExperienceOfPlayerOfTeam2 = winner.getExperience() + gainedExperienceOfPlayerOfTeam2;
                totalExperienceOfPlayerOfTeam1 = loser.getExperience() + gainedExperienceOfPlayerOfTeam1;

                winner.setGainedExperience(winner.getGainedExperience() + gainedExperienceOfPlayerOfTeam2);
                loser.setGainedExperience(loser.getGainedExperience() + gainedExperienceOfPlayerOfTeam1);


                winner.setExperience(totalExperienceOfPlayerOfTeam2);
                loser.setExperience(totalExperienceOfPlayerOfTeam1);
            }
            //endregion

            //region Winner list is updated because the winner of this elimination phase is going to play in the next phase of elimination:
            winnerList.add(winner);
            //endregion
            updateExperience(winner.getId(), winner.getExperience(), winner.getGainedExperience());
            updateExperience(loser.getId(), loser.getExperience(), loser.getGainedExperience());

        }


    }

    /**
     * This method decides the winner of the league tournament according to the match rules.
     *
     * @param tournamentId is the id of the league tournament.
     **/
    private void decideLeagueWinner(int tournamentId) {

        for (int i = 0; i < listOfMatchOfPlayersInLeague.size(); i++) {

            Player firstPlayer = listOfMatchOfPlayersInLeague.get(i).getFirstPlayer();
            Player secondPlayer = listOfMatchOfPlayersInLeague.get(i).getSecondPlayer();

            totalExperienceOfPlayerOfTeam1 = firstPlayer.getExperience();
            totalExperienceOfPlayerOfTeam2 = secondPlayer.getExperience();

            //region Matching gives gained experience score of 1 to both players:
            gainedExperienceOfPlayerOfTeam1 = 1;
            gainedExperienceOfPlayerOfTeam2 = 1;
            //endregion

            //region The player which has higher total experience gains experience score of 3:
            if (totalExperienceOfPlayerOfTeam1 > totalExperienceOfPlayerOfTeam2) {

                gainedExperienceOfPlayerOfTeam1 = gainedExperienceOfPlayerOfTeam1 + 3;
            }

            else {

                gainedExperienceOfPlayerOfTeam2 = gainedExperienceOfPlayerOfTeam2 + 3;
            }

            //endregion

            //region Players who use their left hands gain experience score of 2:
            if (firstPlayer.getHand().equals("left")) {

                gainedExperienceOfPlayerOfTeam1 = gainedExperienceOfPlayerOfTeam1 + 2;
            }

            else if (secondPlayer.getHand().equals("left")) {

                gainedExperienceOfPlayerOfTeam2 = gainedExperienceOfPlayerOfTeam2 + 2;
            }
            //endregion

            //region The player who has advantage over the skill at the ground type gains experience score of 4:
            switch (leagueList.get(tournamentId).getSurface()) {
                case "clay":

                    if (firstPlayer.getSkills().getClay() > secondPlayer.getSkills().getClay()) {

                        gainedExperienceOfPlayerOfTeam1 = gainedExperienceOfPlayerOfTeam1 + 4;
                    }

                    if (secondPlayer.getSkills().getClay() > firstPlayer.getSkills().getClay()) {

                        gainedExperienceOfPlayerOfTeam2 = gainedExperienceOfPlayerOfTeam2 + 4;
                    }
                    break;

                case "grass":

                    if (firstPlayer.getSkills().getClay() > secondPlayer.getSkills().getGrass()) {

                        gainedExperienceOfPlayerOfTeam1 = gainedExperienceOfPlayerOfTeam1 + 4;
                    }

                    if (secondPlayer.getSkills().getClay() > firstPlayer.getSkills().getGrass()) {

                        gainedExperienceOfPlayerOfTeam2 = gainedExperienceOfPlayerOfTeam2 + 4;
                    }

                    break;

                case "hard":

                    if (firstPlayer.getSkills().getClay() > secondPlayer.getSkills().getHard()) {

                        gainedExperienceOfPlayerOfTeam1 = gainedExperienceOfPlayerOfTeam1 + 4;
                    }

                    if (secondPlayer.getSkills().getClay() > firstPlayer.getSkills().getHard()) {

                        gainedExperienceOfPlayerOfTeam2 = gainedExperienceOfPlayerOfTeam2 + 4;
                    }
                    break;
            }

            //endregion

            //region Probability of winning of player from Team 1 and Team 2 (the total probability is 1):
            double pr1 = (double) gainedExperienceOfPlayerOfTeam1 / (gainedExperienceOfPlayerOfTeam1 + gainedExperienceOfPlayerOfTeam2);
            double pr2 = (double) gainedExperienceOfPlayerOfTeam2 / (gainedExperienceOfPlayerOfTeam1 + gainedExperienceOfPlayerOfTeam2);
            //endregion

            double randomNumber = Math.random();
            Player winner;
            Player loser;

            //region If the probability of winning of player from Team 1 is higher than the other, it is more probable to be higher than a random number btw 0 and 1 (and vice versa):
            if (pr1 > pr2) {

                if (pr1 > randomNumber) {

                    winner = firstPlayer;
                    loser = secondPlayer;
                } else {

                    winner = secondPlayer;
                    loser = firstPlayer;
                }
            } else {

                if (pr2 > randomNumber) {

                    winner = secondPlayer;
                    loser = firstPlayer;
                } else {

                    winner = firstPlayer;
                    loser = secondPlayer;
                }
            }
            //endregion

            //region Update gained experience scores according to the winning case of players:
            if (winner == firstPlayer) {

                gainedExperienceOfPlayerOfTeam1 = gainedExperienceOfPlayerOfTeam1 + 10;
                gainedExperienceOfPlayerOfTeam2 = gainedExperienceOfPlayerOfTeam2 + 1;

                totalExperienceOfPlayerOfTeam1 = winner.getExperience() + gainedExperienceOfPlayerOfTeam1;
                totalExperienceOfPlayerOfTeam2 = loser.getExperience() + gainedExperienceOfPlayerOfTeam2;

                winner.setGainedExperience(winner.getGainedExperience() + gainedExperienceOfPlayerOfTeam1);
                loser.setGainedExperience(loser.getGainedExperience() + gainedExperienceOfPlayerOfTeam2);

                winner.setExperience(totalExperienceOfPlayerOfTeam1);
                loser.setExperience(totalExperienceOfPlayerOfTeam2);

            } else {

                gainedExperienceOfPlayerOfTeam1 = gainedExperienceOfPlayerOfTeam1 + 1;
                gainedExperienceOfPlayerOfTeam2 = gainedExperienceOfPlayerOfTeam2 + 10;

                totalExperienceOfPlayerOfTeam2 = winner.getExperience() + gainedExperienceOfPlayerOfTeam2;
                totalExperienceOfPlayerOfTeam1 = loser.getExperience() + gainedExperienceOfPlayerOfTeam1;

                winner.setGainedExperience(winner.getGainedExperience() + gainedExperienceOfPlayerOfTeam2);
                loser.setGainedExperience(loser.getGainedExperience() + gainedExperienceOfPlayerOfTeam1);

                winner.setExperience(totalExperienceOfPlayerOfTeam2);
                loser.setExperience(totalExperienceOfPlayerOfTeam1);


            }
            //endregion
            updateExperience(winner.getId(), winner.getExperience(), winner.getGainedExperience());
            updateExperience(loser.getId(), loser.getExperience(), loser.getGainedExperience());

        }


    }

    /**
     * This method sorts players according to their gained experiences in a descending order:
     *
     **/
    private void sortByGainedExperience() {

        Collections.sort(playerList, new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                int gainedExperienceDiff = o2.getGainedExperience() - o1.getGainedExperience();

                if (gainedExperienceDiff == 0) {
                    return o2.getExperience() - o1.getExperience();
                } else {

                    return gainedExperienceDiff;
                }
            }
        });
    }

    /**
     * This method sets TextView properties in the TableLayout:
     *
     * @param textView is the TextView of which parameters is going to be set.
     *
     **/
    private void setTextViewProperties(TextView textView) {

        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f);

        textView.setPadding(8, 8, 8, 8);
        textView.setGravity(Gravity.CENTER);
        textView.setLayoutParams(lp);
        textView.setTextColor(Color.WHITE);
    }
    private void updateExperience(int userId , int userExperience, int userGainedExperience){

        for(int i = 0 ; i < playerList.size() ; i++){
            if(playerList.get(i).getId() == userId ){
                Player player =  playerList.get(i);
                player.setExperience(userExperience);
                player.setGainedExperience(userGainedExperience);
            }
        }

    }

}

