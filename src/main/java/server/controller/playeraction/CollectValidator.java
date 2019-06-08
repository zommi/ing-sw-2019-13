package server.controller.playeraction;

import client.CollectInfo;
import client.weapons.Cost;
import constants.Constants;
import server.model.cards.PowerUpCard;
import server.model.map.SpawnPoint;
import server.model.map.Square;
import server.model.map.SquareAbstract;
import server.model.player.PlayerAbstract;
import server.model.player.PlayerState;

import java.util.ArrayList;
import java.util.List;

public class CollectValidator {

    public boolean validate(PlayerAbstract player, SquareAbstract square, int choice, CollectInfo collectInfo) {


        //checks that the number of steps the player take is allowed by the rules of the game:
        //max 2 movements if the player has more than 2 damages,
        // max 1 movement if the player has less than 2 damages

        if(square == null)
            return false;

        if ((player.currentState() == PlayerState.BETTER_COLLECT || player.currentState() == PlayerState.BETTER_SHOOT)
                && square.distance(player.getPosition()) > Constants.MAX_MOVES_BETTER_COLLECT)
            return false;

        if(player.currentState() == PlayerState.NORMAL
                && square.distance(player.getPosition()) > Constants.MAX_MOVES_NORMAL_COLLECT)
            return false;

        //checks illegal combinations
        if(square instanceof SpawnPoint && choice == Constants.NO_CHOICE)
            return false;

        if(square instanceof Square && choice != Constants.NO_CHOICE)
            return false;

        //choice must be legal and player should be able to pay
        if(square instanceof SpawnPoint){ //then it is a Weapon

            if(choice >= ((SpawnPoint) square).getWeaponCards().size())
                return false;


            //checking powerups
            //converting powerUpCards
            List<PowerUpCard> powerUpCards = new ArrayList<>();
            if(collectInfo.getPowerUpCards() == null)
                return false;
            for(PowerUpCard powerUpCard : collectInfo.getPowerUpCards()){
                if(player.getPowerUpCard(powerUpCard) == null)
                    return false;
                else
                    powerUpCards.add(powerUpCard);
            }

            //attacker must have the selected powerups
            if(!player.hasCards(collectInfo.getPowerUpCards()))
                return false;

            if(!player.canPay(((SpawnPoint) square).getWeaponCards().get(choice).getWeapon().getBuyCost()
                    .subtract(Cost.powerUpListToCost(collectInfo.getPowerUpCards()))))
                return false;

            if(player.getHand().getWeaponHand().size() == Constants.MAX_WEAPON_HAND) {
                //you have to discard a weapon if you have a full weaponHand
                //checks if player owns the weaponToDiscard

                if (collectInfo.getWeaponToDiscard() == null ||
                        player.getWeaponCard(collectInfo.getWeaponToDiscard().getWeapon()) == null)
                    return false;
            }

        }

        //chosen square must not be empty
        return !square.isEmpty();
    }

}

