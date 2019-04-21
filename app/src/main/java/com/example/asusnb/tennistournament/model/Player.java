package com.example.asusnb.tennistournament.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "hand",
        "experience",
        "skills"
})
public class Player {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("hand")
    private String hand;
    @JsonProperty("experience")
    private Integer experience;
    @JsonProperty("skills")
    private Skills skills;
    @JsonIgnore
    private Integer gainedExperience = 0;

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("hand")
    public String getHand() {
        return hand;
    }

    @JsonProperty("experience")
    public Integer getExperience() {
        return experience;
    }

    @JsonProperty("skills")
    public Skills getSkills() {
        return skills;
    }

    public Integer getGainedExperience() {

        return gainedExperience;
    }

    public void addGainedExperience(Integer experience) {

        this.gainedExperience += experience;
        this.experience += experience;
    }

}