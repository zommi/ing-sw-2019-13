package client.gui;

import client.*;
import client.gui.guielements.*;
import constants.Color;
import constants.Constants;
import constants.Direction;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;

import java.io.ObjectOutputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;

import server.controller.playeraction.normalaction.CollectAction;
import server.model.cards.PowerUpCard;
import server.model.cards.WeaponCard;
import server.model.map.GameMap;
import server.model.map.SpawnPoint;
import server.model.map.Square;
import server.model.map.SquareAbstract;
import server.model.player.PlayerAbstract;
import server.model.player.PlayerBoard;
import view.PlayerBoardAnswer;

public class MainGuiController implements GuiController {

    @FXML
    private GridPane weaponHand;

    @FXML
    private GridPane powerupHand;

    @FXML
    private GridPane buttonGrid;

    @FXML
    private StackPane mapStackPane;

    @FXML
    private TextArea textLogger;

    private String log;

    @FXML
    private ScrollPane chat;

    private GameModel model;

    boolean iteration = true;

    private int weaponHandSize = 0;

    private UpdaterGui gui;

    private ActionParser actionParser;

    private int side = 175;

    private List<GuiSquare> squares = new ArrayList<>();

    private List<GuiSpawnPoint> spawnPoints = new ArrayList<>();

    private List<GuiTile> tiles = new ArrayList<>();

    private GuiTile tileToUpdate;

    private GuiCharacter myCharacter;

    private Map<Integer,GuiCharacter> idClientGuiCharacterMap = new HashMap<>();
    @Override
    public void init(){
        log = "Game started! \n";
        initializeMap();
        this.actionParser = gui.getActionParser();
        textLogger.setText(log);
        this.textLogger.setEditable(false);
    }

    public void spawn() {
        logText("Choose a powerup and Spawn in that color! \n");
        DrawInfo action = new DrawInfo();
        this.gui.getConnection().send(action);
    }

    @Override
    public void enableAll() {
        for(GuiTile tile : tiles){
            tile.setDisable(false);
        }
        for(Node buttonNode : buttonGrid.getChildren()){
            buttonNode.setDisable(false);
        }
        for(Node weaponNode : weaponHand.getChildren()){
            weaponNode.setDisable(false);
        }
        for(Node powerupNode : powerupHand.getChildren()){
            powerupNode.setDisable(false);
        }
        logText("It's your turn! \n");
    }

    @Override
    public void disableAll() {
        for(GuiTile tile : tiles){
            tile.setDisable(true);
        }
        for(Node buttonNode : buttonGrid.getChildren()){
            buttonNode.setDisable(true);
        }
        for(Node weaponNode : weaponHand.getChildren()){
            weaponNode.setDisable(true);
        }
        for(Node powerupNode : powerupHand.getChildren()){
            powerupNode.setDisable(true);
        }
        logText("It's not your turn, disabled commands \n");
    }

