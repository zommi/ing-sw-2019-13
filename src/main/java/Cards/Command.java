package Cards;

import java.util.*;

import Player.Character;


public interface Command {

    public ArrayList<ArrayList<Character>> execute(Map.NoSpawnSquare square);

}