package view;

import server.model.gameboard.*;
import server.model.player.PlayerBoard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
