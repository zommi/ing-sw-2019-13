package view;

import server.model.player.PlayerAbstract;

import java.util.Map;

public class GameOverAnswer implements ServerAnswer{

    private final Map<PlayerAbstract, Integer> playerToPoint;

    private PlayerAbstract winner;

    public GameOverAnswer(Map<PlayerAbstract, Integer> playerToPoint, PlayerAbstract winner) {
        this.playerToPoint = playerToPoint;
        this.winner = winner;
    }

    public Map<PlayerAbstract, Integer> getPlayerToPoint() {
        return playerToPoint;
    }

    public PlayerAbstract getWinner() {
        return winner;
    }
}
