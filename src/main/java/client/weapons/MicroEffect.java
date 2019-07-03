package client.weapons;

import com.fasterxml.jackson.annotation.JsonProperty;
import server.model.cards.WeaponPolicy;

import java.io.Serializable;
import java.util.List;

/**
 * The parts in which a macro effect could be divided.
 * @author Matteo Pacciani
 */
public class MicroEffect implements Serializable {

    /**
     * True if the attacker must activate this micro effect
     */
    @JsonProperty
    private boolean mandatory;

    /**
     * True if it can only be activated once
     */
    @JsonProperty
    private boolean limited;

    /**
     * True if its activation depends on the activation of another micro effect
     */
    @JsonProperty
    private boolean conditional;

    /**
     * The damage that all the players included in this micro effect will receive
     */
    @JsonProperty
    private int damage;

    /**
     * The marks that all the players included in this micro effect will receive
     */
    @JsonProperty
    private int marks;

    /**
     * Max number of player that can be included. If one, it must be chosen
     */
    @JsonProperty
    private int maxTargetPlayerSize;

    /**
     * Max number of squares that have to be selected. They are used for example when choosing a square and
     * giving damage to all the players in that square. They differs from the move squares because they are not
     * related to player move
     */
    @JsonProperty
    private int maxNmSquareSize;

    /**
     * Max number of rooms that can be chosen. If one, it must be chosen
     */
    @JsonProperty
    private int maxTargetRoomSize;

    /**
     * The index of the macro of the micro this micro activation depends on
     */
    @JsonProperty
    private int macroEffectIndex;

    /**
     * The index of the micro this micro activation depends on
     */
    @JsonProperty
    private int microEffectIndex;

    /**
     * A list of all the policies that have to be verified
     */
    @JsonProperty
    private List<WeaponPolicy> policies;

    /**
     * True if the players related to this micro effect cannot be chosen by the player, and instead they are automatically
     * generated wrt the policies
     */
    @JsonProperty
    private boolean generatePlayerFlag;

    /**
     * True if the move square related to this micro effect cannot be chosen by the player, and instead it is automatically
     *      generated wrt the policies
     */
    @JsonProperty
    private boolean generateSquareFlag;

    /**
     * True if the no move squares cannot be chosen by the player
     */
    @JsonProperty
    private boolean generateNMSquareFlag;

    /**
     * True if this micro effect requires the player to choose a square in which the targets will be moved
     */
    @JsonProperty
    private boolean moveFlag;

    /**
     * A description of the micro effect
     */
    private String description;

    /**
     * The index of the macro this micro if part of
     */
    private int macroNumber;

    /**
     * The index of this micro inside the containing macro
     */
    private int number;

    public int getNumber() {
        return number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMacroNumber() {
        return macroNumber;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setMacroNumber(int macroNumber) {
        this.macroNumber = macroNumber;
    }

    public boolean isConditional() {
        return conditional;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public boolean isLimited() {
        return limited;
    }

    public int getMacroEffectIndex() {
        return macroEffectIndex;
    }

    public int getMicroEffectIndex() {
        return microEffectIndex;
    }

    public boolean isGeneratePlayerFlag() {
        return generatePlayerFlag;
    }

    public boolean isGenerateSquareFlag() {
        return generateSquareFlag;
    }

    public boolean isMoveFlag() {
        return moveFlag;
    }

    public int getMaxTargetPlayerSize() {
        return maxTargetPlayerSize;
    }

    public int getMaxNmSquareSize() {
        return maxNmSquareSize;
    }

    public int getMaxTargetRoomSize() {
        return maxTargetRoomSize;
    }

    public List<WeaponPolicy> getPolicies() {
        return policies;
    }

    public int getDamage() {
        return damage;
    }

    public int getMarks() {
        return marks;
    }

    public String toString(){
        return this.description;
    }

    public boolean isGenerateNMSquareFlag() {
        return generateNMSquareFlag;
    }
}

