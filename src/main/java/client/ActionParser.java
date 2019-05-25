package client;

import java.util.StringTokenizer;

public class ActionParser {

    public static Info createMoveEvent(int coordinatex, int coordinatey) {
        Info moveInfo = new MoveInfo(coordinatex, coordinatey);
        return null;
    }

    public static Info createShootEvent(){
        return null;
        //TODO
    }


    public static Info createCollectEvent(int collectDecision) {
        return null;
    }

    public static Info createPowerUpEvent(String PowerUp) {
        return null;
    }
}

