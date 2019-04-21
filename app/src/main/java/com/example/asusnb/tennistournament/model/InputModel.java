package com.example.asusnb.tennistournament.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("players")
    public List<Player> getPlayers() {
        return players;
    }

    @JsonProperty("players")
    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public InputModel withPlayers(List<Player> players) {
        this.players = players;
        return this;
    }

    @JsonProperty("tournaments")
    public List<Tournament> getTournaments() {
        return tournaments;
    }

    @JsonProperty("tournaments")
    public void setTournaments(List<Tournament> tournaments) {
        this.tournaments = tournaments;
    }

    public InputModel withTournaments(List<Tournament> tournaments) {
        this.tournaments = tournaments;
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public InputModel withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}