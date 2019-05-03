package server.controller.playeraction;

import constants.Constants;
import constants.Directions;
import server.model.map.SpawnPoint;
import server.model.map.Square;
import server.model.map.SquareAbstract;
import server.model.player.PlayerAbstract;
import server.model.player.PlayerState;

import java.util.List;

public class CollectValidator {

    public CollectValidator() {
    }

    public boolean validate(PlayerAbstract playerToValidate, List<Directions> movements, int choice) {
        if ((playerToValidate.currentState() != PlayerState.NORMAL
                && movements.size() > Constants.MAX_NUMBER_OF_ADRENALINE_MOVEMENTS)
            || (playerToValidate.currentState() == PlayerState.NORMAL
                && movements.size() > Constants.MAX_NUMBER_OF_NORMAL_MOVEMENTS)) {
            return false;
        }
        //TODO add condition choice < number of weapons on spawnpoin
        SquareAbstract squareTemp = playerToValidate.getPosition();
        for (Directions dir : movements) {
            if(squareTemp.getNearFromDir(dir) == null) return false;
            squareTemp = squareTemp.getNearFromDir(dir);
        }
        //assuming the game doesn't allow a player to collect in an empty square
        return ((squareTemp instanceof Square && choice == Constants.NO_CHOICE)
                || (squareTemp instanceof SpawnPoint && choice != Constants.NO_CHOICE))
                && !squareTemp.isEmpty();
    }

}
