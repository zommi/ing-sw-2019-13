package client;

import com.fasterxml.jackson.annotation.JsonProperty;
import exceptions.NoSuchEffectException;

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

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public MicroEffect getMicroEffect(int micro) throws NoSuchEffectException {
        if(micro < microEffects.size())
            return microEffects.get(micro);
        else throw new NoSuchEffectException();
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
}
