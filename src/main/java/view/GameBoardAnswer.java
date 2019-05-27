package view;

import server.model.gameboard.*;

import java.util.ArrayList;
import java.util.List;

public class GameBoardAnswer implements ServerAnswer {

    private GameBoard result;

    public GameBoardAnswer(GameBoard gameBoard){
        result = gameBoard.createCopy(gameBoard);
    }

    public GameBoard getResult() {
        return result;
    }

    public WeaponDeck getWeaponDeck(){return result.getWeaponDeck(); }

    public List<String> getCharacterNames(){
        List<String> list = new ArrayList<>();
        for(int i = 0; i < result.getGameCharacterList().size(); i++){
            list.add(result.getGameCharacterList().get(i).getFigure().toString());
        }
        return list;
    }
}
