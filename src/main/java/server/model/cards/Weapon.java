package server.model.cards;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Weapon {

    @JsonProperty
    private String name;

    @JsonProperty
    private WeaponType type;

    @JsonProperty
    private Cost buyCost;

    @JsonProperty
    private Cost reloadCost;

    @JsonProperty
    private List<Effect> macroEffects;

    public Cost getBuyCost() {
        return buyCost;
    }

    public String getName() {
        return name;
    }

    public Cost getReloadCost() {
        return reloadCost;
    }

    public List<Effect> getMacroEffects() {
        return macroEffects;
    }

    public WeaponType getType() {
        return type;
    }
}