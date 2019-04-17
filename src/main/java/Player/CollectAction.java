package Player;

import Constants.*;

import java.util.List;

public class CollectAction implements Action{
    private List<Directions> moves;
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
        for(Directions dir : moves){
            player.move(dir);
        }
        //player.add(player.getCharacter().getPosition().getItem());
    }
}
