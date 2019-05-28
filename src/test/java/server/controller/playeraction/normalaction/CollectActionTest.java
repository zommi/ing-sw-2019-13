package server.controller.playeraction.normalaction;

import constants.Color;
import constants.Direction;
import org.junit.jupiter.api.Test;
import client.CollectInfo;
import client.MoveInfo;
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

        PlayerAbstract testPlayer = new ConcretePlayer("pippo");
        testPlayer.setPlayerCharacter(Figure.SPROG);
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

        //TODO change implementation!
        //MoveInfo correctNormalMove = new MoveInfo(testPlayer, correctNormalList);
        CollectInfo noChoice = new CollectInfo(0,0);

        //TODO change implementation!
        //CollectAction testAction = new CollectAction(correctNormalMove, noChoice);

        //TODO
        //assertTrue(testAction.execute());

        testPlayer.setState(PlayerState.BETTER_COLLECT);

        //TODO change implementation!
        //MoveInfo correctAdrenalineMove = new MoveInfo(testPlayer, correctAdrenalineList);

        //TODO
        //testAction = new CollectAction(correctAdrenalineMove, noChoice);

        //TODO
        //assertFalse(testAction.execute());

        //TODO change implementation!
        //MoveInfo correctAdrenalineMoveAndCollect = new MoveInfo(testPlayer,correctAdrenalineList);
        CollectInfo collectSecondItem = new CollectInfo(1);
        //TODO
        //testAction = new CollectAction(correctAdrenalineMoveAndCollect, collectSecondItem);

        //TODO
        //assertTrue(testAction.execute());
        assertTrue(testPlayer.getPosition() instanceof SpawnPoint);

        //TODO change implementation!
        //MoveInfo wrongMove = new MoveInfo(testPlayer, wrongList);

        //TODO
        //testAction = new CollectAction(wrongMove, noChoice);

        //TODO
        //assertFalse(testAction.execute());


    }

}