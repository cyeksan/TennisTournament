package com.example.asusnb.tennistournament.model;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
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
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    @JsonIgnore
    private Integer gainedExperience = 0;

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    public Player withId(Integer id) {
        this.id = id;
        return this;
    }

    @JsonProperty("hand")
    public String getHand() {
        return hand;
    }

    @JsonProperty("hand")
    public void setHand(String hand) {
        this.hand = hand;
    }

    public Player withHand(String hand) {
        this.hand = hand;
        return this;
    }

    @JsonProperty("experience")
    public Integer getExperience() {
        return experience;
    }

    @JsonProperty("experience")
    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public Player withExperience(Integer experience) {
        this.experience = experience;
        return this;
    }

    @JsonProperty("skills")
    public Skills getSkills() {
        return skills;
    }

    @JsonProperty("skills")
    public void setSkills(Skills skills) {
        this.skills = skills;
    }

    public Player withSkills(Skills skills) {
        this.skills = skills;
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

    public Player withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    public Integer getGainedExperience() {

        return gainedExperience;
    }

    public void setGainedExperience(Integer gainedExperience) {
        this.gainedExperience = gainedExperience;
    }
}