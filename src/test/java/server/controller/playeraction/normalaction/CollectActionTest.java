package server.controller.playeraction.normalaction;

import constants.Color;
import constants.Direction;
import org.junit.jupiter.api.Test;
import server.controller.playeraction.CollectInfo;
import server.controller.playeraction.MoveInfo;
import server.model.gameboard.GameBoard;
import server.model.map.SpawnPoint;
import server.model.player.ConcretePlayer;
import server.model.player.Figure;
import server.model.player.PlayerAbstract;
import server.model.player.PlayerState;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CollectActionTest {



    @Test
    public void executeTest() {
        GameBoard testGB = new GameBoard(1, 8);
        testGB.setupGameBoard();

        PlayerAbstract testPlayer = new ConcretePlayer("pippo", testGB, Figure.SPROG);
        testPlayer.spawn(testGB.getMap().getSpawnPoint(Color.BLUE));

        List<Direction> emptyList = new ArrayList<>();

        List<Direction> correctNormalList = new ArrayList<>();
        correctNormalList.add(Direction.SOUTH);

        List<Direction> correctAdrenalineList = new ArrayList<>();
        correctAdrenalineList.add(Direction.EAST);
        correctAdrenalineList.add(Direction.SOUTH);

        List<Direction> wrongList = new ArrayList<>();
        wrongList.add(Direction.WEST);
        wrongList.add(Direction.WEST);
        wrongList.add(Direction.NORTH);

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