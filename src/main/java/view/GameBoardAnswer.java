package view;

import server.model.gameboard.*;

public class GameBoardAnswer implements ServerAnswer {

    private KillshotTrack track;
    private WeaponDeck weaponDeck;
    private PowerupDeck powerupDeck;
    private AmmoTileDeck ammoTileDeck;

    public GameBoardAnswer(KillshotTrack track, WeaponDeck weaponDeck, PowerupDeck powerupDeck, AmmoTileDeck ammoTileDeck){
        this.track = track;
        this.weaponDeck = weaponDeck;
        this.powerupDeck = powerupDeck;
        this.ammoTileDeck = ammoTileDeck;
    }

    public KillshotTrack getTrack() {
        return this.track;
    }
    public WeaponDeck getWeaponDeck() {
        return this.weaponDeck;
    }
    public PowerupDeck getPowerupDeck() {
        return powerupDeck;
    }
    public AmmoTileDeck getAmmoTileDeck() {
        return ammoTileDeck;
    }
}
