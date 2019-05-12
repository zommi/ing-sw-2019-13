package server.controller.playeraction;

import java.util.List;

public class MacroInfo {
    private int macroNumber;
    private List<MicroInfo> activatedMicros;

    public int getMacroNumber() {
        return macroNumber;
    }

    public List<MicroInfo> getActivatedMicros() {
        return activatedMicros;
    }

    public MicroInfo getActivatedMicro(int micro){
        return activatedMicros.get(micro);
    }
}
