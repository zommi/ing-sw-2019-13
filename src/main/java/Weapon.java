import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;


public class Weapon {

    private Bullet bullet;
    private Command command;

    public Weapon() {
    }

    private Bullet shoot() {
        return(command.prepareBullet());
    }

    public Command getCommand(){
        return this.command;
    }


    public Bullet charge() {
        // TODO implement here
        return null;
    }

}