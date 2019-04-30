package server.model.player;

/**
 *
 */
public abstract class PlayerState {

    private ConcretePlayer player;

    public PlayerState(ConcretePlayer player) {
        this.player = player;
    }


    public ConcretePlayer getPlayer() {
        return this.player;
    }

    public void nextState() {
    }

    public void shoot() {
    }

    public void collect() {
    }

}