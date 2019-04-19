package com.example.asusnb.tennistournament.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.asusnb.tennistournament.R;
import com.example.asusnb.tennistournament.model.ListModel;
import com.example.asusnb.tennistournament.model.Player;
import com.example.asusnb.tennistournament.model.ResponseModel;
import com.example.asusnb.tennistournament.model.Skills;
import com.example.asusnb.tennistournament.model.Tournament;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
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
    private ObjectMapper objectMapper = null;
    private JsonFactory jsonFactory = null;
    private JsonParser jp = null;
    private StringBuilder total;

    private ResponseModel mResponseModel;

    private List<Player> players = null;
    private Player player = null;
    private String hand = null;
    private Integer playerId = null;
    private List<Player> playerList = new ArrayList<>();
    private List<Player> tempList = new ArrayList<>();
    private List<String> surfaceList = new ArrayList<>();
    private Integer experience = null;
    private Skills skills = new Skills();
    private Integer claySkill = null;
    private Integer grassSkill = null;
    private Integer hardSkill = null;
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
    private List<Tournament> leaugeList = new ArrayList<Tournament>();
    private List<Tournament> eleminationList = new ArrayList<Tournament>();
    private List<String> tournamentTypeList = new ArrayList<String>();

    private HashMap<Integer, Integer> hashMap = new HashMap<>();

    private Integer tournamentId = null;
    private String surface = null;
    private String type = null;

    private Player winner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        objectMapper = new ObjectMapper();
        jsonFactory = new JsonFactory();


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
            jp = jsonFactory.createJsonParser(total.toString());
            mResponseModel = objectMapper.readValue(jp, ResponseModel.class);

            for (int i = 0; i < mResponseModel.getPlayers().size(); i++) {

                playerId = mResponseModel.getPlayers().get(i).getId();

                playerList.add(mResponseModel.getPlayers().get(i));
                this.tempList = playerList;

                hand = mResponseModel.getPlayers().get(i).getHand();
                experience = mResponseModel.getPlayers().get(i).getExperience();
                skills = mResponseModel.getPlayers().get(i).getSkills();
                claySkill = skills.getClay();
                grassSkill = skills.getGrass();
                hardSkill = skills.getHard();

                Log.d("Players", "playerId: " + playerId + " hand: " + hand + " experience: " + experience + " skill/clay : " + claySkill + " skill/grass: " + grassSkill + " skill/hard: " + hardSkill);
            }

            for (int i = 0; i < mResponseModel.getTournaments().size(); i++) {

                tournamentId = mResponseModel.getTournaments().get(i).getId();

                surface = mResponseModel.getTournaments().get(i).getSurface();
                surfaceList.add(mResponseModel.getTournaments().get(i).getSurface());
                type = mResponseModel.getTournaments().get(i).getType();

                if (type.equals("elimination")) {

                    eleminationList.add(mResponseModel.getTournaments().get(i));
                } else {

                    leaugeList.add(mResponseModel.getTournaments().get(i));

                }

                Log.d("Tournaments", "tournamentId: " + tournamentId + " surface: " + surface + " type: " + type);

                tournamentTypeList.add(mResponseModel.getTournaments().get(i).getType());
            }

            for (int j = 0; j < mResponseModel.getTournaments().size(); j++) {

                if (mResponseModel.getTournaments().get(j).getType().equalsIgnoreCase("elimination")) {

                    playEliminationMatchFirstTour(j);

                    playEliminationMatchOtherTours(8, j);

                    playEliminationMatchOtherTours(4, j);

                    playEliminationMatchOtherTours(2, j);
                    Log.d("tournamentWinner", "eliminationId: " + " winner " + winner.getId() + " experience: " + winner.getExperience());

                } else {
                    playLeagueMatch();

                    Log.d("tournamentWinner", "leagueId: " + " winner " + winner.getId() + " experience: " + winner.getExperience());


                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void playEliminationMatchFirstTour(int eleminationId) {

        if (playerList.size() == 0) {
            try {
                jp = jsonFactory.createJsonParser(total.toString());
                mResponseModel = objectMapper.readValue(jp, ResponseModel.class);

            } catch (IOException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < mResponseModel.getPlayers().size(); i++) {

                playerId = mResponseModel.getPlayers().get(i).getId();

                playerList.add(mResponseModel.getPlayers().get(i));

            }
        }

        playerPair1.clear();
        playerPair2.clear();
        while (playerPair1.size() < 8) {

            int a = (int) (Math.random() * (playerList.size()));
            int b = (int) (Math.random() * (playerList.size()));

            if (a != b) {

                pA = playerList.get(a);
                pB = playerList.get(b);


                playerPair1.add(pA);
                playerPair2.add(pB);


                playerList.remove(pA);
                playerList.remove(pB);

            }
        }
        decideEliminationWinner(eleminationId);
    }

    private void playLeagueMatch() {
        if (playerList.size() == 0) {
            try {
                jp = jsonFactory.createJsonParser(total.toString());
                mResponseModel = objectMapper.readValue(jp, ResponseModel.class);

            } catch (IOException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < mResponseModel.getPlayers().size(); i++) {

                playerId = mResponseModel.getPlayers().get(i).getId();

                playerList.add(mResponseModel.getPlayers().get(i));

            }
        }


        int count = 0;
        while (count < playerList.size()) {

            int count2 = count;

            hashMap = new HashMap<>();
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

        for (int i = 0; i < leaugeList.size(); i++) {

            Collections.shuffle(list);
            decideLeagueWinner(i);
        }

    }

    private void updateExperience(int id, int totalExperience) {

        for(int i = 0; i < playerList.size(); i++) {

            if(playerList.get(i).getId() == id) {

                playerList.get(i).setExperience(totalExperience);
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
                } else {

                    winner = playerPair2.get(i);
                }
            } else {

                if (pr2 > randomNumber) {

                    winner = playerPair2.get(i);
                } else {

                    winner = playerPair1.get(i);
                }
            }

            if (winner == playerPair1.get(i)) {

                totalExperience1 = totalExperience1 + gainedExperience1 + 20;
                totalExperience2 = totalExperience2 + gainedExperience2 + 10;
                winner.setExperience(totalExperience1);
            } else if (winner == playerPair2.get(i)) {

                totalExperience2 = totalExperience2 + gainedExperience2 + 20;
                totalExperience1 = totalExperience1 + gainedExperience1 + 10;
                winner.setExperience(totalExperience2);
            }


            winnerList.add(winner);
            updateExperience(winner.getId(), winner.getExperience());
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


            if (leaugeList.get(tournamentId).getSurface().equals("clay")) {

                if (firstList.get(i).getSkills().getClay() > secondList.get(i).getSkills().getClay()) {

                    gainedExperience1 = gainedExperience1 + 4;
                }

                if (secondList.get(i).getSkills().getClay() > firstList.get(i).getSkills().getClay()) {

                    gainedExperience2 = gainedExperience2 + 4;
                }
            } else if (leaugeList.get(tournamentId).getSurface().equals("grass")) {

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
                } else {

                    winner = secondList.get(i);
                }
            } else {

                if (pr2 > randomNumber) {

                    winner = secondList.get(i);
                } else {

                    winner = firstList.get(i);
                }
            }

            if (winner == firstList.get(i)) {

                totalExperience1 = totalExperience1 + gainedExperience1 + 10;
                totalExperience2 = totalExperience2 + gainedExperience2 + 1;
                winner.setExperience(totalExperience1);
            } else if (winner == secondList.get(i)) {

                totalExperience2 = totalExperience2 + gainedExperience2 + 10;
                totalExperience1 = totalExperience1 + gainedExperience1 + 1;
                winner.setExperience(totalExperience2);

            }


            winnerList.add(winner);
            updateExperience(winner.getId(), winner.getExperience());

        }

        //list.clear();
    }

}


