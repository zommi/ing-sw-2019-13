package Model.Cards;

import Model.Map.SquareAbstract;
import Model.Player.Character;

import java.util.ArrayList;


public interface Command {

    public ArrayList<ArrayList<Character>> execute(SquareAbstract square);

}