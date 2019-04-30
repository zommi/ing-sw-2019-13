package server.model.cards;

/**
 * 
 */
public class TagbackGrenade extends Powerup {

    //This PowerUp can be used during another's turn. So we can check the value of bullet.getDamage() everytime a player receive a bullet
    //if it is > 0 then this can be used.

    private Bullet bullet;

    public TagbackGrenade() {
    }


    public Bullet usePowerup() {
        // TODO implement here

        bullet = new Bullet(0,0,0,1,false);
        //It gives one mark to the visible character that shot me.
        //It does not move the character.
        return bullet;
    }

    @Override
    public String getName() {
        return "Tagback Grenade";
    }
}