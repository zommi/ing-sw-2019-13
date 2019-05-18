package server.model.cards;

import com.fasterxml.jackson.annotation.JsonProperty;
import exceptions.NoSuchEffectException;
import server.controller.playeraction.MicroInfo;
import server.controller.playeraction.ShootInfo;
import server.model.map.Room;
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

    public String getPolicyType() {
        return policyType;
    }

    public boolean isVerified(ShootInfo shootInfo, MicroInfo microInfo){
        switch(this.policyName){
            case "visible": return this.checkVisible(shootInfo, microInfo);
            case "different": return this.checkDifferent(shootInfo, microInfo);
            case "specificSquare": return this.checkSpecificSquare(shootInfo, microInfo);
            case "inList": return this.checkInList(shootInfo, microInfo);

            default: return false;
        }
    }

    //
    //public void apply()

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
                    } catch(NoSuchEffectException e){
                        //if an effect is not present, it's automatically verified
                    }

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
        if(policyType.equals("player") && effectFlag && effectList.equals("player")){
             try {
                 for (PlayerAbstract playerAbstract : microInfo.getPlayersList())
                     if(shootInfo.getActivatedMicro(macroEffectIndex, microEffectIndex)
                             .getPlayersList().contains(playerAbstract))
                             return false;
             } catch (NoSuchEffectException e){
                 return true;
             }
            return true;
        }
        else if(policyType.equals("square") && attackerFlag){
            return microInfo.getSquare() != shootInfo.getAttacker().getPosition();

        }
        else if(policyType.equals("room") && attackerFlag){
            for(Room room : microInfo.getRoomsList()){
                if(room == shootInfo.getAttacker().getPosition().getRoom())
                    return false;
            }
            return true;
        }
        else
            return false;
    }

    public boolean checkSpecificSquare(ShootInfo shootInfo, MicroInfo microInfo){
        return true;
    }

    public boolean checkInList(ShootInfo shootInfo, MicroInfo microInfo){
        if(policyType.equals("player") && effectFlag && effectList.equals("player")){
            try {
                for (PlayerAbstract playerAbstract : microInfo.getPlayersList()) {
                    if (!shootInfo.getActivatedMicro(macroEffectIndex, microEffectIndex)
                            .getPlayersList().contains(playerAbstract))
                        return false;
                }
            } catch(NoSuchEffectException e){
                return true;
            }
            return true;
        }
        else return false;
    }

}
