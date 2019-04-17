package com.example.asusnb.tennistournament.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.asusnb.tennistournament.R;
import com.example.asusnb.tennistournament.model.Player;
import com.example.asusnb.tennistournament.model.ResponseModel;
import com.example.asusnb.tennistournament.model.Skills;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
    private List<String> eliminationTypeList = new ArrayList<>();
    private Integer experience = null;
    private Skills skills = new Skills();
    private Integer claySkill = null;
    private Integer grassSkill = null;
    private Integer hardSkill = null;
    private Integer gainedExperience1 = null;
    private Integer gainedExperience2 = null;

    private Player pA;
    private Player pB;

    private Integer totalExperience1 = null;
    private Integer totalExperience2 = null;

    private List<Player> playerPair1 = new ArrayList<Player>();
    private List<Player> playerPair2 = new ArrayList<Player>();
    private List<Player> winnerList = new ArrayList<Player>();
    private List<Player> winnerGroupList = new ArrayList<Player>();

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

                if(type.equals("elimination")){

                    eliminationTypeList.add(type);
                }


                Log.d("Tournaments", "tournamentId: " + tournamentId + " surface: " + surface + " type: " + type);
            }

            for(int i = 0; i < eliminationTypeList.size(); i++) {

                playEliminationMatchFirstTour();

                playEliminationMatchOtherTours(8);

                playEliminationMatchOtherTours(4);

                playEliminationMatchOtherTours(2);




            }



        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void playEliminationMatchFirstTour() {

        if(playerList.size() == 0) {
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
        decideWinner();
    }

    private void playEliminationMatchOtherTours(int playerNumber) {

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

        decideWinner();
    }


    private void decideWinner() {

        winnerList.clear();

        for (int i = 0; i < playerPair1.size(); i++) {

            totalExperience1 = playerPair1.get(i).getExperience();
            totalExperience2 = playerPair2.get(i).getExperience();

            gainedExperience1 = totalExperience1 + 1;
            gainedExperience2 = totalExperience2 + 1;

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

            if (surfaceList.get(i).equals("clay")) {

                if (playerPair1.get(i).getSkills().getClay() > playerPair2.get(i).getSkills().getClay()) {

                    gainedExperience1 = gainedExperience1 + 4;
                }

                if (playerPair2.get(i).getSkills().getClay() > playerPair1.get(i).getSkills().getClay()) {

                    gainedExperience2 = gainedExperience2 + 4;
                }
            } else if (surfaceList.get(i).equals("grass")) {

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

            if (gainedExperience1 > gainedExperience2) {

                winner = playerPair1.get(i);

            } else {

                winner = playerPair2.get(i);

            }

            winnerList.add(winner);
            Log.d("winner", "decideWinner: " + winner.getId());

        }
    }

}


