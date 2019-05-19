package server.model.cards;

import com.fasterxml.jackson.annotation.JsonProperty;
import exceptions.NoSuchEffectException;
import javafx.geometry.NodeOrientation;
import server.controller.playeraction.MicroInfo;
import server.controller.playeraction.ShootInfo;
import server.model.game.Game;
import server.model.gameboard.GameBoard;
import server.model.map.Room;
import server.model.map.SquareAbstract;
import server.model.player.GameCharacter;
import server.model.player.PlayerAbstract;

import java.util.ArrayList;
import java.util.List;

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
            case "distanceEqual":
            case "distanceGreater":
            case "distanceSmaller": return this.checkDistance(shootInfo, microInfo);

            default: return false;
        }
    }

    public void generate(ShootInfo shootInfo, MicroInfo microInfo){
        switch(this.policyName){
            case "specificPlayer": this.generateSpecificPlayer(shootInfo, microInfo);
                break;
            case "specificSquare": this.generateSpecificSquare(shootInfo, microInfo);
                break;
            case "allPlayersInRoom": this.generateAllPlayersInRoom(shootInfo, microInfo);
                break;
            case "distanceEqual" : this.generateDistanceEqual(shootInfo, microInfo);
                break;
            default: //this should never happen
        }
    }

    public void generateSpecificPlayer(ShootInfo shootInfo, MicroInfo microInfo){
        if(policyType.equals("player")){
            if(attackerFlag){
                microInfo.getPlayersList().add(shootInfo.getAttacker());
            }
            else if(effectFlag && effectList.equals("player")){
                try {
                    microInfo.getPlayersList().add(shootInfo
                            .getActivatedMicro(macroEffectIndex, microEffectIndex).getFirstPlayer());
                }catch(NoSuchEffectException e){
                    //this should never happen
                }
            }
        }
    }

    public void generateSpecificSquare(ShootInfo shootInfo, MicroInfo microInfo){
        try {
            if (policyType.equals("square")) {
                if (effectFlag) {
                    if (effectList.equals("square")) {
                        microInfo.setSquare(shootInfo.getActivatedMicro(macroEffectIndex, microEffectIndex).getSquare());
                    } else if (effectList.equals("player")) {
                        microInfo.setSquare(shootInfo.getActivatedMicro(macroEffectIndex, microEffectIndex)
                                .getFirstPlayer().getPosition());
                    }
                }
            }
        }catch(NoSuchEffectException e){
            //this should never happen
        }
    }

    public void generateAllPlayersInRoom(ShootInfo shootInfo, MicroInfo microInfo){
        try{
            if(policyType.equals("player") && effectFlag && effectList.equals("room"))
                for(GameCharacter  gameCharacter : shootInfo.getActivatedMicro(macroEffectIndex, microEffectIndex)
                        .getFirstRoom().getCharacters()){
                    microInfo.getPlayersList().add(gameCharacter.getConcretePlayer());
                }
        }catch(NoSuchEffectException e){
            //this should never happen
        }
    }

    public void generateDistanceEqual(ShootInfo shootInfo, MicroInfo microInfo){
        if(policyType.equals("player") && attackerFlag){

        }
        else if(policyType.equals("square") && attackerFlag)
    }

    public boolean checkDistance(ShootInfo shootInfo, MicroInfo microInfo){
        try{
            List<SquareAbstract> list = new ArrayList<>();
            switch(policyType){
                case "player":
                    for(PlayerAbstract playerAbstract : microInfo.getPlayersList()){
                        list.add(playerAbstract.getPosition());
                    }
                    break;
                case "square":
                    list.add(microInfo.getSquare());
                    break;
                case "noMoveSquare":
                    list.addAll(microInfo.getNoMoveSquaresList());
                    break;
                default: return false;
            }
            SquareAbstract square2 = null;
            if(attackerFlag){
                square2 = shootInfo.getAttacker().getPosition();
            }
            else if(effectFlag){
                if(effectList.equals("player"))
                    square2 = shootInfo.getActivatedMicro(macroEffectIndex, microEffectIndex)
                            .getFirstPlayer().getPosition();
                else if(effectList.equals("square")){
                    square2 = microInfo.getSquare();
                }
                else if(effectList.equals("noMoveSquare")){
                    square2 = microInfo.getFirstNMS();
                }
                else return false;
            }
            return distanceCheckSwitch(list, square2);
        }catch(NoSuchEffectException e){
            return true;
        }
    }

    public boolean distanceCheckSwitch(List<SquareAbstract> list, SquareAbstract square){
        switch (policyName){
            case "distanceEqual":
                for(SquareAbstract square1 : list){
                    if(square1.distance(square) != distance)
                        return false;
                }
                return true;
            case "distanceGreater":
                for(SquareAbstract square1 : list){
                    if(square1.distance(square) <= distance)
                        return false;
                }
                return true;
            case "distanceSmaller":
                for(SquareAbstract square1 : list){
                    if(square1.distance(square) >= distance)
                        return false;
                }
                return true;
            default: return false;
        }
    }

    public boolean checkDistanceSmaller(ShootInfo shootInfo, MicroInfo microInfo){
        return true;
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
