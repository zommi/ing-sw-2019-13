package view;

import server.model.gameboard.*;

public class GameBoardAnswer implements ServerAnswer {

    private GameBoard result;

    public GameBoardAnswer(GameBoard gameBoard){
        result = gameBoard.CreateCopy(gameBoard);
    }

    public GameBoard getResult() {
        return result;
    }
}
