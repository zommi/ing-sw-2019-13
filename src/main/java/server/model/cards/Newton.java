package server.model.cards;

import java.io.Serializable;
import java.util.Scanner;

public class Newton extends Powerup  implements Serializable {

    // The character can use this card before or after his action. He has to choose another character and move it
    // 1 or 2 squares in one direction.


    public Newton() {
    }


    @Override
    public String getName() {
        return "Newton";
    }
}