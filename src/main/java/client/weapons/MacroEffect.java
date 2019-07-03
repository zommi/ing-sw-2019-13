package client.weapons;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * The effect that can be seen on every weapon card. It has at least one micro effect.
 * @author Matteo Pacciani
 *
 */
public class MacroEffect implements Serializable {

    /**
     * True if this macro effect must be activated
     */
    @JsonProperty
    private boolean mandatory;

    /**
     * True if this macro effect depends on the activation of another macro effect
     */
    @JsonProperty
    private boolean conditional;

    /**
     * True if this macro effect can be activated only once. The player may be asked activating it more than once
     */
    @JsonProperty
    private boolean limited;

    /**
     * The index of the macro effect of the micro effect this macro effect depends on
     */
    @JsonProperty
    private int macroEffectIndex;

    /**
     * The index of the micro effect this macro depends on
     */
    @JsonProperty
    private int microEffectIndex;

    /**
     * A list that contains all the possible micro effects
     */
    @JsonProperty
    private List<MicroEffect> microEffects;

    /**
     * The cost of this macro effect. Once this is paid, all the micro effects are free
     */
    @JsonProperty
    private Cost cost;

    /**
     * True if at least one micro effect has to be activated
     */
    @JsonProperty
    private boolean atLeastOneActiveFlag;

    /**
     * The index of this macro effect
     */
    private int number;

    /**
     * A description of the effect of this macro effect
     */
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

    /**
     *
     * @param micro the index of the micro effect
     * @return the micro effect in the microEffects list that has the specified index
     */
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

    public void setCost(Cost cost) {
        this.cost = cost;
    }

    public int getMacroEffectIndex() {
        return macroEffectIndex;
    }

    public String toString(){
        return this.description + "\ncost: " + cost;
    }
}
