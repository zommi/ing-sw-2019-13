package server.model.cards;


import client.weapons.Weapon;

import java.io.Serializable;

/**
 *
 */
public class WeaponCard implements CollectableInterface, CardInterface, Serializable {

    private String name;
    private Weapon weapon;
    private boolean ready;

    /**
     * Initialize the card cost
     */
    public WeaponCard(Weapon weapon) {
        this.weapon = weapon;
        this.name = weapon.getName();
        this.ready = true;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public boolean isReady() {
        return ready;
    }

    public String getName() {
        return name;
    }

    public void discard() { }


    public void draw() {
        //Draw card from spawnpoint.
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof WeaponCard))
            return false;
        WeaponCard card2 = (WeaponCard) obj;

        return this.getName().equalsIgnoreCase(card2.getName());

    }

    @Override
    public String toString() {
        return this.name;
    }

    public String getPath(){
        return weapon.getPath();
    }
}