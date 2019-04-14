package Cards;

import Map.SquareAbstract;
import Player.Character;

import java.util.ArrayList;


public interface Command {

    public ArrayList<ArrayList<Character>> execute(SquareAbstract square);

}