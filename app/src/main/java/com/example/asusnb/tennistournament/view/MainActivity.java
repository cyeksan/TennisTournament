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
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private StringBuilder total;

    private List<Player> playerList = new ArrayList<>();
    private List<String> surfaceList = new ArrayList<>();
    private Integer gainedExperience1 = null;
    private Integer gainedExperience2 = null;

    private Player pA;
    private Player pB;
    List<ListModel> list = new ArrayList<>();

    private Integer totalExperience1 = null;
    private Integer totalExperience2 = null;

    private List<Player> playerPair1 = new ArrayList<Player>();
    private List<Player> playerPair2 = new ArrayList<Player>();
    private List<Player> winnerList = new ArrayList<Player>();
    private List<Player> loserList = new ArrayList<Player>();
    private List<Tournament> leagueList = new ArrayList<Tournament>();
    private List<Tournament> eliminationList = new ArrayList<Tournament>();
    private List<String> tournamentTypeList = new ArrayList<String>();

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
            Log.v("trial", total.toString());

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

                    table.addView(row);
                } else {
                    playLeagueMatch();

                    Log.d("tournamentWinner", "leagueId: " + " winner " + winner.getId() + " experience: " + winner.getGainedExperience() + " total experience: " + winner.getExperience());

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
                    table.addView(row);

                }



            }

            HashMap<Player, Integer> finalList = new HashMap<Player, Integer>();

            for (int i = 0; i < playerList.size(); i++) {

                finalList.put(playerList.get(i), playerList.get(i).getGainedExperience());

                Log.v("FİNAL", playerList.get(i).getGainedExperience() + "");


            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void playEliminationMatchFirstTour(int eleminationId) {

        List<Player> tempList = new ArrayList<>(playerList);

        playerPair1.clear();
        playerPair2.clear();
        while (playerPair1.size() < 8) {

            int a = (int) (Math.random() * (tempList.size()));
            int b = (int) (Math.random() * (tempList.size()));

            if (a != b) {

                pA = tempList.get(a);
                pB = tempList.get(b);


                playerPair1.add(pA);
                playerPair2.add(pB);


                tempList.remove(pA);
                tempList.remove(pB);

            }
        }
        decideEliminationWinner(eleminationId);
    }

    private void playLeagueMatch() {

        int count = 0;
        while (count < playerList.size()) {

            int count2 = count;

            while (count2 < 16) {

                if (count != count2) {

                    ListModel model = new ListModel();


                    model.setFirstPlayer(playerList.get(count));
                    model.setSecondPlayer(playerList.get(count2));

                    list.add(model);
                    Log.d("hashmapim", "playLeagueMatch: " + "count: " + count + "count2: " + count2);

                }
                count2++;

            }
            count++;

        }

        Log.d("hashmapim2", new Gson().toJson(list));
        Log.d("hashmapim2", list.size() + "");

        for (int i = 0; i < leagueList.size(); i++) {

            Collections.shuffle(list);
            decideLeagueWinner(i);
        }

    }

    private void updateExperience(int id, int totalExperience, int gainedExperience) {

        for (int i = 0; i < playerList.size(); i++) {

            if (playerList.get(i).getId() == id) {

                playerList.get(i).setExperience(totalExperience);
                playerList.get(i).setGainedExperience(gainedExperience);
            }
        }
    }

    private void playEliminationMatchOtherTours(int playerNumber, int eleminationId) {

        playerPair1.clear();
        playerPair2.clear();

        for (int i = 0; i < playerNumber; i = i + 2) {

            pA = winnerList.get(i);

            playerPair1.add(pA);

        }

        for (int i = 1; i < playerNumber; i = i + 2) {

            pB = winnerList.get(i);

            playerPair2.add(pB);
        }

        decideEliminationWinner(eleminationId);
    }


    private void decideEliminationWinner(int eleminationId) {

        winnerList.clear();

        for (int i = 0; i < playerPair1.size(); i++) {

            totalExperience1 = playerPair1.get(i).getExperience();
            totalExperience2 = playerPair2.get(i).getExperience();

            gainedExperience1 = 1;
            gainedExperience2 = 1;

            if (totalExperience1 > totalExperience2) {

                gainedExperience1 = gainedExperience1 + 3;
            }

            if (totalExperience2 > totalExperience1) {

                gainedExperience2 = gainedExperience2 + 3;
            }


            if (playerPair1.get(i).getHand().equals("left")) {

                gainedExperience1 = gainedExperience1 + 2;
            }

            if (playerPair2.get(i).getHand().equals("left")) {

                gainedExperience2 = gainedExperience2 + 2;
            }

            if (tournamentTypeList.get(eleminationId).equals("clay")) {

                if (playerPair1.get(i).getSkills().getClay() > playerPair2.get(i).getSkills().getClay()) {

                    gainedExperience1 = gainedExperience1 + 4;
                }

                if (playerPair2.get(i).getSkills().getClay() > playerPair1.get(i).getSkills().getClay()) {

                    gainedExperience2 = gainedExperience2 + 4;
                }
            } else if (tournamentTypeList.get(eleminationId).equals("grass")) {

                if (playerPair1.get(i).getSkills().getClay() > playerPair2.get(i).getSkills().getGrass()) {

                    gainedExperience1 = gainedExperience1 + 4;
                }

                if (playerPair2.get(i).getSkills().getClay() > playerPair1.get(i).getSkills().getGrass()) {

                    gainedExperience2 = gainedExperience2 + 4;
                }

            } else {

                if (playerPair1.get(i).getSkills().getClay() > playerPair2.get(i).getSkills().getHard()) {

                    gainedExperience1 = gainedExperience1 + 4;
                }

                if (playerPair2.get(i).getSkills().getClay() > playerPair1.get(i).getSkills().getHard()) {

                    gainedExperience2 = gainedExperience2 + 4;
                }
            }


            double pr1 = (double) gainedExperience1 / (gainedExperience1 + gainedExperience2);
            double pr2 = (double) gainedExperience2 / (gainedExperience1 + gainedExperience2);

            double randomNumber = Math.random();

            if (pr1 > pr2) {

                if (pr1 > randomNumber) {

                    winner = playerPair1.get(i);
                    loser = playerPair2.get(i);
                } else {

                    winner = playerPair2.get(i);
                    loser = playerPair1.get(i);
                }
            } else {

                if (pr2 > randomNumber) {

                    winner = playerPair2.get(i);
                    loser = playerPair1.get(i);
                } else {

                    winner = playerPair1.get(i);
                    loser = playerPair2.get(i);
                }
            }

            if (winner == playerPair1.get(i)) {

                gainedExperience1 = gainedExperience1 + 20;
                gainedExperience2 = gainedExperience2 + 10;

                winner.addGainedExperience(gainedExperience1);
                loser.addGainedExperience(gainedExperience2);

            } else if (winner == playerPair2.get(i)) {

                gainedExperience1 = gainedExperience1 + 10;
                gainedExperience2 = gainedExperience2 + 20;

                winner.addGainedExperience(gainedExperience2);
                loser.addGainedExperience(gainedExperience1);
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


            totalExperience1 = firstList.get(i).getExperience();
            totalExperience2 = secondList.get(i).getExperience();

            gainedExperience1 = 1;
            gainedExperience2 = 1;

            if (totalExperience1 > totalExperience2) {

                gainedExperience1 = gainedExperience1 + 3;
            }

            if (totalExperience2 > totalExperience1) {

                gainedExperience2 = gainedExperience2 + 3;
            }


            if (firstList.get(i).getHand().equals("left")) {

                gainedExperience1 = gainedExperience1 + 2;
            }

            if (secondList.get(i).getHand().equals("left")) {

                gainedExperience2 = gainedExperience2 + 2;
            }


            if (leagueList.get(tournamentId).getSurface().equals("clay")) {

                if (firstList.get(i).getSkills().getClay() > secondList.get(i).getSkills().getClay()) {

                    gainedExperience1 = gainedExperience1 + 4;
                }

                if (secondList.get(i).getSkills().getClay() > firstList.get(i).getSkills().getClay()) {

                    gainedExperience2 = gainedExperience2 + 4;
                }
            } else if (leagueList.get(tournamentId).getSurface().equals("grass")) {

                if (firstList.get(i).getSkills().getClay() > secondList.get(i).getSkills().getGrass()) {

                    gainedExperience1 = gainedExperience1 + 4;
                }

                if (secondList.get(i).getSkills().getClay() > firstList.get(i).getSkills().getGrass()) {

                    gainedExperience2 = gainedExperience2 + 4;
                }

            } else {

                if (firstList.get(i).getSkills().getClay() > secondList.get(i).getSkills().getHard()) {

                    gainedExperience1 = gainedExperience1 + 4;
                }

                if (secondList.get(i).getSkills().getClay() > firstList.get(i).getSkills().getHard()) {

                    gainedExperience2 = gainedExperience2 + 4;
                }
            }


            double pr1 = (double) gainedExperience1 / (gainedExperience1 + gainedExperience2);
            double pr2 = (double) gainedExperience2 / (gainedExperience1 + gainedExperience2);

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

                gainedExperience1 = gainedExperience1 + 10;
                gainedExperience2 = gainedExperience2 + 1;

                winner.addGainedExperience(gainedExperience1);
                loser.addGainedExperience(gainedExperience2);

            } else if (winner == secondList.get(i)) {

                gainedExperience1 = gainedExperience1 + 1;
                gainedExperience2 = gainedExperience2 + 10;


                winner.addGainedExperience(gainedExperience2);
                loser.addGainedExperience(gainedExperience1);

            }

          //  updateExperience(winner.getId(), winner.getExperience(), winner.getGainedExperience());
           // updateExperience(loser.getId(), loser.getExperience(), loser.getGainedExperience());

        }

        //list.clear();
    }

}
