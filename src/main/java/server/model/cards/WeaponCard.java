package server.model.cards;



/**
 *
 */
public class WeaponCard implements CollectableInterface, CardInterface {

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


    public void getEffect() {
        // TODO READ FROM FILE
        return;
    }


    public void collect() {

    }


    @Override
    public String toString() {
        return this.name;
    }
}