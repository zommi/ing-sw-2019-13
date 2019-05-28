package view;

import server.model.map.GameMap;


public class MapAnswer implements ServerAnswer{

    private GameMap result;

    public MapAnswer(GameMap map){
        this.result = map.createCopy(map);
    }

    public GameMap getResult(){
        return this.result;
    }


}
