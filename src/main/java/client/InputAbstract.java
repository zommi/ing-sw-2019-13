package client;

import client.weapons.MacroEffect;
import client.weapons.MicroEffect;
import client.weapons.Weapon;

import java.util.List;

public interface InputInterface {
    public boolean getChoice(MacroEffect macroEffect);
    public MacroEffect chooseOneMacro(Weapon weapon);

    public boolean getChoice(MicroEffect microEffect);

    List<SquareInfo> askSquares(int maxSquares);

    List<String> askPlayers(int maxTargetPlayerSize);

    List<String> askRooms(int maxTargetRoomSize);


}
