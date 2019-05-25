package view;

import server.model.gameboard.*;

public class GameBoardAnswer implements ServerAnswer {

    private GameBoard result;

    public GameBoardAnswer(GameBoard gameBoard){
        result = gameBoard.createCopy(gameBoard);
    }

    public GameBoard getResult() {
        return result;
    }
}
