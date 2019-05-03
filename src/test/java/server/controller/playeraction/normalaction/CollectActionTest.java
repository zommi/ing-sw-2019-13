package server.controller.playeraction.normalaction;

import constants.Color;
import constants.Constants;
import constants.Directions;
import org.junit.jupiter.api.Test;
import server.controller.playeraction.CollectInfo;
import server.controller.playeraction.MoveInfo;
import server.model.game.Game;
import server.model.gameboard.GameBoard;
import server.model.map.GameMap;
import server.model.map.SpawnPoint;
import server.model.player.ConcretePlayer;
import server.model.player.Figure;
import server.model.player.PlayerAbstract;
import server.model.player.PlayerState;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CollectActionTest {



    @Test
    public void executeTest() {
        GameBoard testGB = new GameBoard(1, 8);
        testGB.setupGameBoard();

        PlayerAbstract testPlayer = new ConcretePlayer("pippo", testGB, Figure.SPROG);
        testPlayer.spawn(GameMap.getSpawnPoint(Color.BLUE));

        List<Directions> emptyList = new ArrayList<>();

        List<Directions> correctNormalList = new ArrayList<>();
        correctNormalList.add(Directions.SOUTH);

        List<Directions> correctAdrenalineList = new ArrayList<>();
        correctAdrenalineList.add(Directions.EAST);
        correctAdrenalineList.add(Directions.SOUTH);

        List<Directions> wrongList = new ArrayList<>();
        wrongList.add(Directions.WEST);
        wrongList.add(Directions.WEST);
        wrongList.add(Directions.NORTH);

        MoveInfo correctNormalMove = new MoveInfo(testPlayer, correctNormalList);
        CollectInfo noChoice = new CollectInfo();

        CollectAction testAction = new CollectAction(correctNormalMove, noChoice);

        assertTrue(testAction.execute());

        testPlayer.setState(PlayerState.BETTER_COLLECT);

        MoveInfo correctAdrenalineMove = new MoveInfo(testPlayer, correctAdrenalineList);

        testAction = new CollectAction(correctAdrenalineMove, noChoice);

        assertFalse(testAction.execute());

        MoveInfo correctAdrenalineMoveAndCollect = new MoveInfo(testPlayer,correctAdrenalineList);
        CollectInfo collectSecondItem = new CollectInfo(1);
        testAction = new CollectAction(correctAdrenalineMoveAndCollect, collectSecondItem);

        assertTrue(testAction.execute());
        assertTrue(testPlayer.getPosition() instanceof SpawnPoint);

        MoveInfo wrongMove = new MoveInfo(testPlayer, wrongList);

        testAction = new CollectAction(wrongMove, noChoice);

        assertFalse(testAction.execute());


    }

}