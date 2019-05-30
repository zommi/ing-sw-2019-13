package client;

import client.weapons.MacroEffect;
import client.weapons.MicroEffect;
import client.weapons.Weapon;

import java.util.List;

public class GuiInput extends InputAbstract {

    //mettere un riferimento al maingui controller

    //quando premo sulla carta devo passare la wapon a al shootparser e chiamare geat weapon input,
    //il get weapon input in base all'arma chiama metodi di input abstract. devo organizzare sta classe
    // per far uscire i prompt giusti.
    //dentro inputabstract ci sono delle liste di giocatori che sono quelli che devo far scegliere al giocatore.
    //si potrebbero mettere i nomi dentro a dei bottoni.

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
