package client;

import client.weapons.MacroEffect;
import client.weapons.MicroEffect;
import client.weapons.Weapon;

import java.util.List;

public abstract class InputAbstract {
    List<String> playersNames;

    public void setPlayersNames(List<String> playersNames){
        this.playersNames = playersNames;
    }

    public List<String> getPlayersNames() {
        return playersNames;
    }

    public abstract boolean getChoice(MacroEffect macroEffect);
    public abstract MacroEffect chooseOneMacro(Weapon weapon);

    public abstract boolean getChoice(MicroEffect microEffect);

    public abstract List<SquareInfo> askSquares(int maxSquares);

    public abstract List<String> askPlayers(int maxTargetPlayerSize);

    public abstract List<String> askRooms(int maxTargetRoomSize);


}
