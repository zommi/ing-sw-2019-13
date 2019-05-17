package server.controller.playeraction;

import exceptions.NoSuchSquareException;
import org.junit.jupiter.api.Test;
import server.model.game.Game;
import server.model.gameboard.GameBoard;
import server.model.player.ConcretePlayer;
import server.model.player.Figure;
import server.model.player.PlayerAbstract;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShootValidatorTest {

    @Test
    void validate() throws NoSuchSquareException {
        GameBoard testGb = new GameBoard(1,8);
        PlayerAbstract player1 = new ConcretePlayer("Pippo", testGb, Figure.DESTRUCTOR);
        PlayerAbstract player2 = new ConcretePlayer("Pluto", testGb, Figure.BANSHEE);

        player1.spawn(testGb.getMap().getSquare(0,2));
        player2.spawn(testGb.getMap().getSquare(1,0));

        List<PlayerAbstract> playerList = new ArrayList<>();
        playerList.add(player2);
        MicroInfo microInfo = new MicroInfo(0, 0, playerList, null, Collections.emptyList(), Collections.emptyList());
        List<MicroInfo> activatedMicros = new ArrayList<>();
        activatedMicros.add(microInfo);
        MacroInfo macroInfo = new MacroInfo(0,activatedMicros);
        List<MacroInfo> activatedMacros = new ArrayList<>();
        activatedMacros.add(macroInfo);

        ShootInfo shootInfo = new ShootInfo(player1, testGb.getWeaponDeck().getWeapon("Zx-2"), activatedMacros);

        ShootValidator shootValidator = new ShootValidator();

        assertTrue(shootValidator.validate(shootInfo, testGb));
    }
}