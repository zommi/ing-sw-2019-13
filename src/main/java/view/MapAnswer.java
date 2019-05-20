package view;

import java.util.ArrayList;
import java.util.List;

public class MapAnswer implements ServerAnswer {
    private int numMap;
    private List<MapElement> charactersPosition;

    public MapAnswer(int numMap, ArrayList<MapElement> charactersPosition){
        this.numMap = numMap;
        this.charactersPosition = charactersPosition;
    }

    public int getNumMap(){
        return this.numMap;
    }
    public List<MapElement> getCharactersPosition(){
        return this.charactersPosition;
    }
}
