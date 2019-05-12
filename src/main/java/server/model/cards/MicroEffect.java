package server.model.cards;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class MicroEffect {

    @JsonProperty
    private boolean mandatory;

    @JsonProperty
    private boolean limited;

    @JsonProperty
    private int damage;

    @JsonProperty
    private int marks;

    @JsonProperty
    private int maxTargetPlayerSize;

    @JsonProperty
    private List<WeaponPolicy> policies;

    @JsonProperty
    private boolean generatePlayerFlag;

    @JsonProperty
    private boolean moveFlag;

}
