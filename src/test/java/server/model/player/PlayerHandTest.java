package server.model.player;

import org.junit.jupiter.api.Test;
import server.GameManager;
import server.Server;
import server.controller.Controller;
import server.model.cards.PowerUpCard;
import server.model.gameboard.*;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

class PlayerHandTest {

    @Test
    public void testPlayCard(){
        ConcretePlayer p = new ConcretePlayer("pippo");
        p.setPlayerCharacter(Figure.DESTRUCTOR);
        assertEquals(Figure.DESTRUCTOR, p.getGameCharacter().getFigure());
        PlayerHand ph = new PlayerHand(p);


    }


    @Test
    public void infiniteDraw() throws RemoteException {
        Controller controller = new Controller(1,5,new GameManager(new Server()));
        ConcretePlayer p = new ConcretePlayer("pippo");
        controller.getCurrentGame().addPlayerToGame(p);
        controller.getGameManager().addPlayerBeforeMatchStarts(p);
        p.setCurrentGame(controller.getCurrentGame());
        PowerupDeck deck = controller.getCurrentGame().getCurrentGameBoard().getPowerupDeck();
        int largenum = deck.getDeck().size() * 100;
        PowerUpCard card;
        for(int i = 0; i < largenum; i++) {
            card = deck.draw();
            p.getHand().addCard(card);
            p.getHand().removePowerUpCard(card);
        }
    }
}