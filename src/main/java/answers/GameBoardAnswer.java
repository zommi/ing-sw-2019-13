package answers;

import server.model.gameboard.*;
import server.model.player.PlayerBoard;

import java.io.Serializable;

public class GameBoardAnswer implements ServerAnswer, Serializable {

    private GameBoard result;

    public GameBoardAnswer(GameBoard gameBoard){
        result = gameBoard;
    }

    public GameBoard getResult() {
        return result;
    }

    public PlayerBoardAnswer getPlayerBoard(int clientID){
        return (new PlayerBoardAnswer((PlayerBoard) (result.getHashMap()).get(clientID)));
    }
}
