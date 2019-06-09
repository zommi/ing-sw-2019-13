package client.weapons;

import client.Info;
import client.SquareInfo;
import server.model.cards.PowerUpCard;

import java.util.ArrayList;
import java.util.List;

public class ShootPack implements Info {

    private String weapon;
    private List<MacroPack> activatedMacros;
    private SquareInfo square;
    private List<PowerUpCard> powerUpCards;
    private List<ScopePack> scopePacks;


    public ShootPack(String weapon){
        this.weapon = weapon;
        this.activatedMacros = new ArrayList<>();
    }

    public String getWeapon(){
        return this.weapon;
    }

    public List<MacroPack> getActivatedMacros() {
        return activatedMacros;
    }

    public MacroPack getActivatedMacro(int macro){
        for(MacroPack macroPack : activatedMacros)
            if(macroPack.getMacroNumber() == macro)
                return macroPack;
        return null;
    }

    public void setScopePacks(List<ScopePack> scopePacks) {
        this.scopePacks = scopePacks;
    }

    public List<ScopePack> getScopePacks() {
        return scopePacks;
    }

    public SquareInfo getSquare() {
        return square;
    }

    public void setSquare(SquareInfo square) {
        this.square = square;
    }

    public void setPowerUpCards(List<PowerUpCard> powerUpCards) {
        this.powerUpCards = powerUpCards;
    }

    public List<PowerUpCard> getPowerUpCards() {
        return powerUpCards;
    }
}
