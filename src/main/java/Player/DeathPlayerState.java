package Player;

/**
 * 
 */
public class DeathPlayerState extends PlayerState {
    private ConcretePlayer player;

    /**
     * Default constructor
     */
    public DeathPlayerState(ConcretePlayer player) {
        super(player);
        this.player = player;
    }

}