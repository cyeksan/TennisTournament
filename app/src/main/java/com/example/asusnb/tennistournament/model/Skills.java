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
        "clay",
        "grass",
        "hard"
})
public class Skills {

    @JsonProperty("clay")
    private Integer clay;
    @JsonProperty("grass")
    private Integer grass;
    @JsonProperty("hard")
    private Integer hard;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("clay")
    public Integer getClay() {
        return clay;
    }

    @JsonProperty("clay")
    public void setClay(Integer clay) {
        this.clay = clay;
    }

    public Skills withClay(Integer clay) {
        this.clay = clay;
        return this;
    }

    @JsonProperty("grass")
    public Integer getGrass() {
        return grass;
    }

    @JsonProperty("grass")
    public void setGrass(Integer grass) {
        this.grass = grass;
    }

    public Skills withGrass(Integer grass) {
        this.grass = grass;
        return this;
    }

    @JsonProperty("hard")
    public Integer getHard() {
        return hard;
    }

    @JsonProperty("hard")
    public void setHard(Integer hard) {
        this.hard = hard;
    }

    public Skills withHard(Integer hard) {
        this.hard = hard;
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

    public Skills withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }


}
