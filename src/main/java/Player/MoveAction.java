package Player;

import Constants.Direction;

import java.util.List;

public class MoveAction {
    private List<Direction> moves;
    private PlayerAbstract player;

    public MoveAction(ActionInfo info){
        this.moves = info.getMoves();
        this.player = info.getPlayer();
    }


    public void execute(){
        this.move();
    }

    private void move() {
        for(Direction dir : moves){
            player.move(dir);
        }
    }
}
