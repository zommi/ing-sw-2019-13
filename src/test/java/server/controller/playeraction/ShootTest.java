package server.controller.playeraction;


import client.ReloadInfo;
import client.SquareInfo;
import client.weapons.MacroPack;
import client.weapons.MicroPack;
import client.weapons.ScopePack;
import client.weapons.ShootPack;
import constants.Color;
import org.junit.jupiter.api.Test;
import server.model.cards.PowerUpCard;
import server.model.game.Game;
import server.model.game.GameState;
import server.model.player.ConcretePlayer;
import server.model.player.Figure;
import server.model.player.PlayerAbstract;
import server.model.player.PlayerState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShootTest {

    @Test
    void lockRifle() {

        Game game = new Game(1,5,null);

        PlayerAbstract player00 = new ConcretePlayer("player00");
        player00.setPlayerCharacter(Figure.DESTRUCTOR);
        game.getPlayers().add(player00);
        player00.spawn(game.getCurrentGameBoard().getMap().getSquare(0,0));

        PlayerAbstract player01 = new ConcretePlayer("player01");
        player01.setPlayerCharacter(Figure.BANSHEE);
        game.getPlayers().add(player01);
        player01.spawn(game.getCurrentGameBoard().getMap().getSquare(0,1));


        ShootPack shootPack0 = new ShootPack("lock rifle");

        shootPack0.setScopePacks(Collections.emptyList());
        shootPack0.setPowerUpCards(Collections.emptyList());
        shootPack0.setReloadInfo(null);

        //macropack0
        MacroPack macroPack0 = new MacroPack(0);
        shootPack0.getActivatedMacros().add(macroPack0);

        MicroPack microPack00 = new MicroPack(0,0);
        macroPack0.getActivatedMicros().add(microPack00);

        List<String> listPlayer = new ArrayList<>();
        listPlayer.add("player01");
        microPack00.setPlayersList(listPlayer);


        //setting up player ammo and cards
        int[] ammo = {0,0,0};
        player00.getPlayerBoard().setAmmo(ammo);
        player00.getHand().getWeaponHand().add(game.getCurrentGameBoard().getWeaponCard("lock rifle"));


        assertNotNull((new ShootValidator()).validate(shootPack0, game, player00));

    }

    @Test
    void powerupPayment(){
        Game game = new Game(1,5,null);

        PlayerAbstract player00 = new ConcretePlayer("player00");
        player00.setPlayerCharacter(Figure.DESTRUCTOR);
        game.getPlayers().add(player00);
        player00.spawn(game.getCurrentGameBoard().getMap().getSquare(0,0));

        PlayerAbstract player01 = new ConcretePlayer("player01");
        player01.setPlayerCharacter(Figure.BANSHEE);
        game.getPlayers().add(player01);
        player01.spawn(game.getCurrentGameBoard().getMap().getSquare(0,1));

        PlayerAbstract player02 = new ConcretePlayer("player02");
        player02.setPlayerCharacter(Figure.SPROG);
        game.getPlayers().add(player02);
        player02.spawn(game.getCurrentGameBoard().getMap().getSquare(0,2));


        ShootPack shootPack0 = new ShootPack("lock rifle");

        List<ScopePack> scopePacks = new ArrayList<>();
        scopePacks.add(new ScopePack(game.getCurrentGameBoard().getPowerUpCard(1), "player01", Color.RED));
        shootPack0.setScopePacks(scopePacks);

        List<PowerUpCard> powerUpCardList = new ArrayList<>();
        powerUpCardList.add(game.getCurrentGameBoard().getPowerUpCard(0));
        shootPack0.setPowerUpCards(powerUpCardList);

        shootPack0.setReloadInfo(null);

        //macropack0
        MacroPack macroPack0 = new MacroPack(0);
        shootPack0.getActivatedMacros().add(macroPack0);

        MicroPack microPack00 = new MicroPack(0,0);
        macroPack0.getActivatedMicros().add(microPack00);

        List<String> listPlayer = new ArrayList<>();
        listPlayer.add("player01");
        microPack00.setPlayersList(listPlayer);

        //macropack1
        MacroPack macroPack1 = new MacroPack(1);
        shootPack0.getActivatedMacros().add(macroPack1);

        MicroPack microPack10 = new MicroPack(1,0);
        macroPack1.getActivatedMicros().add(microPack10);

        List<String> listPlayer1 = new ArrayList<>();
        listPlayer1.add("player02");
        microPack10.setPlayersList(listPlayer1);


        //setting up player ammo and cards
        int[] ammo = {1,0,0};
        player00.getPlayerBoard().setAmmo(ammo);
        player00.getHand().getWeaponHand().add(game.getCurrentGameBoard().getWeaponCard("lock rifle"));
        player00.getHand().getPowerupHand().add(game.getCurrentGameBoard().getPowerUpCard(0));
        player00.getHand().getPowerupHand().add(game.getCurrentGameBoard().getPowerUpCard(1));


        assertNotNull((new ShootValidator()).validate(shootPack0, game, player00));
    }

    @Test
    void powerupPaymentTargetingScopeCombined(){
        Game game = new Game(1,5,null);

        PlayerAbstract player00 = new ConcretePlayer("player00");
        player00.setPlayerCharacter(Figure.DESTRUCTOR);
        game.getPlayers().add(player00);
        player00.spawn(game.getCurrentGameBoard().getMap().getSquare(0,0));

        PlayerAbstract player01 = new ConcretePlayer("player01");
        player01.setPlayerCharacter(Figure.BANSHEE);
        game.getPlayers().add(player01);
        player01.spawn(game.getCurrentGameBoard().getMap().getSquare(0,1));

        PlayerAbstract player02 = new ConcretePlayer("player02");
        player02.setPlayerCharacter(Figure.SPROG);
        game.getPlayers().add(player02);
        player02.spawn(game.getCurrentGameBoard().getMap().getSquare(0,2));


        ShootPack shootPack0 = new ShootPack("lock rifle");

        List<ScopePack> scopePacks = new ArrayList<>();
        scopePacks.add(new ScopePack(game.getCurrentGameBoard().getPowerUpCard(0), "player01", Color.RED));
        shootPack0.setScopePacks(scopePacks);

        List<PowerUpCard> powerUpCardList = new ArrayList<>();
        powerUpCardList.add(game.getCurrentGameBoard().getPowerUpCard(0));
        shootPack0.setPowerUpCards(powerUpCardList);

        shootPack0.setReloadInfo(null);

        //macropack0
        MacroPack macroPack0 = new MacroPack(0);
        shootPack0.getActivatedMacros().add(macroPack0);

        MicroPack microPack00 = new MicroPack(0,0);
        macroPack0.getActivatedMicros().add(microPack00);

        List<String> listPlayer = new ArrayList<>();
        listPlayer.add("player01");
        microPack00.setPlayersList(listPlayer);

        //macropack1
        MacroPack macroPack1 = new MacroPack(1);
        shootPack0.getActivatedMacros().add(macroPack1);

        MicroPack microPack10 = new MicroPack(1,0);
        macroPack1.getActivatedMicros().add(microPack10);

        List<String> listPlayer1 = new ArrayList<>();
        listPlayer1.add("player02");
        microPack10.setPlayersList(listPlayer1);


        //setting up player ammo and cards
        int[] ammo = {1,0,0};
        player00.getPlayerBoard().setAmmo(ammo);
        player00.getHand().getWeaponHand().add(game.getCurrentGameBoard().getWeaponCard("lock rifle"));
        player00.getHand().getPowerupHand().add(game.getCurrentGameBoard().getPowerUpCard(0));
        player00.getHand().getPowerupHand().add(game.getCurrentGameBoard().getPowerUpCard(0));


        assertNull((new ShootValidator()).validate(shootPack0, game, player00));
    }

    @Test
    void invisible(){
        Game game = new Game(1,5,null);

        PlayerAbstract player00 = new ConcretePlayer("player00");
        player00.setPlayerCharacter(Figure.DESTRUCTOR);
        game.getPlayers().add(player00);
        player00.spawn(game.getCurrentGameBoard().getMap().getSquare(0,0));

        PlayerAbstract player01 = new ConcretePlayer("player01");
        player01.setPlayerCharacter(Figure.BANSHEE);
        game.getPlayers().add(player01);
        player01.spawn(game.getCurrentGameBoard().getMap().getSquare(2,1));


        ShootPack shootPack0 = new ShootPack("Heatseeker");

        shootPack0.setScopePacks(Collections.emptyList());
        shootPack0.setPowerUpCards(Collections.emptyList());
        shootPack0.setReloadInfo(null);

        //macropack0
        MacroPack macroPack0 = new MacroPack(0);
        shootPack0.getActivatedMacros().add(macroPack0);

        MicroPack microPack00 = new MicroPack(0,0);
        macroPack0.getActivatedMicros().add(microPack00);

        List<String> listPlayer = new ArrayList<>();
        listPlayer.add("player01");
        microPack00.setPlayersList(listPlayer);


        //setting up player ammo and cards
        int[] ammo = {0,0,0};
        player00.getPlayerBoard().setAmmo(ammo);
        player00.getHand().getWeaponHand().add(game.getCurrentGameBoard().getWeaponCard("Heatseeker"));


        assertNotNull((new ShootValidator()).validate(shootPack0, game, player00));
    }

    @Test
    void rocketLauncher(){
        Game game = new Game(1,5,null);

        PlayerAbstract player00 = new ConcretePlayer("player00");
        player00.setPlayerCharacter(Figure.DESTRUCTOR);
        game.getPlayers().add(player00);
        player00.spawn(game.getCurrentGameBoard().getMap().getSquare(0,1));
        player00.setCurrentGame(game);

        PlayerAbstract player01 = new ConcretePlayer("player01");
        player01.setPlayerCharacter(Figure.BANSHEE);
        game.getPlayers().add(player01);
        player01.spawn(game.getCurrentGameBoard().getMap().getSquare(0,1));
        player01.setCurrentGame(game);


        PlayerAbstract player02 = new ConcretePlayer("player02");
        player02.setPlayerCharacter(Figure.SPROG);
        game.getPlayers().add(player02);
        player02.spawn(game.getCurrentGameBoard().getMap().getSquare(0,1));
        player02.setCurrentGame(game);


        ShootPack shootPack0 = new ShootPack("Rocket Launcher");

        shootPack0.setScopePacks(Collections.emptyList());
        shootPack0.setPowerUpCards(Collections.emptyList());
        shootPack0.setReloadInfo(null);


        //macropacks

        MacroPack macroPack0 = new MacroPack(0);
        shootPack0.getActivatedMacros().add(macroPack0);

        MicroPack microPack00 = new MicroPack(0,0);
        macroPack0.getActivatedMicros().add(microPack00);
        microPack00.setSquare(new SquareInfo(1,0));


        MacroPack macroPack1 = new MacroPack(1);
        shootPack0.getActivatedMacros().add(macroPack1);

        MicroPack microPack10 = new MicroPack(1,0);
        macroPack1.getActivatedMicros().add(microPack10);
        List<String> listPlayer = new ArrayList<>();
        listPlayer.add("player01");
        microPack10.setPlayersList(listPlayer);

        MicroPack microPack11 = new MicroPack(1,1);
        macroPack1.getActivatedMicros().add(microPack11);
        microPack11.setSquare(new SquareInfo(0,0));


        MacroPack macroPack3 = new MacroPack(3);
        shootPack0.getActivatedMacros().add(macroPack3);

        MicroPack microPack30 = new MicroPack(3,0);
        macroPack3.getActivatedMicros().add(microPack30);

        MicroPack microPack31 = new MicroPack(3,1);
        macroPack3.getActivatedMicros().add(microPack31);



        //setting up player ammo and cards
        int[] ammo = {0,1,1};
        player00.getPlayerBoard().setAmmo(ammo);
        player00.getHand().getWeaponHand().add(game.getCurrentGameBoard().getWeaponCard("Rocket Launcher"));


        ShootInfo shootInfo = new ShootValidator().validate(shootPack0, game, player00);

        assertNotNull(shootInfo);

        ShootActuator shootActuator = new ShootActuator();
        shootActuator.actuate(shootInfo, null);

        assertFalse(game.getCurrentGameBoard().getWeaponCard("Rocket Launcher").isReady());
        assertEquals(player00.getPosition(), game.getCurrentGameBoard().getMap().getSquare(1,0));
        assertEquals(player01.getPosition(), game.getCurrentGameBoard().getMap().getSquare(0,0));
        assertEquals(player02.getPosition(), game.getCurrentGameBoard().getMap().getSquare(0,1));
        assertEquals(player01.getPlayerBoard().getDamage().size(), 3);
        assertEquals(player02.getPlayerBoard().getDamage().size(), 1);

    }

    @Test
    void flameThrower(){
        Game game = new Game(1,5,null);

        PlayerAbstract player00 = new ConcretePlayer("player00");
        player00.setPlayerCharacter(Figure.DESTRUCTOR);
        game.getPlayers().add(player00);
        player00.spawn(game.getCurrentGameBoard().getMap().getSquare(0,0));

        PlayerAbstract player01 = new ConcretePlayer("player01");
        player01.setPlayerCharacter(Figure.BANSHEE);
        game.getPlayers().add(player01);
        player01.spawn(game.getCurrentGameBoard().getMap().getSquare(0,1));

        PlayerAbstract player02 = new ConcretePlayer("player02");
        player02.setPlayerCharacter(Figure.SPROG);
        game.getPlayers().add(player02);
        player02.spawn(game.getCurrentGameBoard().getMap().getSquare(0,1));

        PlayerAbstract player4 = new ConcretePlayer("player4");
        player4.setPlayerCharacter(Figure.SPROG);
        game.getPlayers().add(player4);
        player4.spawn(game.getCurrentGameBoard().getMap().getSquare(0,2));

        PlayerAbstract player5 = new ConcretePlayer("player5");
        player5.setPlayerCharacter(Figure.SPROG);
        game.getPlayers().add(player5);
        player5.spawn(game.getCurrentGameBoard().getMap().getSquare(0,2));


        ShootPack shootPack0 = new ShootPack("Flamethrower");

        shootPack0.setScopePacks(Collections.emptyList());
        shootPack0.setPowerUpCards(Collections.emptyList());
        shootPack0.setReloadInfo(null);

        //macropacks
        MacroPack macroPack1 = new MacroPack(1);
        shootPack0.getActivatedMacros().add(macroPack1);

        MicroPack microPack10 = new MicroPack(1,0);
        macroPack1.getActivatedMicros().add(microPack10);
        List<SquareInfo> squareInfos = new ArrayList<>();
        squareInfos.add(new SquareInfo(0,1));
        microPack10.setNoMoveSquaresList(squareInfos);

        MicroPack microPack11 = new MicroPack(1,1);
        macroPack1.getActivatedMicros().add(microPack11);
        List<SquareInfo> squareInfos1 = new ArrayList<>();
        squareInfos1.add(new SquareInfo(0,2));
        microPack11.setNoMoveSquaresList(squareInfos1);



        int[] ammo = {0,0,2};
        player00.getPlayerBoard().setAmmo(ammo);
        player00.getHand().getWeaponHand().add(game.getCurrentGameBoard().getWeaponCard("Flamethrower"));


        ShootInfo shootInfo = new ShootValidator().validate(shootPack0, game, player00);

        assertNotNull(shootInfo);

        ShootActuator shootActuator = new ShootActuator();
        shootActuator.actuate(shootInfo, null);

        assertEquals(player01.getPlayerBoard().getDamage().size(), 2);
        assertEquals(player02.getPlayerBoard().getDamage().size(), 2);
        assertEquals(player4.getPlayerBoard().getDamage().size(), 1);
        assertEquals(player5.getPlayerBoard().getDamage().size(), 1);
    }

    @Test
    void payScopesReloadMove(){
        Game game = new Game(1,5,null);
        game.setCurrentState(GameState.FINAL_FRENZY);

        PlayerAbstract player00 = new ConcretePlayer("player00");
        player00.setPlayerCharacter(Figure.DESTRUCTOR);
        game.getPlayers().add(player00);
        player00.spawn(game.getCurrentGameBoard().getMap().getSquare(0,1));
        player00.setCurrentGame(game);
        player00.setState(PlayerState.AFTER_FIRST_PLAYER_FF);

        PlayerAbstract player01 = new ConcretePlayer("player01");
        player01.setPlayerCharacter(Figure.BANSHEE);
        game.getPlayers().add(player01);
        player01.spawn(game.getCurrentGameBoard().getMap().getSquare(0,1));
        player01.setCurrentGame(game);

        PlayerAbstract player02 = new ConcretePlayer("player02");
        player02.setPlayerCharacter(Figure.SPROG);
        game.getPlayers().add(player02);
        player02.spawn(game.getCurrentGameBoard().getMap().getSquare(0,1));
        player02.setCurrentGame(game);


        ShootPack shootPack0 = new ShootPack("Rocket Launcher");

        List<ScopePack> scopePacks = new ArrayList<>();
        scopePacks.add(new ScopePack(game.getCurrentGameBoard().getPowerUpCard(0), "player01", Color.RED));
        scopePacks.add(new ScopePack(game.getCurrentGameBoard().getPowerUpCard(2), "player01", Color.BLUE));
        scopePacks.add(new ScopePack(game.getCurrentGameBoard().getPowerUpCard(3), "player01", Color.YELLOW));
        scopePacks.add(new ScopePack(game.getCurrentGameBoard().getPowerUpCard(4), "player01", Color.BLUE));
        shootPack0.setScopePacks(scopePacks);

        List<PowerUpCard> powerUpCards = new ArrayList<>();
        powerUpCards.add(game.getCurrentGameBoard().getPowerUpCard(1));
        powerUpCards.add(game.getCurrentGameBoard().getPowerUpCard(8));
        powerUpCards.add(game.getCurrentGameBoard().getPowerUpCard(10));
        shootPack0.setPowerUpCards(powerUpCards);


        ReloadInfo reloadInfo = new ReloadInfo(Collections.singletonList(game.getCurrentGameBoard()
                .getWeaponCard("rocket launcher")), Arrays.asList(game.getCurrentGameBoard().getPowerUpCard(6),
                game.getCurrentGameBoard().getPowerUpCard(7)));
        shootPack0.setReloadInfo(reloadInfo);

        shootPack0.setSquare(new SquareInfo(1,2));


        //macropacks

        MacroPack macroPack0 = new MacroPack(0);
        shootPack0.getActivatedMacros().add(macroPack0);

        MicroPack microPack00 = new MicroPack(0,0);
        macroPack0.getActivatedMicros().add(microPack00);
        microPack00.setSquare(new SquareInfo(1,0));


        MacroPack macroPack1 = new MacroPack(1);
        shootPack0.getActivatedMacros().add(macroPack1);

        MicroPack microPack10 = new MicroPack(1,0);
        macroPack1.getActivatedMicros().add(microPack10);
        List<String> listPlayer = new ArrayList<>();
        listPlayer.add("player01");
        microPack10.setPlayersList(listPlayer);

        MicroPack microPack11 = new MicroPack(1,1);
        macroPack1.getActivatedMicros().add(microPack11);
        microPack11.setSquare(new SquareInfo(0,0));


        MacroPack macroPack3 = new MacroPack(3);
        shootPack0.getActivatedMacros().add(macroPack3);

        MicroPack microPack30 = new MicroPack(3,0);
        macroPack3.getActivatedMicros().add(microPack30);

        MicroPack microPack31 = new MicroPack(3,1);
        macroPack3.getActivatedMicros().add(microPack31);



        //setting up player ammo and cards
        int[] ammo = {0,2,1};
        player00.getPlayerBoard().setAmmo(ammo);
        player00.getHand().getWeaponHand().add(game.getCurrentGameBoard().getWeaponCard("Rocket Launcher"));
        game.getCurrentGameBoard().getWeaponCard("Rocket Launcher").setReady(false);
        player00.getHand().getPowerupHand().add(game.getCurrentGameBoard().getPowerUpCard(0));
        player00.getHand().getPowerupHand().add(game.getCurrentGameBoard().getPowerUpCard(1));
        player00.getHand().getPowerupHand().add(game.getCurrentGameBoard().getPowerUpCard(2));
        player00.getHand().getPowerupHand().add(game.getCurrentGameBoard().getPowerUpCard(3));
        player00.getHand().getPowerupHand().add(game.getCurrentGameBoard().getPowerUpCard(4));
        player00.getHand().getPowerupHand().add(game.getCurrentGameBoard().getPowerUpCard(6));
        player00.getHand().getPowerupHand().add(game.getCurrentGameBoard().getPowerUpCard(7));
        player00.getHand().getPowerupHand().add(game.getCurrentGameBoard().getPowerUpCard(8));
        player00.getHand().getPowerupHand().add(game.getCurrentGameBoard().getPowerUpCard(10));


        ShootInfo shootInfo = new ShootValidator().validate(shootPack0, game, player00);

        assertNotNull(shootInfo);

        ShootActuator shootActuator = new ShootActuator();
        shootActuator.actuate(shootInfo, null);

        assertFalse(game.getCurrentGameBoard().getWeaponCard("Rocket Launcher").isReady());
        assertEquals(player00.getPosition(), game.getCurrentGameBoard().getMap().getSquare(1,2));
        assertEquals(player01.getPosition(), game.getCurrentGameBoard().getMap().getSquare(0,0));
        assertEquals(player02.getPosition(), game.getCurrentGameBoard().getMap().getSquare(0,1));
        assertEquals(player01.getPlayerBoard().getDamage().size(), 7);
        assertEquals(player02.getPlayerBoard().getDamage().size(), 1);
    }


}