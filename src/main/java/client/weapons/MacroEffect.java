package client.weapons;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class MacroEffect {

    @JsonProperty
    private boolean mandatory;

    @JsonProperty
    private boolean conditional;

    @JsonProperty
    private boolean limited;

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

    private int number;

    private String description;

    public String getDescription() {
        return description;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public MicroEffect getMicroEffect(int micro){
        if(micro < microEffects.size())
            return microEffects.get(micro);
        else
            return null;
    }

    public List<MicroEffect> getMicroEffects() {
        return microEffects;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public boolean isLimited() {
        return limited;
    }

    public boolean isConditional() {
        return conditional;
    }

    public boolean isAtLeastOneActiveFlag() {
        return atLeastOneActiveFlag;
    }

    public Cost getCost() {
        return cost;
    }

    public int getMacroEffectIndex() {
        return macroEffectIndex;
    }

    public String toString(){
        return this.description + "\ncost: " + cost;
    }
}
