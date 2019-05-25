package client;

import java.util.List;

public class MicroPack {
    private int macroNumber;
    private int microNumber;
    private List<String> playersList;
    private SquareInfo square;
    private List<String> roomsList;
    private List<SquareInfo> noMoveSquaresList;

    public int getMicroNumber() {
        return microNumber;
    }

    public int getMacroNumber() {
        return macroNumber;
    }
}
