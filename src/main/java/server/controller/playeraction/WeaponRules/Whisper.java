package server.controller.playeraction.WeaponRules;

import server.controller.playeraction.ShootInfo;
import server.model.player.GameCharacter;

import java.util.List;

public class Whisper implements WeaponRulesInterface{

    private int effectsnumber;
    private int extra0;
    private List<GameCharacter> playerPositionVisibleCharacters;
    private List<GameCharacter> playerPositionAtLeastTwoMoves;
    private List<GameCharacter> target0;

    public boolean validate(ShootInfo pack){

        if((effectsnumber == 0)||(effectsnumber > 1))
            return false;

        if(extra0 != 0)
            return false;

        if(!playerPositionVisibleCharacters.contains(target0.get(0))||
                !playerPositionAtLeastTwoMoves.contains(target0.get(0)))
            return false;

        return true;
    }

    public void actuate(ShootInfo pack){

    }

    @Override
    public void unpack(ShootInfo pack) {

        this.effectsnumber = pack.getExtra().size();
        this.extra0 = pack.getExtra().get(0);
        this.playerPositionVisibleCharacters = pack.getPlayer().getPosition().getVisibleCharacters();
        this.playerPositionAtLeastTwoMoves = pack.getPlayer().getPosition().getAtLeastTwoMovementCharacters();
        this.target0 = pack.getTargets().get(0);
    }
}
