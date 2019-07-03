package server.controller.playeraction;

import java.io.Serializable;
import java.util.List;

/**
 * A {@link client.weapons.MacroPack} that has been converted into references inside the server
 * @author Matteo Pacciani
 */
public class MacroInfo implements Serializable {
    /**
     * The index of the macro effect
     */
    private int macroNumber;

    /**
     * A list of all the activated micro effect for this macro effect
     */
    private List<MicroInfo> activatedMicros;

    public MacroInfo(int macroNumber, List<MicroInfo> activatedMicros){
        this.macroNumber = macroNumber;
        this.activatedMicros = activatedMicros;
    }

    public int getMacroNumber() {
        return macroNumber;
    }

    public List<MicroInfo> getActivatedMicros() {
        return activatedMicros;
    }

    /**
     * @param micro the index of the MicroInfo
     * @return the micro info associated to the micro effect with the specified index
     */
    public MicroInfo getActivatedMicro(int micro){
        for(MicroInfo microInfo : activatedMicros){
            if(microInfo.getMicroNumber() == micro)
                return microInfo;
        }
        return null;
    }
}
