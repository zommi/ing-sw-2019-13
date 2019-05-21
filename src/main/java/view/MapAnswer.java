package view;

import server.model.map.GameMap;

public class MapAnswer {

    private MapElement result;

    public MapAnswer(GameMap map){
        this.result = CreateCopy(map);
    }

    public MapElement getResult(){
        return this.result;
    }

    public static MapElement CreateCopy(GameMap gameMap){
        MapElement el = new MapElement();


        return el;
    }
}
