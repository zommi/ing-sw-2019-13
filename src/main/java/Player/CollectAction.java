package Player;

import Constants.Direction;

import java.util.List;

public class CollectAction implements Action{
    private List<Direction> moves;
    private PlayerAbstract player;

    public CollectAction(ActionInfo info){
        this.moves = info.getMoves();
        this.player = info.getPlayer();
    }


    @Override
    public void execute() {
        this.collect();
    }

    public void collect(){
        for(Direction dir : moves){
            player.move(dir);
        }
        //player.add(player.getCharacter().getPosition().getItem());
    }
}
