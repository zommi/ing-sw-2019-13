package server.model.cards;

import server.model.map.SquareAbstract;
import server.model.player.Character;

import java.util.ArrayList;


public interface Command {

    public ArrayList<ArrayList<Character>> execute(SquareAbstract square);

}