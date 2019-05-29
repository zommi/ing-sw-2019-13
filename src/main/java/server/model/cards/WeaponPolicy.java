package server.model.cards;

import com.fasterxml.jackson.annotation.JsonProperty;
import server.controller.playeraction.MicroInfo;
import server.controller.playeraction.ShootInfo;
import server.model.map.Room;
import server.model.map.SquareAbstract;
import server.model.player.PlayerAbstract;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WeaponPolicy implements Serializable {

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
            case "invisible": return this.checkInvisible(shootInfo, microInfo);
            case "different": return this.checkDifferent(shootInfo, microInfo);
            case "inList": return this.checkInList(shootInfo, microInfo);
            case "distanceEqual":
            case "distanceGreater":
            case "distanceSmaller": return this.checkDistance(shootInfo, microInfo);
            case "differentSquares": return this.checkDifferentSquares(microInfo);
            case "sameDirection" : return this.checkSameDirection(shootInfo, microInfo);
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
            case "distanceGreater":
            case "distanceSmaller":
            case "distanceEqual" : this.generateDistance(shootInfo, microInfo);
                break;
            default: //this should never happen
        }
    }

    private void generateSpecificPlayer(ShootInfo shootInfo, MicroInfo microInfo){

        if(policyType.equals("player")){
            if(attackerFlag)
                microInfo.getPlayersList().removeIf(x -> (x != shootInfo.getAttacker()));
            else if(effectFlag && effectList.equals("player"))
                microInfo.getPlayersList().removeIf(x -> (x != shootInfo
                        .getActivatedMicro(macroEffectIndex, microEffectIndex).getFirstPlayer()));
        }
    }

    private void generateSpecificSquare(ShootInfo shootInfo, MicroInfo microInfo){
        if (policyType.equals("square")) {
            if (effectFlag) {
                if (effectList.equals("square")) {
                    microInfo.setSquare(shootInfo.getActivatedMicro(macroEffectIndex, microEffectIndex).getSquare());
                } else if (effectList.equals("player")) {
                    microInfo.setSquare(shootInfo.getActivatedMicro(macroEffectIndex, microEffectIndex)
                            .getFirstPlayer().getPosition());
                }
                //not checking if micro is null cause it must have been activated
            }
        }
    }

    private void generateAllPlayersInRoom(ShootInfo shootInfo, MicroInfo microInfo){
        if(policyType.equals("player") && effectFlag && effectList.equals("room"))
            microInfo.getPlayersList().removeIf(x -> (!shootInfo.getActivatedMicro(macroEffectIndex, microEffectIndex)
                    .getFirstRoom().getCharacters().contains(x.getGameCharacter())));
        //not checking if micro is null cause it must have been activated
    }

    private void generateDistance(ShootInfo shootInfo, MicroInfo microInfo){
        if(policyType.equals("player")){  //removed && attackerFlag
            List<PlayerAbstract> backupList = new ArrayList<>(microInfo.getPlayersList());

            for (Iterator<PlayerAbstract> iterator = backupList.iterator(); iterator.hasNext();) {
                PlayerAbstract playerAbstract = iterator.next();
                microInfo.getPlayersList().clear();
                microInfo.getPlayersList().add(playerAbstract);
                if(!this.checkDistance(shootInfo, microInfo) || playerAbstract == shootInfo.getAttacker())
                    iterator.remove();

            }
            microInfo.getPlayersList().clear();
            microInfo.getPlayersList().addAll(backupList);
        }
        else if(policyType.equals("square") && attackerFlag && distance==0){
            microInfo.setSquare(shootInfo.getAttacker().getPosition());
        }
        //TODO add player no attackerFlag branch
    }

    private boolean checkDistance(ShootInfo shootInfo, MicroInfo microInfo){
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
            if(shootInfo.getActivatedMicro(macroEffectIndex, microEffectIndex) == null)
                return true;
            else {
                if (effectList.equals("player"))
                    square2 = shootInfo.getActivatedMicro(macroEffectIndex, microEffectIndex)
                            .getFirstPlayer().getPosition();
                else if (effectList.equals("square")) {
                    square2 = shootInfo.getActivatedMicro(macroEffectIndex, microEffectIndex).getSquare();
                } else if (effectList.equals("noMoveSquare")) {
                    square2 = shootInfo.getActivatedMicro(macroEffectIndex, microEffectIndex).getFirstNMS();
                } else return false;
            }
        }
        return distanceCheckSwitch(list, square2);
    }

    private boolean distanceCheckSwitch(List<SquareAbstract> list, SquareAbstract square){
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

    private boolean checkDifferentSquares(MicroInfo microInfo){
        for(PlayerAbstract playerAbstract : microInfo.getPlayersList()){
            for(PlayerAbstract playerAbstract1 : microInfo.getPlayersList()){
                if(playerAbstract!=playerAbstract1 && playerAbstract.getPosition() == playerAbstract1.getPosition())
                    return false;
            }
        }
        return true;
    }

    private boolean checkVisible(ShootInfo shootInfo, MicroInfo microInfo){
        if(policyType.equals("player")){
            for(PlayerAbstract player : microInfo.getPlayersList()){
                if(attackerFlag && !shootInfo.getAttacker().getPosition()
                        .getVisibleCharacters().contains(player.getGameCharacter()))
                    return false;
                else if(effectFlag){
                    if(shootInfo.getActivatedMicro(macroEffectIndex, microEffectIndex) == null)
                        return true;
                    else{
                        if(effectList.equals("player") && !shootInfo.getActivatedMicro(macroEffectIndex, microEffectIndex)
                            .getPlayersList().get(0).getPosition().getVisibleCharacters().contains(player.getGameCharacter()))
                            return false;
                        else if(effectList.equals("square") && !shootInfo.getActivatedMicro(macroEffectIndex,microEffectIndex)
                            .getSquare().getVisibleCharacters().contains(player.getGameCharacter())){
                            return false;
                        }
                        else if(!effectList.equals("player") && !effectList.equals("square"))
                            return false;
                    }
                }
            }
            return true;
        }
        else if(policyType.equals("square")){
            return attackerFlag && shootInfo.getAttacker().getPosition().getVisibleSquares().contains(microInfo.getSquare());
        }
        else if(policyType.equals("room") && attackerFlag){
            for(Room room : microInfo.getRoomsList()){
                if(!shootInfo.getAttacker().getPosition().getVisibleRooms().contains(room))
                    return false;
            }
            return true;
        }
        else if(policyType.equals("noMoveSquare") && attackerFlag){
            for(SquareAbstract squareAbstract : microInfo.getNoMoveSquaresList()){
                if(!shootInfo.getAttacker().getPosition().getVisibleSquares().contains(squareAbstract))
                    return false;
            }
            return true;
        }
        else return false;
    }

    private boolean checkInvisible(ShootInfo shootInfo, MicroInfo microInfo){
        if(policyType.equals("player") && attackerFlag){
            for(PlayerAbstract player : microInfo.getPlayersList()){
                if(shootInfo.getAttacker().getPosition()
                        .getVisibleCharacters().contains(player.getGameCharacter()))
                    return false;
            }
            return true;
        }
        else return false;
    }

    private boolean checkDifferent(ShootInfo shootInfo, MicroInfo microInfo){
        if(policyType.equals("player") && effectFlag && effectList.equals("player")){
             if(shootInfo.getActivatedMicro(macroEffectIndex, microEffectIndex) == null)
                return true;
             else{
                 for (PlayerAbstract playerAbstract : microInfo.getPlayersList())
                     if(shootInfo.getActivatedMicro(macroEffectIndex, microEffectIndex)
                             .getPlayersList().contains(playerAbstract))
                             return false;
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

    private boolean checkInList(ShootInfo shootInfo, MicroInfo microInfo){
        if(policyType.equals("player") && effectFlag && effectList.equals("player")){
            if(shootInfo.getActivatedMicro(macroEffectIndex, microEffectIndex) == null)
                return true;
            else{
                for (PlayerAbstract playerAbstract : microInfo.getPlayersList()) {
                    if (!shootInfo.getActivatedMicro(macroEffectIndex, microEffectIndex)
                            .getPlayersList().contains(playerAbstract))
                        return false;
                }
            }
            return true;
        }
        else return false;
    }

    private boolean checkSameDirection(ShootInfo shootInfo, MicroInfo microInfo){
        SquareAbstract squareAbstract;
        List<SquareAbstract> squareAbstractList = new ArrayList<>();
        if(attackerFlag){
            squareAbstract = shootInfo.getAttacker().getPosition();
            switch(policyType){
                case "player":
                    for(PlayerAbstract playerAbstract : microInfo.getPlayersList()){
                        squareAbstractList.add(playerAbstract.getPosition());
                    }
                    break;
                case "noMoveSquare":
                    squareAbstractList.addAll(microInfo.getNoMoveSquaresList());
                    break;
                case "square":
                    squareAbstractList.add(microInfo.getSquare());
                    break;
                default: return false;
            }
            if (effectFlag && shootInfo.getActivatedMicro(macroEffectIndex, microEffectIndex) != null) {
                switch (effectList) {
                    case "player":
                        for (PlayerAbstract playerAbstract : shootInfo
                                .getActivatedMicro(macroEffectIndex, microEffectIndex).getPlayersList()) {
                            squareAbstractList.add(playerAbstract.getPosition());
                        }
                        break;
                    case "noMoveSquare":
                        squareAbstractList.addAll(shootInfo
                                .getActivatedMicro(macroEffectIndex, microEffectIndex).getNoMoveSquaresList());
                        break;
                    case "square":
                        squareAbstractList.add(shootInfo
                                .getActivatedMicro(macroEffectIndex, microEffectIndex).getSquare());
                        break;
                    default:
                        return false;
                }
                //else no square is added
            }
            return squareAbstract.areSameDirection(squareAbstractList);
        }
        else return false;
    }

}