    private void initializeMap() { //NOSONAR
        this.model = gui.getGameModel();
        GameMap map = model.getMap().getResult();
        //currentSquare = getMap.getIterator().next()
        int col = 0;
        int row = 0;
        int doorSize = side / 3;
        GuiDoorway doorway;

        GridPane mapGridPane = new GridPane();
        this.mapStackPane.getChildren().add(mapGridPane);

        SquareAbstract currentSquare;
        List<SquareAbstract> squaresAlreadyAdded = new ArrayList<>();
        LinkedList<SquareAbstract> queue = new LinkedList<>();

        queue.push(map.getSquare(row,col));
        squaresAlreadyAdded.add(map.getSquare(row,col));

        while(!queue.isEmpty()){
            currentSquare = queue.pop();
            if(currentSquare == null) break;

            if (!currentSquare.isSpawnPoint()){
                Square squareConverted = (Square) currentSquare;
                GuiSquare square = new GuiSquare(currentSquare.getRow(),currentSquare.getCol(),
                        side,side,Paint.valueOf(currentSquare.getColor().getSpHexValue()));
                square.setCursor(Cursor.HAND);
                square.setAmmo(new GuiAmmoTile(squareConverted.getAmmoTile().getPath()));
                squares.add(square);
                tiles.add(square);
                square.setSquare((Square) currentSquare);
                mapGridPane.add(
                        square,
                        currentSquare.getCol()*2,
                        currentSquare.getRow()*2);
            } else {
                GuiSpawnPoint spawnPoint = new GuiSpawnPoint(currentSquare.getRow(),currentSquare.getCol(),
                        side,side,currentSquare.getColor());
                spawnPoints.add(spawnPoint);
                tiles.add(spawnPoint);
                spawnPoint.setCursor(Cursor.HAND);
                mapGridPane.add(
                        spawnPoint,
                        currentSquare.getCol()*2,
                        currentSquare.getRow()*2);
                //spawnPoint.setOnMousePressed(e -> showWeapons(spawnPoint));
                spawnPoint.setCardsOnSpawnPoint((SpawnPoint) currentSquare);
                spawnPoint.setSpawnPoint((SpawnPoint) currentSquare);
            }

            for(Direction dir : Direction.values()){
                SquareAbstract nextSquare = currentSquare.getNearFromDir(dir);
                if(nextSquare != null) {
                    if(!squaresAlreadyAdded.contains(nextSquare)){
                        queue.push(nextSquare);
                        squaresAlreadyAdded.add(nextSquare);
                    }
                    switch (dir) {
                        case SOUTH:
                            doorway = new GuiDoorway(
                                    doorSize,
                                    doorSize/5,
                                    nextSquare.getColor().equals(currentSquare.getColor()) ?
                                            Paint.valueOf(currentSquare.getColor().getSpHexValue()) :
                                            Paint.valueOf(GuiDoorway.DEFAULT_COLOR)
                            );
                            mapGridPane.add(doorway,currentSquare.getCol()*2,currentSquare.getRow()*2 + 1);
                            GridPane.setHalignment(doorway, HPos.CENTER);
                            break;
                        case EAST:
                            doorway = new GuiDoorway(
                                    doorSize/5,
                                    doorSize,
                                    nextSquare.getColor().equals(currentSquare.getColor()) ?
                                            Paint.valueOf(currentSquare.getColor().getSpHexValue()) :
                                            Paint.valueOf(GuiDoorway.DEFAULT_COLOR)
                            );
                            mapGridPane.add(doorway,currentSquare.getCol()*2 + 1,currentSquare.getRow()*2);
                            GridPane.setValignment(doorway, VPos.CENTER);
                            break;
                        case NORTH:
                            doorway = new GuiDoorway(
                                    doorSize,
                                    doorSize/5,
                                    nextSquare.getColor().equals(currentSquare.getColor()) ?
                                            Paint.valueOf(currentSquare.getColor().getSpHexValue()) :
                                            Paint.valueOf(GuiDoorway.DEFAULT_COLOR)
                            );
                            mapGridPane.add(doorway,currentSquare.getCol()*2,currentSquare.getRow()*2 - 1);
                            GridPane.setHalignment(doorway, HPos.CENTER);
                            break;
                        case WEST:
                            doorway = new GuiDoorway(
                                    doorSize/5,
                                    doorSize,
                                    nextSquare.getColor().equals(currentSquare.getColor()) ?
                                            Paint.valueOf(currentSquare.getColor().getSpHexValue()) :
                                            Paint.valueOf(GuiDoorway.DEFAULT_COLOR)
                            );
                            mapGridPane.add(doorway,currentSquare.getCol()*2 - 1,currentSquare.getRow()*2);
                            GridPane.setValignment(doorway, VPos.CENTER);
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }


    @FXML
    void drawWeapon(MouseEvent event, GuiWeaponCard weaponCard, GuiSpawnPoint sp) {
        if(weaponHandSize == 3) return;
        GuiWeaponCard cardToAdd = weaponCard;

        cardToAdd.setCursor(Cursor.HAND);
        cardToAdd.setFitWidth(weaponHand.getWidth()/3);
        cardToAdd.setFitHeight(weaponHand.getHeight());

        cardToAdd.setOnMousePressed(null);
        //this.weaponHand.add(cardToAdd,weaponHandSize,0);
        Info collectInfo = actionParser.createCollectEvent(sp.getRow(),sp.getCol(),weaponCard.getIndex());
        this.gui.getConnection().send(collectInfo);
        weaponHandSize++;
    }

    private void showWeapons(GuiSpawnPoint spawnPoint) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("WEAPONS AVAILABLE");

        GridPane cards = new GridPane();
        for(int i = 0; i < spawnPoint.getCardsOnSpawnPoint().size(); i ++){
            GuiWeaponCard weapon = new GuiWeaponCard(
                    getClass().getResource(spawnPoint.getCardsOnSpawnPoint().get(i)).toExternalForm(),
                    i);
            weapon.setOnMousePressed(e -> {
                if(weaponHandSize < 3){
                    drawWeapon(e,weapon,spawnPoint);
                    spawnPoint.getCardsOnSpawnPoint().remove(weapon.getIndex());
                    alert.close();
                }
            });
            cards.add(weapon,i,0);
        }

        cards.setBackground(Background.EMPTY);
        alert.getDialogPane().setContent(cards);
        alert.setHeaderText("Choose a card to draw. (Press OK to cancel)");
        alert.setOnCloseRequest(e -> alert.close());
        alert.showAndWait();
    }

    public void enableMove(){
        if(!this.model.getToSpawn()) {
            for (GuiTile tile : tiles) {
                tile.setOnMousePressed(e -> {
                    Info moveInfo = this.actionParser.createMoveEvent(tile.getRow(), tile.getCol());
                    this.gui.getConnection().send(moveInfo);
                    disableMouseEvent();
                });
            }
        }else{
            logText("You first need to spawn!\n");
        }
    }

    public void enableCollect(MouseEvent mouseEvent) {
        if(!this.model.getToSpawn()) {
            for (GuiSquare square : squares) {
                square.setOnMousePressed(e -> {
                    Info collectInfo = this.actionParser.createCollectEvent(square.getRow(), square.getCol(), Constants.NO_CHOICE);
                    this.gui.getConnection().send(collectInfo);
                    this.tileToUpdate = square;
                });
            }
            for (GuiSpawnPoint sp : spawnPoints) {
                sp.setOnMousePressed(e -> {
                    showWeapons(sp);
                    this.tileToUpdate = sp;
                });
            }
        } else {
            logText("You first need to spawn!\n");
        }
    }

    private void disableMouseEvent() {
        for(GuiTile tile : tiles){
            tile.setOnMousePressed(null);
        }
    }

    @Override
    public void addGui(UpdaterGui updaterGui) {
        this.gui = updaterGui;
    }


    private void setSpawn(PowerUpCard card) {
        for(GuiSpawnPoint spawnPoint : this.spawnPoints){
            if(card.getColor() == spawnPoint.getColor()){
                this.myCharacter = new GuiCharacter(spawnPoint,Color.fromCharacter(this.gui.getCharacter()));
                this.idClientGuiCharacterMap.put(this.gui.getConnection().getClientID(),myCharacter);
                this.gui.getGameModel().setToSpawn(false);
                disableMouseEvent();
                break;
            }
        }
        Info spawn = this.actionParser.createSpawEvent(card);
        this.gui.getConnection().send(spawn);
    }

    public void showScoreboard(MouseEvent mouseEvent) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("SCOREBOARD");
        alert.setOnCloseRequest(e -> alert.close());
        GridPane box = new GridPane();
        for(int i = 0; i < this.gui.getGameModel().getGameBoard().getCharacterNames().size() ; i++){
            box.add(new GuiPlayerBoard(this.gui.getGameModel().getGameBoard().getPlayerBoard(i)),0,i);
        }
        alert.showAndWait();
    }

    private void logText(String string){
        log += string;
        this.textLogger.setText(log);
    }

    public void updateHand() {
        emptyHand();
        int i = 0;
        GuiWeaponCard cardToAdd;
        GuiPowerupCard powerupCard;
        for(WeaponCard card : this.model.getPlayerHand().getWeaponHand()){
            cardToAdd = new GuiWeaponCard(
                    card.getName(),
                    getClass().getResource(card.getPath()).toExternalForm(),
                    i);
            this.weaponHand.add(cardToAdd,i,0);
            cardToAdd.setCursor(Cursor.HAND);
            cardToAdd.setFitWidth(weaponHand.getWidth()/3);
            cardToAdd.setFitHeight(weaponHand.getHeight());
            i++;
        }
        i = 0;
        for(PowerUpCard card : this.model.getPlayerHand().getPowerupHand()){
            powerupCard = new GuiPowerupCard(
                    getClass().getResource(card.getPath()).toExternalForm(),
                    card,
                    i);
            powerupCard.setCursor(Cursor.HAND);
            powerupCard.setFitWidth(powerupHand.getWidth()/3);
            powerupCard.setFitHeight(powerupHand.getHeight());

            int finalI = i;
            powerupCard.setOnMousePressed(e -> {
                GuiPowerupCard defaultCard = new GuiPowerupCard(
                        getClass().getResource("/Grafica/cards/AD_powerups_IT_02.png").toExternalForm(),
                        finalI);
                defaultCard.setOnMousePressed(null);
                defaultCard.setCursor(Cursor.DEFAULT);
                defaultCard.setFitWidth(powerupHand.getWidth()/3);
                defaultCard.setFitHeight(powerupHand.getHeight());
                if(this.model.getToSpawn()){
                    setSpawn(card);
                }
                powerupHand.add(defaultCard, finalI, 0);
                enablePowerup();
            });
            this.powerupHand.add(powerupCard,i,0);
            i++;
        }
    }

    private void emptyHand() {
        this.weaponHand.getChildren().removeAll();
        for(int i = 0; i < Constants.MAX_NUMBER_OF_CARDS; i++){
            GuiWeaponCard weaponCard = new GuiWeaponCard(getClass().getResource("/Grafica/cards/AD_weapons_IT_0225.png").toExternalForm(),i);
            weaponCard.setFitWidth(weaponHand.getWidth()/3);
            weaponCard.setFitHeight(weaponHand.getHeight());
            this.weaponHand.add(weaponCard, i, 0);
        }

        this.powerupHand.getChildren().removeAll();
        for(int i = 0; i < Constants.MAX_NUMBER_OF_CARDS; i++){
            GuiPowerupCard powerupCard = new GuiPowerupCard(
                    getClass().getResource("/Grafica/cards/AD_powerups_IT_02.png").toExternalForm(), i);
            powerupCard.setFitWidth(powerupHand.getWidth()/3);
            powerupCard.setFitHeight(powerupHand.getHeight());
            this.powerupHand.add(powerupCard, i, 0);
        }
    }

    private void enablePowerup() {
        for(Node card : powerupHand.getChildren()){
            card.setOnMousePressed(null);
        }
    }

    //metodo che gestisce la visualizzazione di giocatori e ammotile
    public void updateGameboard() {
        if(this.model != null) {
            //update position of players
            for (int i = 0; i < this.model.getPlayersNames().size(); i++) {
                if (this.idClientGuiCharacterMap.get((Object) i) == null) {
                    this.idClientGuiCharacterMap.put(i, spawnEnemy(this.model.getPlayerBoard(i)));
                } else {
                    this.idClientGuiCharacterMap.get(i).setPosition(getTile(
                            this.model.getPlayerBoard(i).getRow(),
                            this.model.getPlayerBoard(i).getCol()
                            ));
                }
            }

            if(tileToUpdate != null){
                tileToUpdate.setSquare(this.model.getMap().getResult().getSquare(tileToUpdate.getRow(),tileToUpdate.getCol()));
                tileToUpdate.restore();
                tileToUpdate = null;
            }
        }
    }

    private GuiCharacter spawnEnemy(PlayerBoardAnswer playerBoard) {
        for(GuiSpawnPoint sp : spawnPoints){
            if(sp.getCol() == playerBoard.getCol() && sp.getRow() == playerBoard.getRow()){
                return new GuiCharacter(sp,Color.fromCharacter(playerBoard.getCharacterName()));
            }
        }
        return null;
    }

    public boolean noSpawned(){
        return this. myCharacter == null || this.myCharacter.getPosition() == null;
    }

    public GuiTile getTile(int row, int col){
        for(GuiTile tile : tiles){
            if(tile.getCol() == col && tile.getRow() == row){
                return tile;
            }
        }
        return null;
    }



}
