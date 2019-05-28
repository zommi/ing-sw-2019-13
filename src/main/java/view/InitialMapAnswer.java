package view;


import server.model.map.GameMap;

public class InitialMapAnswer implements ServerAnswer {
    private GameMap initialMap;

    public InitialMapAnswer(GameMap map){
        this.initialMap = map;
    }

    public GameMap getInitialMap(){
        return this.initialMap;
    }
}
