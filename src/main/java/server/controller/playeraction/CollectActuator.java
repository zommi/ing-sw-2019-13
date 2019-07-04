package server.controller.playeraction;

import client.CollectInfo;
import constants.Constants;
import server.controller.Controller;
import server.model.cards.CollectableInterface;
import server.model.cards.WeaponCard;
import server.model.map.SpawnPoint;
import server.model.map.Square;
import server.model.map.SquareAbstract;
import server.model.player.PlayerAbstract;
import view.MessageAnswer;

/**
 * Actuates the collect action
 */

public class CollectActuator {

    /**
     * Put the ammo and the powerup card, if present, or the chosen weapon card in the hand of the player.
     * Removes the selected weapon if the hand is full and puts it loaded in the spawn point of the collected weapon.
     *
     * @param player the player that is making the collect action
     * @param square the square selected
     * @param choice {@link Constants#NO_CHOICE} if collecting an ammo, otherwise one of the three weapons (or less if weapon cards
     *                                          are not enough to be put in the spawn points) in the selected square
     * @param controller the controller of the game in which the collect action is made
     * @param collectInfo the collect info
     */
    public void actuate(PlayerAbstract player, SquareAbstract square, int choice, Controller controller, CollectInfo collectInfo){
        player.getGameCharacter().move(square);
        CollectableInterface card;
        if(choice == Constants.NO_CHOICE){
            Square squarePlayer =  (Square)player.getPosition();
            player.collect(squarePlayer);
            card = ((Square) player.getPosition()).getAmmoTile();
            squarePlayer.removeItem(card, controller);
        }else {     //collecting a weapon

            //removes powerups and pay
            player.payWithPowerUps(((SpawnPoint) square).getWeaponCards().get(choice).getWeapon().getBuyCost(),
                    collectInfo.getPowerUpCards());

            SpawnPoint spawnPoint = (SpawnPoint) player.getPosition();

            if(player.getHand().getWeaponHand().size() == Constants.MAX_WEAPON_HAND) {
                //reloads weapon to discard
                player.getWeaponCard(collectInfo.getWeaponToDiscard().getWeapon()).setReady(true);

                //discard weapon from hand and from PLAYERBOARD and moves it into the spawn point
                player.getHand().removeWeaponCard(collectInfo.getWeaponToDiscard());
                player.getPlayerBoard().removeWeaponCard(collectInfo.getWeaponToDiscard());
                square.addItem(collectInfo.getWeaponToDiscard());
            }


            player.collect(spawnPoint, choice);
            card = ((SpawnPoint)player.getPosition()).getWeaponCards().get(choice);
            spawnPoint.removeItem(card, controller);
        }

        //sending message
        String message = player.getName() + " collected " + (choice == Constants.NO_CHOICE ?
                "some ammo" : ((WeaponCard)card).getName());
        controller.getGameManager().sendEverybodyExcept(new MessageAnswer(message), player.getClientID());
    }
}
