package server.controller.playeraction;

import client.SquareInfo;
import client.weapons.*;

import constants.Color;
import constants.Constants;
import server.model.cards.*;
import server.model.game.Game;
import server.model.game.GameState;
import server.model.gameboard.GameBoard;
import server.model.map.Room;
import server.model.map.SquareAbstract;
import server.model.player.PlayerAbstract;
import server.model.player.PlayerState;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ShootValidator {

    public ShootInfo validate(ShootPack shootPack, Game game, PlayerAbstract attacker){
        GameBoard gameBoard = game.getCurrentGameBoard();

        List<PlayerAbstract> damagedPlayers = new ArrayList<>();

        ShootInfo shootInfo = convert(shootPack, game, attacker);
        if(shootInfo == null)       //if conversion fails
            return null;

        //IMPORTANT:
        //uncomment these lines only when instantiating weapon cards and assigning them to players

        //attacker must have the selected weapon
        WeaponCard weaponCard = shootInfo.getAttacker().getWeaponCard(shootInfo.getWeapon());
        if(weaponCard == null)
            return null;


        //final frenzy checks
        if(game.getCurrentState() != GameState.FINAL_FRENZY && shootInfo.getReloadInfo() != null)
            return null;

        //if player wants to reload before shooting
        if(shootInfo.getReloadInfo() != null) {
            //cards used for paying the reload, for paying the shoot, and targeting scopes CANNOT intersect
            List<PowerUpCard> combinedPowerUps = new ArrayList<>(shootInfo.getPowerUpCards());
            combinedPowerUps.addAll(shootInfo.getReloadInfo().getPowerUpCards());
            combinedPowerUps.addAll(shootInfo.getScopeInfos().stream().map(ScopeInfo::getTargetingScope).collect(Collectors.toList()));

            if (!attacker.hasPowerUpCards(combinedPowerUps))
                return null;

            ReloadValidator reloadValidator = new ReloadValidator();
            if (reloadValidator.validate(shootInfo.getReloadInfo(), attacker) == null)
                return null;
        }

        //if the attacker wanted to reload, at this point we know for sure that reload info is legal
        //so we don't check if the weapon is actually ready

        //weapon must be loaded if player doesn't want to reload
        else if(!shootInfo.getAttacker().getWeaponCard(shootInfo.getWeapon()).isReady())
            return null;

        //attacker must have the selected powerups
        if(!shootInfo.getAttacker().hasPowerUpCards(shootInfo.getPowerUpCards()))
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
        if(shootInfo.getAttacker().getPlayerState()==PlayerState.ADRENALINIC_SHOOT || game.getCurrentState() == GameState.FINAL_FRENZY){
            shootInfo.getAttacker().getGameCharacter().setOldPosition();
            if(shootInfo.getSquare() != null) {
                if((attacker.getPlayerState() == PlayerState.ADRENALINIC_SHOOT || attacker.getPlayerState() == PlayerState.BEFORE_FIRST_PLAYER_FF) &&
                        shootInfo.getSquare().distance(shootInfo.getAttacker().getPosition()) > Constants.MAX_MOVES_ADRENALINIC_SHOOT) {
                    return null;
                }
                if(attacker.getPlayerState() == PlayerState.AFTER_FIRST_PLAYER_FF &&
                        shootInfo.getSquare().distance(shootInfo.getAttacker().getPosition()) > Constants.MAX_MOVES_FRENETIC_SHOOT){
                    return null;
                }
                else{
                    gameBoard.getMap().setValid(false);
                    shootInfo.getAttacker().getGameCharacter().move(shootInfo.getSquare());
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
            shootInfo.setTotalCost(new Cost(0, 0, 0));
            boolean limitedActivated = false;
            MacroEffect weaponMacro;
            for (MacroInfo macroInfo : shootInfo.getActivatedMacros()) {
                //weapon must have this macro
                if(shootInfo.getWeapon().getMacroEffect(macroInfo.getMacroNumber()) == null)
                    return null;
                else
                    weaponMacro = shootInfo.getWeapon().getMacroEffect(macroInfo.getMacroNumber());

                //updating cost
                shootInfo.setTotalCost(shootInfo.getTotalCost().sum(weaponMacro.getCost()));

                //also removing cubes given by powerups
                if (!shootInfo.getAttacker().canPay(shootInfo.getTotalCost()
                        .subtract(Cost.powerUpListToCost(shootInfo.getPowerUpCards()))))
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

                    //adds damaged player to a list for a later check
                    for(PlayerAbstract playerAbstract : microInfo.getPlayersList()){
                        if(weaponMicro.getDamage() != 0 && !damagedPlayers.contains(playerAbstract))
                            damagedPlayers.add(playerAbstract);
                    }
                }
            }

            //validating targeting scopes
            if(shootInfo.getScopeInfos() != null){
                List<PowerUpCard> cardsUsed = new ArrayList<>();

                for(ScopeInfo scopeInfo : shootInfo.getScopeInfos()){
                    cardsUsed.add(scopeInfo.getTargetingScope());

                    if(!damagedPlayers.contains(scopeInfo.getTarget()))
                        return null;

                    shootInfo.setTotalCost(shootInfo.getTotalCost().sum(Cost.getCost(scopeInfo.getColor())));

                }

                //you cannot use the ammocube of the targeting scope card to pay if you are using its effect
                for(PowerUpCard powerUpCard : shootInfo.getPowerUpCards()){
                    for(ScopeInfo scopeInfo : shootInfo.getScopeInfos()){
                        if(powerUpCard.equals(scopeInfo.getTargetingScope()))
                            return null;
                    }
                }

                //player must have the selected powerups
                if(!attacker.hasPowerUpCards(cardsUsed))
                    return null;

                //checks if player can pay
                if(!attacker.canPay(shootInfo.getTotalCost().subtract(Cost.powerUpListToCost(shootInfo.getPowerUpCards()))))
                    return null;

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

            if(shootInfo.getAttacker().getPlayerState() == PlayerState.ADRENALINIC_SHOOT){
                shootInfo.getAttacker().getGameCharacter().move(shootInfo.getAttacker().getGameCharacter().getOldPosition());
            }
        }
    }

    public ShootInfo convert(ShootPack shootPack, Game game, PlayerAbstract attacker)  {
        //must return null if conversion fails
        GameBoard gameBoard = game.getCurrentGameBoard();
        List<MacroInfo> macroInfos = new ArrayList<>();

        if(shootPack == null)
            return null;

        for(MacroPack macroPack : shootPack.getActivatedMacros()){
            List<MicroInfo> microInfos = new ArrayList<>();
            if(macroPack == null)
                return null;
            for(MicroPack microPack : macroPack.getActivatedMicros()){
                if(microPack == null)
                    return null;
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

        //checking if player has powerups to pay and targeting scopes to use COMBINED
        if(shootPack.getPowerUpCards() != null && shootPack.getScopePacks() != null) {
            List<PowerUpCard> combinedPowerUps = new ArrayList<>(shootPack.getPowerUpCards());
            for(ScopePack scopePack : shootPack.getScopePacks()){
                if(scopePack.getTargetingScope() != null)
                    combinedPowerUps.add(scopePack.getTargetingScope());
            }
            
            if(!attacker.hasPowerUpCards(combinedPowerUps))
                return null;
        }


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

        //converting scopePacks
        List<ScopeInfo> scopeInfos = new ArrayList<>();

        if(shootPack.getScopePacks() != null) {

            PowerUpCard targetingScope;
            PlayerAbstract target;
            Color color;
            for (ScopePack scopePack : shootPack.getScopePacks()) {
                targetingScope = attacker.getPowerUpCard(scopePack.getTargetingScope());
                if (targetingScope == null)
                    return null;
                target = game.getPlayer(scopePack.getTarget());
                if (target == null)
                    return null;
                color = scopePack.getColor();
                if (color == null || !color.isAmmoColor())
                    return null;

                scopeInfos.add(new ScopeInfo(targetingScope, target, color));
            }
        }

        return new ShootInfo(attacker, gameBoard.getWeapon(shootPack.getWeapon()),
                            macroInfos, squareAbstract, powerUpCards, scopeInfos, shootPack.getReloadInfo());
    }
}
