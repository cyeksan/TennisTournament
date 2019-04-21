package com.example.asusnb.tennistournament.view;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private StringBuilder total;

    private List<Player> playerList = new ArrayList<>();
    private List<String> surfaceList = new ArrayList<>();
    private Integer gainedExperienceOfPlayerOfTeam1 = null;
    private Integer gainedExperienceOfPlayerOfTeam2 = null;

    private Player playerOfEliminationTeam1;
    private Player playerOfEliminationTeam2;
    List<ListModel> list = new ArrayList<>();

    private Integer totalExperienceOfPlayerOfTeam1 = null;
    private Integer totalExperienceOfPlayerOfTeam2 = null;

    private List<Player> eliminationTeam1 = new ArrayList<>();
    private List<Player> eliminationTeam2 = new ArrayList<>();
    private List<Player> winnerList = new ArrayList<>();
    private List<Tournament> leagueList = new ArrayList<>();
    private List<Tournament> eliminationList = new ArrayList<>();
    private List<String> tournamentTypeList = new ArrayList<>();

    private Player winner;
    private Player loser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory jsonFactory = new JsonFactory();
        TableLayout table = findViewById(R.id.table);

        try (InputStream is = getResources().openRawResource(R.raw.input)) {

            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            total = new StringBuilder();
            for (String line; (line = br.readLine()) != null; ) {
                total.append(line);
            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        try {
            JsonParser jp = jsonFactory.createJsonParser(total.toString());
            InputModel mInputModel = objectMapper.readValue(jp, InputModel.class);

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

            for (int j = 0; j < mInputModel.getTournaments().size(); j++) {

                if (mInputModel.getTournaments().get(j).getType().equalsIgnoreCase("elimination")) {

                    playEliminationMatchFirstTour(j);

                    playEliminationMatchOtherTours(8, j);

                    playEliminationMatchOtherTours(4, j);

                    playEliminationMatchOtherTours(2, j);

                    Log.d("tournamentWinner", "eliminationId: " + " winner " + winner.getId() + " gained experience: " + winner.getGainedExperience() + " total experience: " + winner.getExperience());

                    createTableView(mInputModel, j);
                } else {
                    playLeagueMatch();

                    Log.d("tournamentWinner", "leagueId: " + " winner " + winner.getId() + " experience: " + winner.getGainedExperience() + " total experience: " + winner.getExperience());

                    createTableView(mInputModel, j);
                }



            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

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

    private void playLeagueMatch() {

        int playerOfLeagueTeam1 = 0;
        while (playerOfLeagueTeam1 < playerList.size()) {

            int playerOfLeagueTeam2 = playerOfLeagueTeam1;

            while (playerOfLeagueTeam2 < playerList.size()) {

                if (playerOfLeagueTeam1 != playerOfLeagueTeam2) {

                    ListModel model = new ListModel();


                    model.setFirstPlayer(playerList.get(playerOfLeagueTeam1));
                    model.setSecondPlayer(playerList.get(playerOfLeagueTeam2));

                    list.add(model);
                    Log.d("hashmapim", "playLeagueMatch: " + "count: " + playerOfLeagueTeam1 + "count2: " + playerOfLeagueTeam2);

                }
                playerOfLeagueTeam2++;

            }
            playerOfLeagueTeam1++;

        }

        Log.d("hashmapim2", new Gson().toJson(list));
        Log.d("hashmapim2", list.size() + "");

        for (int i = 0; i < leagueList.size(); i++) {

            Collections.shuffle(list);
            decideLeagueWinner(i);
        }

    }

    private void playEliminationMatchOtherTours(int playerNumber, int eleminationId) {

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

        decideEliminationWinner(eleminationId);
    }


    private void decideEliminationWinner(int eleminationId) {

        winnerList.clear();

        for (int i = 0; i < eliminationTeam1.size(); i++) {

            totalExperienceOfPlayerOfTeam1 = eliminationTeam1.get(i).getExperience();
            totalExperienceOfPlayerOfTeam2 = eliminationTeam2.get(i).getExperience();

            gainedExperienceOfPlayerOfTeam1 = 1;
            gainedExperienceOfPlayerOfTeam2 = 1;

            if (totalExperienceOfPlayerOfTeam1 > totalExperienceOfPlayerOfTeam2) {

                gainedExperienceOfPlayerOfTeam1 = gainedExperienceOfPlayerOfTeam1 + 3;
            }

            if (totalExperienceOfPlayerOfTeam2 > totalExperienceOfPlayerOfTeam1) {

                gainedExperienceOfPlayerOfTeam2 = gainedExperienceOfPlayerOfTeam2 + 3;
            }


            if (eliminationTeam1.get(i).getHand().equals("left")) {

                gainedExperienceOfPlayerOfTeam1 = gainedExperienceOfPlayerOfTeam1 + 2;
            }

            if (eliminationTeam2.get(i).getHand().equals("left")) {

                gainedExperienceOfPlayerOfTeam2 = gainedExperienceOfPlayerOfTeam2 + 2;
            }

            if (tournamentTypeList.get(eleminationId).equals("clay")) {

                if (eliminationTeam1.get(i).getSkills().getClay() > eliminationTeam2.get(i).getSkills().getClay()) {

                    gainedExperienceOfPlayerOfTeam1 = gainedExperienceOfPlayerOfTeam1 + 4;
                }

                if (eliminationTeam2.get(i).getSkills().getClay() > eliminationTeam1.get(i).getSkills().getClay()) {

                    gainedExperienceOfPlayerOfTeam2 = gainedExperienceOfPlayerOfTeam2 + 4;
                }
            } else if (tournamentTypeList.get(eleminationId).equals("grass")) {

                if (eliminationTeam1.get(i).getSkills().getClay() > eliminationTeam2.get(i).getSkills().getGrass()) {

                    gainedExperienceOfPlayerOfTeam1 = gainedExperienceOfPlayerOfTeam1 + 4;
                }

                if (eliminationTeam2.get(i).getSkills().getClay() > eliminationTeam1.get(i).getSkills().getGrass()) {

                    gainedExperienceOfPlayerOfTeam2 = gainedExperienceOfPlayerOfTeam2 + 4;
                }

            } else {

                if (eliminationTeam1.get(i).getSkills().getClay() > eliminationTeam2.get(i).getSkills().getHard()) {

                    gainedExperienceOfPlayerOfTeam1 = gainedExperienceOfPlayerOfTeam1 + 4;
                }

                if (eliminationTeam2.get(i).getSkills().getClay() > eliminationTeam1.get(i).getSkills().getHard()) {

                    gainedExperienceOfPlayerOfTeam2 = gainedExperienceOfPlayerOfTeam2 + 4;
                }
            }


            double pr1 = (double) gainedExperienceOfPlayerOfTeam1 / (gainedExperienceOfPlayerOfTeam1 + gainedExperienceOfPlayerOfTeam2);
            double pr2 = (double) gainedExperienceOfPlayerOfTeam2 / (gainedExperienceOfPlayerOfTeam1 + gainedExperienceOfPlayerOfTeam2);

            double randomNumber = Math.random();

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

            if (winner == eliminationTeam1.get(i)) {

                gainedExperienceOfPlayerOfTeam1 = gainedExperienceOfPlayerOfTeam1 + 20;
                gainedExperienceOfPlayerOfTeam2 = gainedExperienceOfPlayerOfTeam2 + 10;

                winner.addGainedExperience(gainedExperienceOfPlayerOfTeam1);
                loser.addGainedExperience(gainedExperienceOfPlayerOfTeam2);

            } else if (winner == eliminationTeam2.get(i)) {

                gainedExperienceOfPlayerOfTeam1 = gainedExperienceOfPlayerOfTeam1 + 10;
                gainedExperienceOfPlayerOfTeam2 = gainedExperienceOfPlayerOfTeam2 + 20;

                winner.addGainedExperience(gainedExperienceOfPlayerOfTeam2);
                loser.addGainedExperience(gainedExperienceOfPlayerOfTeam1);
            }


            winnerList.add(winner);
            //updateExperience(winner.getId(), winner.getExperience(), winner.getGainedExperience());
            //updateExperience(loser.getId(), loser.getExperience(), loser.getGainedExperience());
            Log.d("winner", "decideWinner: " + winner.getId() + "experience" + winner.getExperience());

        }
    }


    private void decideLeagueWinner(int tournamentId) {

        List<Player> firstList = new ArrayList<>();
        List<Player> secondList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {

            firstList.add(list.get(i).getFirstPlayer());
            secondList.add(list.get(i).getSecondPlayer());


            totalExperienceOfPlayerOfTeam1 = firstList.get(i).getExperience();
            totalExperienceOfPlayerOfTeam2 = secondList.get(i).getExperience();

            gainedExperienceOfPlayerOfTeam1 = 1;
            gainedExperienceOfPlayerOfTeam2 = 1;

            if (totalExperienceOfPlayerOfTeam1 > totalExperienceOfPlayerOfTeam2) {

                gainedExperienceOfPlayerOfTeam1 = gainedExperienceOfPlayerOfTeam1 + 3;
            }

            if (totalExperienceOfPlayerOfTeam2 > totalExperienceOfPlayerOfTeam1) {

                gainedExperienceOfPlayerOfTeam2 = gainedExperienceOfPlayerOfTeam2 + 3;
            }


            if (firstList.get(i).getHand().equals("left")) {

                gainedExperienceOfPlayerOfTeam1 = gainedExperienceOfPlayerOfTeam1 + 2;
            }

            if (secondList.get(i).getHand().equals("left")) {

                gainedExperienceOfPlayerOfTeam2 = gainedExperienceOfPlayerOfTeam2 + 2;
            }


            if (leagueList.get(tournamentId).getSurface().equals("clay")) {

                if (firstList.get(i).getSkills().getClay() > secondList.get(i).getSkills().getClay()) {

                    gainedExperienceOfPlayerOfTeam1 = gainedExperienceOfPlayerOfTeam1 + 4;
                }

                if (secondList.get(i).getSkills().getClay() > firstList.get(i).getSkills().getClay()) {

                    gainedExperienceOfPlayerOfTeam2 = gainedExperienceOfPlayerOfTeam2 + 4;
                }
            } else if (leagueList.get(tournamentId).getSurface().equals("grass")) {

                if (firstList.get(i).getSkills().getClay() > secondList.get(i).getSkills().getGrass()) {

                    gainedExperienceOfPlayerOfTeam1 = gainedExperienceOfPlayerOfTeam1 + 4;
                }

                if (secondList.get(i).getSkills().getClay() > firstList.get(i).getSkills().getGrass()) {

                    gainedExperienceOfPlayerOfTeam2 = gainedExperienceOfPlayerOfTeam2 + 4;
                }

            } else {

                if (firstList.get(i).getSkills().getClay() > secondList.get(i).getSkills().getHard()) {

                    gainedExperienceOfPlayerOfTeam1 = gainedExperienceOfPlayerOfTeam1 + 4;
                }

                if (secondList.get(i).getSkills().getClay() > firstList.get(i).getSkills().getHard()) {

                    gainedExperienceOfPlayerOfTeam2 = gainedExperienceOfPlayerOfTeam2 + 4;
                }
            }


            double pr1 = (double) gainedExperienceOfPlayerOfTeam1 / (gainedExperienceOfPlayerOfTeam1 + gainedExperienceOfPlayerOfTeam2);
            double pr2 = (double) gainedExperienceOfPlayerOfTeam2 / (gainedExperienceOfPlayerOfTeam1 + gainedExperienceOfPlayerOfTeam2);

            double randomNumber = Math.random();

            if (pr1 > pr2) {

                if (pr1 > randomNumber) {

                    winner = firstList.get(i);
                    loser = secondList.get(i);
                } else {

                    winner = secondList.get(i);
                    loser = firstList.get(i);
                }
            } else {

                if (pr2 > randomNumber) {

                    winner = secondList.get(i);
                    loser = firstList.get(i);
                } else {

                    winner = firstList.get(i);
                    loser = secondList.get(i);
                }
            }

            if (winner == firstList.get(i)) {

                gainedExperienceOfPlayerOfTeam1 = gainedExperienceOfPlayerOfTeam1 + 10;
                gainedExperienceOfPlayerOfTeam2 = gainedExperienceOfPlayerOfTeam2 + 1;

                winner.addGainedExperience(gainedExperienceOfPlayerOfTeam1);
                loser.addGainedExperience(gainedExperienceOfPlayerOfTeam2);

            } else if (winner == secondList.get(i)) {

                gainedExperienceOfPlayerOfTeam1 = gainedExperienceOfPlayerOfTeam1 + 1;
                gainedExperienceOfPlayerOfTeam2 = gainedExperienceOfPlayerOfTeam2 + 10;


                winner.addGainedExperience(gainedExperienceOfPlayerOfTeam2);
                loser.addGainedExperience(gainedExperienceOfPlayerOfTeam1);

            }

          //  updateExperience(winner.getId(), winner.getExperience(), winner.getGainedExperience());
           // updateExperience(loser.getId(), loser.getExperience(), loser.getGainedExperience());

        }

        //list.clear();
    }

    private void createTableView(InputModel mInputModel, int j) {

        TableLayout tableLayout = findViewById(R.id.table);

        TableRow row = new TableRow(MainActivity.this);

        TextView tournamentIdTv = new TextView(MainActivity.this);
        tournamentIdTv.setTextColor(Color.BLACK);
        tournamentIdTv.setText(mInputModel.getTournaments().get(j).getId() +"");

        TextView tournamentTypeTv = new TextView(MainActivity.this);
        tournamentTypeTv.setTextColor(Color.BLACK);
        tournamentTypeTv.setText(mInputModel.getTournaments().get(j).getType());


        TextView playerTv = new TextView(MainActivity.this);
        playerTv.setTextColor(Color.BLACK);
        playerTv.setText("" + winner.getId());

        TextView gainedExperienceTv = new TextView(MainActivity.this);
        gainedExperienceTv.setTextColor(Color.BLACK);
        gainedExperienceTv.setText(""+ winner.getGainedExperience());

        TextView totalExperienceTv = new TextView(MainActivity.this);
        totalExperienceTv.setTextColor(Color.BLACK);
        totalExperienceTv.setText(""+ winner.getExperience());

        row.addView(tournamentIdTv);
        row.addView(tournamentTypeTv);
        row.addView(playerTv);
        row.addView(gainedExperienceTv);
        row.addView(totalExperienceTv);

        tableLayout.addView(row);

        tableLayout.requestLayout();
    }

}
