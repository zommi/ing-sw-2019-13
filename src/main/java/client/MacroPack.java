package client;

import exceptions.NoSuchEffectException;

import java.util.ArrayList;
import java.util.List;

public class MacroPack {
    private int macroNumber;
    private List<MicroPack> activatedMicros;

    public MacroPack(int macroNumber){
        this.macroNumber = macroNumber;
        this.activatedMicros = new ArrayList<>();
    }

    public int getMacroNumber() {
        return macroNumber;
    }

    public List<MicroPack> getActivatedMicros() {
        return activatedMicros;
    }

    public MicroPack getActivatedMicro(int micro) throws NoSuchEffectException {
        for(MicroPack microPack : activatedMicros){
            if(microPack.getMicroNumber() == micro)
                return microPack;
        }
        throw new NoSuchEffectException();
    }

}