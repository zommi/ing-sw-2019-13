package server.model.cards;

import com.fasterxml.jackson.annotation.JsonProperty;
import exceptions.NoSuchEffectException;
import server.controller.playeraction.MicroInfo;
import server.controller.playeraction.ShootInfo;
import server.model.player.PlayerAbstract;

public class WeaponPolicy {

    @JsonProperty
    private String policyName;

    @JsonProperty
    private String policyType;

    @JsonProperty
    private boolean attackerFlag;

    @JsonProperty
    private boolean effectFlag;

    @JsonProperty
    private String effectList;

    @JsonProperty
    private int macroEffectIndex;

    @JsonProperty
    private int microEffectIndex;

    @JsonProperty
    private int distance;

    public boolean isVerified(ShootInfo shootInfo, MicroInfo microInfo){
        switch(this.policyName){
            case "visible": return this.checkVisible(shootInfo, microInfo);
            case "different": return this.checkDifferent(shootInfo, microInfo);
            default: return false;
        }
    }

    public boolean checkVisible(ShootInfo shootInfo, MicroInfo microInfo){
        if(policyType.equals("player")){
            for(PlayerAbstract player : microInfo.getPlayersList()){
                if(attackerFlag && !shootInfo.getAttacker().getPosition()
                        .getVisibleCharacters().contains(player.getGameCharacter()))
                    return false;
                else if(effectFlag){
                    try{
                        if(effectList.equals("player") && !shootInfo.getActivatedMicro(macroEffectIndex, microEffectIndex)
                            .getPlayersList().get(0).getPosition().getVisibleCharacters().contains(player.getGameCharacter()))
                            return false;
                        else if(effectList.equals("square") && !shootInfo.getActivatedMicro(macroEffectIndex,microEffectIndex)
                            .getSquare().getVisibleCharacters().contains(player.getGameCharacter())){
                            return false;
                        }
                        else if(!effectList.equals("player") && !effectList.equals("square"))
                            return false;
                    } catch(NoSuchEffectException e){};

                }
            }
            return true;
        }
        else if(policyType.equals("square")){
            return attackerFlag && shootInfo.getAttacker().getPosition().getVisibleSquares().contains(microInfo.getSquare());
        }
        else return false;
    }

    public boolean checkDifferent(ShootInfo shootInfo, MicroInfo microInfo){
        return true;
    }

}
