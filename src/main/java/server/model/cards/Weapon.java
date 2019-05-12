package server.model.cards;

import com.fasterxml.jackson.annotation.JsonProperty;
import exceptions.NoSuchEffectException;

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
    private List<MacroEffect> macroEffects;

    public Cost getBuyCost() {
        return buyCost;
    }

    public String getName() {
        return name;
    }

    public Cost getReloadCost() {
        return reloadCost;
    }

    public List<MacroEffect> getMacroEffects() {
        return macroEffects;
    }

    public WeaponType getType() {
        return type;
    }

    public MacroEffect getMacroEffect(int macro) throws NoSuchEffectException {
        if(macro < macroEffects.size())
            return macroEffects.get(macro);
        else throw new NoSuchEffectException();
    }

    public MicroEffect getMicroEffect(int macro, int micro) throws NoSuchEffectException{
        return getMacroEffect(macro).getMicroEffect(micro);

    }
}