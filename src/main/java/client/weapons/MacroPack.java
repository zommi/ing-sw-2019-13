package client.weapons;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The pack that is created inside the client and sent to the server.
 * It will be converted on the server during the validation of the action
 * @author Matteo Pacciani
 */
public class MacroPack implements Serializable {

    /**
     * The index of the macro effect that this pack refers to
     */
    private int macroNumber;

    /**
     * A list of the activated micro effect relative to this macro
     */
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

}
