package com.example.asusnb.tennistournament.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "players",
        "tournaments"
})
public class InputModel {

    @JsonProperty("players")
    private List<Player> players = null;
    @JsonProperty("tournaments")
    private List<Tournament> tournaments = null;

    @JsonProperty("players")
    public List<Player> getPlayers() {
        return players;
    }

    @JsonProperty("tournaments")
    public List<Tournament> getTournaments() {
        return tournaments;
    }


}