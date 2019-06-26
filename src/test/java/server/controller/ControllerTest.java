package server.controller;

import constants.Color;
import org.junit.jupiter.api.Test;
import server.GameManager;
import server.Server;
import server.model.player.ConcretePlayer;
import server.model.player.Figure;
import server.model.player.PlayerAbstract;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {

    @Test
    public void pointDistributionTest() throws RemoteException {
        Controller controller = new Controller(1,5,new GameManager(new Server()));
        PlayerAbstract mimmo = new ConcretePlayer("mimmo");
        PlayerAbstract pluto = new ConcretePlayer("pluto");
        PlayerAbstract pippo = new ConcretePlayer("pippo");
        PlayerAbstract nino = new ConcretePlayer("nino");

        controller.getCurrentGame().addPlayer(mimmo);
        controller.getCurrentGame().addPlayer(pluto);
        controller.getCurrentGame().addPlayer(pippo);
        controller.getCurrentGame().addPlayer(nino);
        mimmo.setPlayerCharacter(Figure.BANSHEE);
        pluto.setPlayerCharacter(Figure.DESTRUCTOR);
        pippo.setPlayerCharacter(Figure.SPROG);
        nino.setPlayerCharacter(Figure.VIOLET);

        pluto.addDamage(4, Color.GREEN); //pippo
        pluto.addDamage(3, Color.PURPLE); //nino
        pluto.addDamage(5, Color.BLUE); //mimmo

        controller.handleDeaths();

        assertEquals(7,pippo.getPoints());
        assertEquals(4,nino.getPoints());
        assertEquals(8,mimmo.getPoints());

        pluto.addDamage(4, Color.GREEN); //pippo
        pluto.addDamage(3, Color.PURPLE); //nino
        pluto.addDamage(4, Color.BLUE); //mimmo

        controller.handleDeaths();

        assertEquals(14,pippo.getPoints()); //7+6+1
        assertEquals(6,nino.getPoints());   //4+2
        assertEquals(12,mimmo.getPoints()); //8+4


    }

    @Test
    public void doublekillTest() throws RemoteException{
        Controller controller = new Controller(1,5,new GameManager(new Server()));
        PlayerAbstract mimmo = new ConcretePlayer("mimmo");
        PlayerAbstract pluto = new ConcretePlayer("pluto");
        PlayerAbstract pippo = new ConcretePlayer("pippo");

        controller.getCurrentGame().addPlayer(mimmo);
        controller.getCurrentGame().addPlayer(pluto);
        controller.getCurrentGame().addPlayer(pippo);
        mimmo.setPlayerCharacter(Figure.BANSHEE);
        pluto.setPlayerCharacter(Figure.DESTRUCTOR);
        pippo.setPlayerCharacter(Figure.SPROG);

        pluto.addDamage(11, Color.BLUE); //mimmo
        pippo.addDamage(11, Color.BLUE); //mimmo

        controller.handleDeaths();

        assertEquals(19,mimmo.getPoints()); //8+8+1+1+1
    }
}