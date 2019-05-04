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
        //checks that the number of steps the player takes is allowed by the rules of the game:
        //max 2 movements if the player has more than 2 damages,
        // max 1 movement if the player has less than 2 damages
        if ((playerToValidate.currentState() != PlayerState.NORMAL
                && movements.size() > Constants.MAX_NUMBER_OF_ADRENALINE_MOVEMENTS)
            || (playerToValidate.currentState() == PlayerState.NORMAL
                && movements.size() > Constants.MAX_NUMBER_OF_NORMAL_MOVEMENTS)) {
            return false;
        }


        //checks that all the movements are allowed
        SquareAbstract squareTemp = playerToValidate.getPosition();
        for (Directions dir : movements) {
            if(squareTemp.getNearFromDir(dir) == null) return false;
            squareTemp = squareTemp.getNearFromDir(dir);
        }


        //checks that the choice parameter and the type of squareTemp are coherent
        //moreover if squareTemp is a spawn point it check that the choice is inside the boundaries
        //of the list
        //finally it is checks that the square is not empty.
        return ((squareTemp instanceof Square && choice == Constants.NO_CHOICE)
                || (squareTemp instanceof SpawnPoint && choice != Constants.NO_CHOICE
                        && ((SpawnPoint) squareTemp).getWeaponCards().size() > choice))
                && !squareTemp.isEmpty();
    }

}

