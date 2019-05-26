package client.weapons;

import com.fasterxml.jackson.annotation.JsonProperty;
import server.model.cards.WeaponPolicy;

import java.util.List;

public class MicroEffect {

    @JsonProperty
    private boolean mandatory;

    @JsonProperty
    private boolean limited;

    @JsonProperty
    private boolean conditional;

    @JsonProperty
    private int damage;

    @JsonProperty
    private int marks;

    @JsonProperty
    private int maxTargetPlayerSize;

    @JsonProperty
    private int maxNmSquareSize;

    @JsonProperty
    private int maxTargetRoomSize;

    @JsonProperty
    private int macroEffectIndex;

    @JsonProperty
    private int microEffectIndex;

    @JsonProperty
    private List<WeaponPolicy> policies;

    @JsonProperty
    private boolean generatePlayerFlag;

    @JsonProperty
    private boolean generateSquareFlag;

    @JsonProperty
    private boolean moveFlag;

    private String description;

    private int macroNumber;

    private int number;

    public int getNumber() {
        return number;
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

}

