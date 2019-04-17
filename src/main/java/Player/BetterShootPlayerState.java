package Player;

/**
 * 
 */
public class BetterShootPlayerState extends PlayerState {
    private ConcretePlayer player;
    /**
     * Default constructor
     */
    public BetterShootPlayerState(ConcretePlayer player) {
        super(player);
        this.player = player;
    }

}