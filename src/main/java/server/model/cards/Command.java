package server.model.cards;

import server.model.map.SquareAbstract;
import server.model.player.GameCharacter;

import java.util.ArrayList;


public interface Command {

    public ArrayList<ArrayList<GameCharacter>> execute(SquareAbstract square);

}