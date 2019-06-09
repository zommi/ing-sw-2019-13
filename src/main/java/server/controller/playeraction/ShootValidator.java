package server.controller.playeraction;

import client.SquareInfo;
import client.weapons.*;

import constants.Color;
import constants.Constants;
import server.model.cards.*;
import server.model.game.Game;
import server.model.gameboard.GameBoard;
import server.model.map.Room;
import server.model.map.SquareAbstract;
import server.model.player.PlayerAbstract;
import server.model.player.PlayerState;

import java.util.ArrayList;
import java.util.List;

public class ShootValidator {

    public ShootInfo validate(ShootPack shootPack, Game game, PlayerAbstract attacker){
        GameBoard gameBoard = game.getCurrentGameBoard();

        ShootInfo shootInfo = convert(shootPack, game, attacker);
        if(shootInfo == null)       //if conversion fails
            return null;

        /*//IMPORTANT:
        //uncomment these lines only when instantiating weapon cards and assigning them to players

        //attacker must have the selected weapon
        if(shootInfo.getAttacker().getWeaponCard(shootInfo.getWeapon()) == null)
            return null;

        //weapon must be loaded
        if(!shootInfo.getAttacker().getWeaponCard(shootInfo.getWeapon()).isReady())
            return null;*/

        //attacker must have the selected powerups
        if(!shootInfo.getAttacker().hasCards(shootInfo.getPowerUpCards()))
            return null;

        //saves players' old positions and sets map to not valid if weapon is special
        if(shootInfo.getWeapon().isSpecial()){
            gameBoard.getMap().setValid(false);
            for(PlayerAbstract playerAbstract : game.getActivePlayers()){
                if(playerAbstract.getPosition() != null)
                    playerAbstract.getGameCharacter().setOldPosition();
            }
        }

        //validating adrenalinic move
        if(shootInfo.getAttacker().getPlayerState()==PlayerState.BETTER_SHOOT){
            shootInfo.getAttacker().getGameCharacter().setOldPosition();
            if(shootInfo.getSquare() != null) {
                if(shootInfo.getSquare().distance(shootInfo.getAttacker().getPosition()) == Constants.MAX_MOVES_BETTER_SHOOT) {
                    gameBoard.getMap().setValid(false);
                    shootInfo.getAttacker().getGameCharacter().move(shootInfo.getSquare());
                }
                else{
                    return null;
                }
            }
        }else{
            if(shootInfo.getSquare()!=null)
                return null;
        }

        //a try finally block is used to restore old positions
        try {
            //checks if macros order is correct and there are no duplicates
            if (shootInfo.getActivatedMacros().isEmpty())
                return null;
            for (int i = 0; i < shootInfo.getActivatedMacros().size() - 1; i++) {
                if (shootInfo.getActivatedMacros().get(i).getMacroNumber() >= shootInfo.getActivatedMacros().get(i + 1).getMacroNumber())
                    return null;
            }

            //checks if macros are activated correctly, accordingly to weapon type
            if (shootInfo.getWeapon().getType() != WeaponType.EXTRA) {
                if (shootInfo.getActivatedMacros().size() > 1)   //we know size is not zero
                    return null;

            } else {
                //checks if all mandatory macros are activated(extra type weapon base effect)
                for (int i = 0; i < shootInfo.getWeapon().getMacroEffects().size(); i++) {
                    if (shootInfo.getWeapon().getMacroEffects().get(i).isMandatory() &&
                            shootInfo.getActivatedMacro(i) == null)
                        return null;
                }
            }

            //now every single activated macro is processed
            Cost totalCost = new Cost(0, 0, 0);
            boolean limitedActivated = false;
            MacroEffect weaponMacro;
            for (MacroInfo macroInfo : shootInfo.getActivatedMacros()) {
                //weapon must have this macro
                if(shootInfo.getWeapon().getMacroEffect(macroInfo.getMacroNumber()) == null)
                    return null;
                else
                    weaponMacro = shootInfo.getWeapon().getMacroEffect(macroInfo.getMacroNumber());

                //player should have enough ammo
                totalCost = totalCost.sum(weaponMacro.getCost());
                //it's a surprise tool that will help us later in the actuator
                shootInfo.setTotalCost(totalCost);
                //also removing cubes given by powerups
                if (!shootInfo.getAttacker().canPay(totalCost.subtract(Cost.powerUpListToCost(shootInfo.getPowerUpCards()))))
                    return null;

                //checks if macro depends on the activation of another macro
                if (weaponMacro.isConditional() &&
                        shootInfo.getActivatedMacro(weaponMacro.getMacroEffectIndex()) == null)
                    return null;

                //checks if micros order is correct and there are no duplicates
                if (macroInfo.getActivatedMicros().isEmpty())
                    return null;
                for (int i = 0; i < macroInfo.getActivatedMicros().size() - 1; i++) {
                    if (macroInfo.getActivatedMicros().get(i).getMicroNumber() >= macroInfo.getActivatedMicros().get(i + 1).getMicroNumber())
                        return null;
                }

                //checks if all mandatory micros are activated
                for (int i = 0; i < weaponMacro.getMicroEffects().size(); i++) {
                    if (weaponMacro.getMicroEffects().get(i).isMandatory() &&
                            shootInfo.getActivatedMicro(macroInfo.getMacroNumber(), i) == null)
                        return null;
                }

                //now every activated micro for that specific macro is processed
                for (MicroInfo microInfo : macroInfo.getActivatedMicros()) {
                    //weapon must have the couple macro-micro
                    MicroEffect weaponMicro;
                    if(weaponMacro.getMicroEffect(microInfo.getMicroNumber()) == null)
                        return null;
                    else
                        weaponMicro = weaponMacro.getMicroEffect(microInfo.getMicroNumber());

                    //checks if micro is limited
                    if (weaponMicro.isLimited() && limitedActivated)
                        return null;
                    else if (weaponMicro.isLimited())
                        limitedActivated = true;

                    //checks if micro is conditional
                    if (weaponMicro.isConditional() &&
                            shootInfo.getActivatedMicro(weaponMicro.getMacroEffectIndex(),
                                    weaponMicro.getMicroEffectIndex()) == null)
                        return null;

                    //checks if input dimensions are ok
                    if (!shootInfo.areDimensionsOk(microInfo, weaponMicro))
                        return null;

                    //checks if attacker is inside player list
                    for (PlayerAbstract playerAbstract : microInfo.getPlayersList()) {
                        if (playerAbstract.equals(shootInfo.getAttacker()))
                            return null;
                    }

                    //checks if there are non-spawned targets
                    for(PlayerAbstract playerAbstract : microInfo.getPlayersList()){
                        if(playerAbstract.getPosition() == null)
                            return null;
                    }

                    //checks if there are duplicates inside player list
                    for(int i=0; i<microInfo.getPlayersList().size(); i++){
                        for(int j=i+1; j<microInfo.getPlayersList().size(); j++){
                            if(microInfo.getPlayersList().get(i).equals(microInfo.getPlayersList().get(j))){
                                return null;
                            }
                        }
                    }

                    //checks every micro policy
                    for (WeaponPolicy weaponPolicy : weaponMicro.getPolicies()) {
                        if (!(weaponMicro.isGeneratePlayerFlag() && weaponPolicy.getPolicyType().equals("player")) &&
                                !(weaponMicro.isGenerateSquareFlag() && weaponPolicy.getPolicyType().equals("square")) &&
                                !(weaponMicro.isGenerateNMSquareFlag() && weaponPolicy.getPolicyType().equals("noMoveSquare")) &&
                                !weaponPolicy.isVerified(shootInfo, microInfo))
                            return null;
                    }

                    //start player/square targets generation
                    if(weaponMicro.isGeneratePlayerFlag()){
                        //adds all active players to playerList and then removes those who don't meet
                        //  policy requirements
                        for(PlayerAbstract playerAbstract : game.getActivePlayers()){
                            if(playerAbstract.getPosition() != null)
                                microInfo.getPlayersList().add(playerAbstract);
                        }
                    }
                    for(WeaponPolicy weaponPolicy : weaponMicro.getPolicies()){
                        if(weaponMicro.isGeneratePlayerFlag() && weaponPolicy.getPolicyType().equals("player") ||
                                weaponMicro.isGenerateSquareFlag() && weaponPolicy.getPolicyType().equals("square") ||
                                (weaponMicro.isGenerateNMSquareFlag() && weaponPolicy.getPolicyType().equals("noMoveSquare"))){
                            weaponPolicy.generate(shootInfo, microInfo);
                        }
                    }

                    //fakes micro actuation if weapon is special
                    if (shootInfo.getWeapon().isSpecial()) {
                        microInfo.fakeActuate(shootInfo);       //just moves characters, without giving damage
                    }
                }
            }
            return shootInfo;

        } finally {
            if (shootInfo.getWeapon().isSpecial()) {
                for (PlayerAbstract playerAbstract : game.getActivePlayers()) {
                    if(playerAbstract.getPosition() != null)
                        playerAbstract.getGameCharacter().move(playerAbstract.getGameCharacter().getOldPosition());
                }
                gameBoard.getMap().setValid(true);
            }

            if(shootInfo.getAttacker().getPlayerState() == PlayerState.BETTER_SHOOT){
                shootInfo.getAttacker().getGameCharacter().move(shootInfo.getAttacker().getGameCharacter().getOldPosition());
            }
        }
    }

