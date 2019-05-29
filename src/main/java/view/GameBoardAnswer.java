package view;

import server.model.gameboard.*;
import server.model.player.PlayerBoard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameBoardAnswer implements ServerAnswer, Serializable {

    private GameBoard result;

    public GameBoardAnswer(GameBoard gameBoard){
        result = gameBoard.createCopy(gameBoard);
    }

    public GameBoard getResult() {
        return result;
    }

    public List<String> getCharacterNames() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < result.getGameCharacterList().size(); i++) {
            list.add(result.getGameCharacterList().get(i).getFigure().toString());
        }
        return list;
    }

    public AmmoTileDeck getWeaponDeck() {
        //TODO
        return null;
    }

    public PlayerBoardAnswer getPlayerBoard(int clientID){
        return new PlayerBoardAnswer((PlayerBoard) result.getHashMap().get(clientID));
    }
}
