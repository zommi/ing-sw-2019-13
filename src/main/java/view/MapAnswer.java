package view;

import server.model.map.GameMap;

import java.io.Serializable;


public class MapAnswer implements ServerAnswer, Serializable {

    private GameMap result;

    public MapAnswer(GameMap map){
        this.result = map;
    }

    public GameMap getResult(){
        return this.result;
    }


}
