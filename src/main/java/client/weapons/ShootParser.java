package client.weapons;

import client.GameModel;
import client.InputAbstract;
import server.model.game.GameState;
import server.model.cards.WeaponCard;
import server.model.player.PlayerState;

import java.util.Collections;

/**
 * It is used to collect all the info related to the shoot action: players, rooms, squares, and possibly
 * powerups to pay with and targeting scopes to deal additional damage.
 * @author Matteo Pacciani
 */

public class ShootParser {

    /**
     * True if a limited macro or micro has already been activated. The other limited things won't be asked
     */
    private boolean isLimitedActivated;

    /**
     * It could be {@link client.CliInput} or {@link client.GuiInput}, depending on the the type of client the player runs
      */
    private InputAbstract input;
    /**
     * The model of the game that is sent to the client
     */
    private GameModel gameModel;

    /**
     * The {@link ShootPack} that is going to be generated
     */
    private ShootPack shootPack;


    public ShootParser(GameModel gameModel, InputAbstract input){
        this.gameModel = gameModel;
        this.input = input;
    }

    /**
     * Analyzes the structure of a weapon and ask things that could be activated, including macros and micros
     * @param weaponCard the weapon card the player wants to shoot with
     * @return a shoot pack that will be sent to the server and validated
     */
    public ShootPack getWeaponInput(WeaponCard weaponCard){
        this.isLimitedActivated = false;
        Weapon weapon = weaponCard.getWeapon();
        shootPack = new ShootPack(weapon.getName());

        if(gameModel.getGameBoard() != null && (gameModel.getMyPlayer().getPlayerState() == PlayerState.ADRENALINIC_SHOOT
                                                || gameModel.getGameBoard().getResult().getGameState() == GameState.FINAL_FRENZY)){
            if(input.getMoveChoice()) {
                shootPack.setSquare(input.askSquares(1).get(0));
            }
        }

        if(gameModel.getCurrentState() == GameState.FINAL_FRENZY && !weaponCard.isReady())
            shootPack.setReloadInfo(input.askReload(weaponCard));

        switch(weapon.getType()){
            case EXTRA:
                for(MacroEffect macroEffect : weapon.getMacroEffects()){
                    if(macroEffect.isMandatory())
                        this.manageMacro(macroEffect);
                    else if((macroEffect.isConditional() && !this.isMacroActivated(macroEffect)) ||
                            (macroEffect.isLimited() && !this.isLimitedActivated) ||
                            (!macroEffect.isLimited() && !macroEffect.isConditional()))
                        this.chooseMacro(macroEffect);

                }
                break;
            case ALTERNATIVE:
                MacroEffect macroEffect = input.chooseOneMacro(weapon);
                manageMacro(macroEffect);
                break;
            case ONEMODE:
                manageMacro(weapon.getMacroEffect(0));
                break;
            default:
                //this should never happen (it depends on json file)
        }

        //ask for targeting scopes
        shootPack.setScopePacks(input.askTargetingScopes());

        //asking for powerup cards
        if(gameModel != null && gameModel.getPlayerHand() != null && !gameModel.getPlayerHand().getPowerupHand().isEmpty())
            shootPack.setPowerUpCards(input.askPowerUps());
        else
            shootPack.setPowerUpCards(Collections.emptyList());

        return shootPack;
    }

    /**
     * Manages the macro effect that has been activated. It analyzes the structure of the macro effect and
     * asks for the activation of the micro effects
     * @param macroEffect the activated macro effect
     */
    private void manageMacro(MacroEffect macroEffect){
        MacroPack macroPack = new MacroPack(macroEffect.getNumber());
        shootPack.getActivatedMacros().add(macroPack);
        if(macroEffect.isLimited())
            isLimitedActivated = true;

        System.out.println(macroEffect.getDescription());

        for(MicroEffect microEffect : macroEffect.getMicroEffects()){
            if(microEffect.isMandatory())
                this.manageMicro(microEffect);
            else if((microEffect.isConditional() && !this.isMicroActivated(microEffect)) ||
                    (microEffect.isLimited() && !this.isLimitedActivated) ||
                    (!microEffect.isLimited() && !microEffect.isConditional())){
                this.chooseMicro(microEffect);
            }

        }
    }

    /**
     * Manages the micro effect that has been activated. It analyzes the structure of teh micro effect and asks
     * for players, rooms and squares
     * @param microEffect the micro effect that has been activated
     */
    private void manageMicro(MicroEffect microEffect){
        MicroPack microPack = new MicroPack(microEffect.getMacroNumber(), microEffect.getNumber());
        shootPack.getActivatedMacro(microEffect.getMacroNumber()).getActivatedMicros().add(microPack);
        if(microEffect.isLimited())
            isLimitedActivated = true;

        //start asking stuff
        if(microEffect.isMoveFlag() && !microEffect.isGenerateSquareFlag()) {
            microPack.setSquare(input.askSquares(1).get(0));
        }

        if(microEffect.getMaxTargetPlayerSize() != 0)
            microPack.setPlayersList(input.askPlayers(microEffect.getMaxTargetPlayerSize()));

        if(microEffect.getMaxNmSquareSize() != 0 && !microEffect.isGenerateNMSquareFlag())
            microPack.setNoMoveSquaresList(input.askSquares(microEffect.getMaxNmSquareSize()));

        if(microEffect.getMaxTargetRoomSize() != 0)
            microPack.setRoomsList(input.askRooms(microEffect.getMaxTargetRoomSize()));


    }

    /**
     * Ask the player for the optional activation of a micro effect
     * @param microEffect the micro effect whose activation is optional
     */
    private void chooseMicro(MicroEffect microEffect) {
        boolean answer = input.getChoice(microEffect);
        if(answer){
            this.manageMicro(microEffect);
        }
    }

    /**
     * Checks if a macro effect has been activated and thus inserted into the shoot pack
     * @param macroEffect the macro effect that is going to be checked
     * @return true if the macro effect has been activated, false otherwise
     */
    private boolean isMacroActivated(MacroEffect macroEffect){
        for(MacroPack macroPack : shootPack.getActivatedMacros()){
            if (macroPack.getMacroNumber() == macroEffect.getNumber())
                return true;
        }
        return false;
    }

    /**
     * Checks if the micro effect has been activated
     * @param microEffect the micro effect that is going to be checked
     * @return true if the specified micro effect has been activated
     */
    private boolean isMicroActivated(MicroEffect microEffect){
        for(MacroPack macroPack : shootPack.getActivatedMacros()){
            if(macroPack.getMacroNumber() == microEffect.getMacroNumber()){
                for(MicroPack microPack : macroPack.getActivatedMicros()){
                    if(microPack.getMicroNumber() == microEffect.getNumber()){
                        return true;
                    }
                }
                return false;
            }
        }
        return false;
    }

    /**
     * Ask the client for the activation of the specified macro effect
     * @param macroEffect the macro effect that is asked
     */
    private void chooseMacro(MacroEffect macroEffect){
        boolean answer = input.getChoice(macroEffect);
        if(answer){
            this.manageMacro(macroEffect);
        }
    }


}
