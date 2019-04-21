package com.example.asusnb.tennistournament.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "surface",
        "type"
})
public class Tournament {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("surface")
    private String surface;
    @JsonProperty("type")
    private String type;

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }


    @JsonProperty("surface")
    public String getSurface() {
        return surface;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

}