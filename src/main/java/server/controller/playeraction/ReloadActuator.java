package server.controller.playeraction;

import client.ReloadInfo;
import server.model.cards.WeaponCard;
import server.model.player.PlayerAbstract;

public class ReloadActuator {

    public void actuate(ReloadInfo reloadInfo, PlayerAbstract playerAbstract){
        for(WeaponCard weaponCard : reloadInfo.getWeaponCards()){
            weaponCard.setReady(true);
        }

        playerAbstract.payWithPowerUps(reloadInfo.getCost(), reloadInfo.getPowerUpCards());
    }
}
