package server.controller.playeraction;

import server.model.game.Game;
import server.model.player.GameCharacter;

import java.util.List;

public class TargetPack {
    private List<List<GameCharacter>> targets;

    public TargetPack(List<List<GameCharacter>> targets){
        this.targets = targets;
    }

    public List<GameCharacter> getZeroEffectTargets(){
        return targets.get(0);
    }


    public static boolean areDifferentTargets(List<GameCharacter> list){
        for(int i=0; i<list.size(); i++){
            for(int j=i+1; j<list.size(); j++){
                if(list.get(i).equals(list.get(j))){
                    return false;
                }
            }
        }
        return true;
    }
}
