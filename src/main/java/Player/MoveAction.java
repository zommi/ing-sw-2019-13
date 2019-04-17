package Player;

import Constants.*;

import java.util.List;

public class MoveAction {
    private List<Directions> moves;
    private PlayerAbstract player;

    public MoveAction(ActionInfo info){
        this.moves = info.getMoves();
        this.player = info.getPlayer();
    }


    public void execute(){
        this.move();
    }

    private void move() {
        for(Directions dir : moves){
            player.move(dir);
        }
    }
}
