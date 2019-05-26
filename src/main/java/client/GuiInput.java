package client;

import client.weapons.MacroEffect;
import client.weapons.MicroEffect;
import client.weapons.Weapon;

import java.util.List;

public class GuiInput extends InputAbstract {
    @Override
    public boolean getChoice(MacroEffect macroEffect) {
        return false;
    }

    @Override
    public MacroEffect chooseOneMacro(Weapon weapon) {
        return null;
    }

    @Override
    public boolean getChoice(MicroEffect microEffect) {
        return false;
    }

    @Override
    public List<SquareInfo> askSquares(int maxSquares) {
        return null;
    }

    @Override
    public List<String> askPlayers(int maxTargetPlayerSize) {
        return null;
    }

    @Override
    public List<String> askRooms(int maxTargetRoomSize) {
        return null;
    }
}
