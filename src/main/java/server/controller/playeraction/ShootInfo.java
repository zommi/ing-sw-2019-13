package server.controller.playeraction;

import client.Info;
import client.ReloadInfo;
import client.weapons.Cost;
import client.weapons.MicroEffect;
import client.weapons.Weapon;
import server.model.cards.PowerUpCard;
import server.model.map.SquareAbstract;
import server.model.player.PlayerAbstract;


import java.io.Serializable;
import java.util.List;

/**
 * A {@link client.weapons.ShootPack} that has been translated by the server. During the validation process, the cost is added and it keeps
 * track of the cost of all the activated macro effects, of all the targeting scopes with the ammo cube chosen by the attacker, and the cost of
 * the reload info that is sent along with this shoot info if the current state of the match is final frenzy and the attacker
 * chooses to reload before shooting
 * @author Matteo Pacciani
 */

public class ShootInfo implements Serializable, Info {

    private PlayerAbstract attacker;
    private Weapon weapon;
    private List<MacroInfo> activatedMacros;
    private SquareAbstract square;
    private List<PowerUpCard> powerUpCards;

    /**
     * The total cost of the shoot action, including macro effects, targeting scopes and the eventual reload info.
     * It is calculated during the validation process and it does not take into account
     * the powerup cards used for paying.
     */
    private Cost totalCost;
    private List<ScopeInfo> scopeInfos;
    private ReloadInfo reloadInfo;


    public ShootInfo(PlayerAbstract attacker, Weapon weapon, List<MacroInfo> activatedMacros, SquareAbstract squareAbstract,
                     List<PowerUpCard> powerUpCards, List<ScopeInfo> scopeInfos, ReloadInfo reloadInfo){
        this.attacker = attacker;
        this.weapon = weapon;
        this.activatedMacros = activatedMacros;
        this.square = squareAbstract;
        this.powerUpCards = powerUpCards;
        this.scopeInfos = scopeInfos;
        this.reloadInfo = reloadInfo;
    }

    public ReloadInfo getReloadInfo() {
        return reloadInfo;
    }

    public List<ScopeInfo> getScopeInfos() {
        return scopeInfos;
    }

    public Cost getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Cost totalCost) {
        this.totalCost = totalCost;
    }

    public SquareAbstract getSquare() {
        return square;
    }

    public PlayerAbstract getAttacker() {
        return attacker;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public List<PowerUpCard> getPowerUpCards() {
        return powerUpCards;
    }

    public List<MacroInfo> getActivatedMacros() {
        return activatedMacros;
    }

    /**
     * Return the macro info corresponding to the macro effect with the specified index, if included in the shoot info
     * @param macro the index of the macro
     * @return the macro info with the specified index
     */
    public MacroInfo getActivatedMacro(int macro){
        for(MacroInfo macroInfo : activatedMacros)
            if(macroInfo.getMacroNumber() == macro)
                return macroInfo;
        return null;
    }

    /**
     * Returns the micro info corresponding to the micro effect with the specified macro and micro index, if it has been
     * activated and thus included in the shoot info
     * @param macro the index of the macro
     * @param micro the index of the micro
     * @return turns the micro info corresponding to the micro effect with the specified macro and micro index, if it has been
     *      * activated and thus included in the shoot info
     */
    public MicroInfo getActivatedMicro(int macro, int micro){
        if(getActivatedMacro(macro) != null)
            return getActivatedMacro(macro).getActivatedMicro(micro);
        else
            return null;
    }

    /**
     * Checks the dimensions of the objects included in the micro info.
     * If the targets are going to be generated automatically, those fields must be empty.
     * If that particular micro effect does not involve choosing a certain field, that field must be null.
     * All the players and rooms list must have a legal size, specified in the micro effect.
     * @param microInfo the micro info whose dimensions are going to be checked
     * @param microEffect the corresponding micro effect
     * @return true if all is ok and the validation process can go on, false otherwise
     */
    public boolean areDimensionsOk(MicroInfo microInfo, MicroEffect microEffect){
        //checks if playerList is ok
        if(microEffect.isGeneratePlayerFlag()) {
            if (!microInfo.getPlayersList().isEmpty())
                return false;
        }
        else{
            if(microEffect.getMaxTargetPlayerSize() == 0) {
                if (!microInfo.getPlayersList().isEmpty())
                    return false;
            }
            else{
                if(microInfo.getPlayersList().isEmpty() ||
                        microInfo.getPlayersList().size() > microEffect.getMaxTargetPlayerSize())
                    return false;
            }

        }

        //checks if square is ok
        if(((!microEffect.isMoveFlag() || microEffect.isGenerateSquareFlag()) && microInfo.getSquare() != null) ||
                (microEffect.isMoveFlag() && !microEffect.isGenerateSquareFlag() && microInfo.getSquare() == null))
            return false;

        //check if noMoveSquares are ok
        if(microEffect.getMaxNmSquareSize() == 0) {
            if (!microInfo.getNoMoveSquaresList().isEmpty())
                return false;
        }
        else{
            //se bisognava inserire square
                //
            if(microInfo.getNoMoveSquaresList().isEmpty() ||
                    microInfo.getNoMoveSquaresList().size() > microEffect.getMaxNmSquareSize())
                return false;
        }

        //check if rooms are ok
        if(microEffect.getMaxTargetRoomSize() == 0) {
            if (!microInfo.getRoomsList().isEmpty())
                return false;
        }
        else{
            if(microInfo.getRoomsList().isEmpty() ||
                    microInfo.getRoomsList().size() > microEffect.getMaxTargetRoomSize())
                return false;
        }

        return true;
    }
}
