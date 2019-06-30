package client;

import client.weapons.MacroEffect;
import client.weapons.MicroEffect;
import client.weapons.ScopePack;
import client.weapons.Weapon;
import server.model.cards.PowerUpCard;
import server.model.cards.WeaponCard;

import java.io.FileNotFoundException;
import java.util.List;

public abstract class InputAbstract {
    List<String> playersNames;
    List<String> roomsNames;

    GameModel gameModel;

    public abstract ReloadInfo askReload(WeaponCard weaponCard);

    public abstract boolean getChoice(MacroEffect macroEffect);

    public abstract MacroEffect chooseOneMacro(Weapon weapon);

    public abstract boolean getChoice(MicroEffect microEffect);

    public abstract List<SquareInfo> askSquares(int maxSquares);

    public abstract List<String> askPlayers(int maxTargetPlayerSize);

    public abstract List<String> askRooms(int maxTargetRoomSize);

    public List<String> getRoomsNames() {
        return roomsNames;
    }

    public void setRoomsNames(List<String> roomsNames) {
        this.roomsNames = roomsNames;
    }

    public void setPlayersNames(List<String> playersNames){
        this.playersNames = playersNames;
    }

    public List<String> getPlayersNames() {
        return playersNames;
    }

    public abstract boolean getMoveChoice();

    public void setGameModel(GameModel gameModel) {
        this.gameModel = gameModel;
    }

    public GameModel getGameModel() {
        return gameModel;
    }

    public abstract List<PowerUpCard> askPowerUps();

    public abstract List<ScopePack> askTargetingScopes();
}