    public ShootInfo convert(ShootPack shootPack, Game game, PlayerAbstract attacker)  {
        //must return null if conversion fails
        GameBoard gameBoard = game.getCurrentGameBoard();
        List<MacroInfo> macroInfos = new ArrayList<>();
        for(MacroPack macroPack : shootPack.getActivatedMacros()){
            List<MicroInfo> microInfos = new ArrayList<>();
            for(MicroPack microPack : macroPack.getActivatedMicros()){
                List<PlayerAbstract> playerAbstractList = new ArrayList<>();
                SquareAbstract squareAbstract;
                List<Room> roomList = new ArrayList<>();
                List<SquareAbstract> nmsList = new ArrayList<>();

                //converting players
                for(String string : microPack.getPlayersList()){
                    if(game.getPlayer(string) == null)
                        return null;
                    else
                        playerAbstractList.add(game.getPlayer(string));
                }

                //converting move square if moveSquare is active
                squareAbstract = null;
                if(microPack.getSquare() != null) {
                    if (gameBoard.getMap().getSquare(microPack.getSquare().getRow(),
                            microPack.getSquare().getCol()) == null)
                        return null;
                    else {
                        squareAbstract = gameBoard.getMap().getSquare(microPack.getSquare().getRow(),
                                microPack.getSquare().getCol());
                    }
                }

                //converting rooms
                for(String string : microPack.getRoomsList()){
                    if(gameBoard.getMap().getRoom(Color.fromString(string)) == null)
                        return null;
                    else
                        roomList.add(gameBoard.getMap().getRoom(Color.fromString(string)));
                }

                //converting no move square
                for(SquareInfo squareInfo : microPack.getNoMoveSquaresList()){
                    if(gameBoard.getMap().getSquare(squareInfo.getRow(), squareInfo.getCol()) == null)
                        return null;
                    else
                        nmsList.add(gameBoard.getMap().getSquare(squareInfo.getRow(), squareInfo.getCol()));
                }

                //create microInfo
                microInfos.add(new MicroInfo(microPack.getMacroNumber(), microPack.getMicroNumber(),
                        playerAbstractList, squareAbstract, roomList, nmsList));
            }
            macroInfos.add(new MacroInfo(macroPack.getMacroNumber(), microInfos));
        }

        //converting adrenalinic square
        SquareAbstract squareAbstract;
        if(shootPack.getSquare() != null) {
            squareAbstract = gameBoard.getMap().getSquare(shootPack.getSquare().getRow(), shootPack.getSquare().getCol());
            if(squareAbstract== null)
                return null;
        }
        else
            squareAbstract = null;

        //converting powerUpCards
        List<PowerUpCard> powerUpCards = new ArrayList<>();
        if(shootPack.getPowerUpCards() == null)
            return null;
        for(PowerUpCard powerUpCard : shootPack.getPowerUpCards()){
            if(attacker.getPowerUpCard(powerUpCard) == null)
                return null;
            else
                powerUpCards.add(powerUpCard);
        }

        return new ShootInfo(attacker, gameBoard.getWeapon(shootPack.getWeapon()), macroInfos, squareAbstract, powerUpCards);
    }
}
