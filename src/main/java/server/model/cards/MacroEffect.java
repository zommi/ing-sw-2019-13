package server.model.cards;

import com.fasterxml.jackson.annotation.JsonProperty;
import exceptions.NoSuchEffectException;

import java.util.List;

public class MacroEffect {

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

    public MicroEffect getMicroEffect(int micro) throws NoSuchEffectException {
        if(micro < microEffects.size())
            return microEffects.get(micro);
        else throw new NoSuchEffectException();
    }

}
