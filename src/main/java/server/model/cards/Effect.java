package server.model.cards;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Effect {

    @JsonProperty
    private boolean mandatory;

    @JsonProperty
    private boolean conditional;

    @JsonProperty
    private int macroEffectIndex;

    @JsonProperty
    private int microEffectIndex;

    @JsonProperty
    private List<MicroEffect> microEffects;

    @JsonProperty
    private Cost cost;

    @JsonProperty
    private boolean atLeastOneActiveFlag;



}
