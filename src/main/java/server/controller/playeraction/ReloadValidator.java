package server.controller.playeraction;

import client.ReloadInfo;
import client.weapons.Cost;
import server.model.cards.PowerUpCard;
import server.model.cards.WeaponCard;
import server.model.player.PlayerAbstract;

import java.util.ArrayList;
import java.util.List;

public class ReloadValidator {

    public ReloadInfo validate(ReloadInfo reloadInfo, PlayerAbstract player){
        List<WeaponCard> weaponCards = new ArrayList<>();
        List<PowerUpCard> powerUpCards = new ArrayList<>();

        reloadInfo.setCost(new Cost(0,0,0));

        //converting references
        for(WeaponCard weaponCard : reloadInfo.getWeaponCards()){
            if(player.getWeaponCard(weaponCard.getWeapon()) != null)
                weaponCards.add(weaponCard);
            else
                return null;
        }

        reloadInfo.setWeaponCards(weaponCards);

        for(PowerUpCard powerUpCard : reloadInfo.getPowerUpCards()){
            if(player.getPowerUpCard(powerUpCard) != null)
                powerUpCards.add(powerUpCard);
            else
                return null;
        }

        reloadInfo.setPowerUpCards(powerUpCards);

        //checks that player has all the selected weapons
        if(!player.hasWeaponCards(reloadInfo.getWeaponCards()))
            return null;

        //checks that all selected weapons are unloaded and calculate the total cost
        for(WeaponCard weaponCard : reloadInfo.getWeaponCards()){
            if(weaponCard.isReady())
                return null;
            else{
                reloadInfo.setCost(reloadInfo.getCost().sum(weaponCard.getWeapon().getReloadCost()));
            }

        }

        //player must have the selected powerups
        if(!player.hasPowerUpCards(reloadInfo.getPowerUpCards()))
            return null;

        //can pay
        if(!player.canPay(reloadInfo.getCost().subtract(Cost.powerUpListToCost(reloadInfo.getPowerUpCards()))))
            return null;

        return reloadInfo;


    }
}
