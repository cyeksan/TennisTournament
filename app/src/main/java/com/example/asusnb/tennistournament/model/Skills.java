package com.example.asusnb.tennistournament.model;

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

    @JsonProperty("clay")
    public Integer getClay() {
        return clay;
    }

    @JsonProperty("grass")
    public Integer getGrass() {
        return grass;
    }

    @JsonProperty("hard")
    public Integer getHard() {
        return hard;
    }

}
