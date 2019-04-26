package Model.Player;

/**
 * 
 */
public class BetterCollectPlayerState extends PlayerState {
    private ConcretePlayer player;
    /**
     * Default constructor
     */
    public BetterCollectPlayerState(ConcretePlayer player) {
        super(player);
        this.player = player;
    }

}