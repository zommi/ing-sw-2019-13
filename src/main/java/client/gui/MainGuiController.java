package client.gui;

import client.*;
import client.gui.guielements.*;
import client.weapons.MacroEffect;
import client.weapons.MicroEffect;
import client.weapons.ScopePack;
import client.weapons.Weapon;
import constants.Color;
import constants.Constants;
import constants.Direction;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import server.model.cards.PowerUpCard;
import server.model.cards.TagbackGrenade;
import server.model.cards.WeaponCard;
import server.model.map.*;
import server.model.player.GameCharacter;
import server.model.player.PlayerBoard;
import server.model.player.PlayerState;
import view.PlayerBoardAnswer;

public class MainGuiController implements GuiController {

    /**
     * Reference to the GridPane containing the weaponCards.
     */
    @FXML
    private GridPane weaponHand;

    /**
     * Reference to the GridPane containing the powerup cards.
     */
    @FXML
    private GridPane powerupHand;

    /**
     * Reference to the GridPane containing the action buttons.
     */
    @FXML
    private GridPane buttonGrid;

    /**
     * Reference to the StackPane containing the map.
     */
    @FXML
    private StackPane mapStackPane;

    /**
     * Reference to the TextArea containing the messages for the client.
     */
    @FXML
    private TextArea textLogger;

    /**
     * Reference to the button that shows the scoreboard.
     */
    @FXML
    private Button scoreboardButton;

    /**
     * Log of all the communication with the client
     */
    private String log;

    /**
     * Reference to the local model of the client
     */
    private GameModel model;

    /**
     * Reference to the updater, used to get common fields
     */
    private UpdaterGui gui;

    /**
     * Reference to the parser that creates all infos
     */
    private ActionParser actionParser;

    /**
     * List of squares of the map
     */
    private List<GuiSquare> squares = new ArrayList<>();

    /**
     * List of spawnpoints of the map
     */
    private List<GuiSpawnPoint> spawnPoints = new ArrayList<>();

    /**
     * List containing both squares and spawnpoints
     */
    private List<GuiTile> tiles = new ArrayList<>();

    /**
     * Reference to the input abstract class that handles the parsing
     * for the shoot action.
     */
    private GuiInput input;

    /**
     * Map containing
     */
    private Map<Integer,GuiCharacter> idClientGuiCharacterMap = new HashMap<>();

    /**
     * Constant containing the standard height od powerup cards
     */
    private static final double POWERUP_HEIGHT = 200;

    private static final String SPAWN_ALERT = "You first need to spawn!\n";

    /**
     * Method that initializes the gameboard with the map received
     * from the server and initialiazes the log.
     * It is called when the server notifies the start of the game.
     */
    @Override
    public void init(){
        log = "Game started! \n";
        initializeMap();
        this.actionParser = gui.getActionParser();
        this.actionParser.addGameModel(this.model);
        textLogger.setText(log);
        textLogger.setWrapText(true);
        logText("Welcome " + this.model.getMyPlayer().getName() + "\n");
        this.textLogger.setEditable(false);
    }

    /**
     * Method that enables the action buttons. It is called when the server
     * notifies a player has ended its turn and it's the client's turn.
     */
    @Override
    public void enableAll() {
        for(GuiTile tile : tiles){
            tile.setDisable(false);
        }
        for(Node buttonNode : buttonGrid.getChildren()){
            buttonNode.setDisable(false);
        }
        logText("It's your turn! \n");
    }

    /**
     * Method that enables the action buttons. It is called when the server
     * notifies a player has ended its turn and it's not the client's turn.
     */
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
        scoreboardButton.setDisable(false);
        logText("It's not your turn, disabled commands \n");
    }

    /**
     * Method that prints the map on the screen based on the map in the gameModel,
     * this method is called only during the initialization process
     */
    private void initializeMap() { //NOSONAR
        this.model = gui.getGameModel();
        GameMap map = model.getMap();
        SquareAbstract currentSquare = map.iterator().next();
        int side = 175;
        int doorSize = side / 3;
        GuiDoorway doorway;

        GridPane mapGridPane = new GridPane();
        this.mapStackPane.getChildren().add(mapGridPane);

        List<SquareAbstract> squaresAlreadyAdded = new ArrayList<>();
        LinkedList<SquareAbstract> queue = new LinkedList<>();

        queue.push(currentSquare);
        squaresAlreadyAdded.add(currentSquare);

        while(!queue.isEmpty()){
            currentSquare = queue.pop();
            if(currentSquare == null) break;

            if (!currentSquare.isSpawnPoint()){
                Square squareConverted = (Square) currentSquare;
                GuiSquare square = new GuiSquare(currentSquare.getRow(),currentSquare.getCol(),
                        side, side,Paint.valueOf(currentSquare.getColor().getTileColor()));
                square.setCursor(Cursor.HAND);
                if(squareConverted.getAmmoTile() != null)square.setAmmo(new GuiAmmoTile(squareConverted.getAmmoTile().getPath()));
                squares.add(square);
                tiles.add(square);
                square.setSquare((Square) currentSquare);
                mapGridPane.add(
                        square,
                        currentSquare.getCol()*2,
                        currentSquare.getRow()*2);
            } else {
                GuiSpawnPoint spawnPoint = new GuiSpawnPoint(currentSquare.getRow(),currentSquare.getCol(),
                        side, side,currentSquare.getColor());
                spawnPoints.add(spawnPoint);
                tiles.add(spawnPoint);
                spawnPoint.setCursor(Cursor.HAND);
                mapGridPane.add(
                        spawnPoint,
                        currentSquare.getCol()*2,
                        currentSquare.getRow()*2);
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
                                            Paint.valueOf(currentSquare.getColor().getTileColor()) :
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
                                            Paint.valueOf(currentSquare.getColor().getTileColor()) :
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
                                            Paint.valueOf(currentSquare.getColor().getTileColor()) :
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
                                            Paint.valueOf(currentSquare.getColor().getTileColor()) :
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

    /**
     * Method called when a weapon is collected by the client. If the client
     * has 3 cards in hand it also sends the information regarding the weapon to discard.
     * @param event The mouse pressed.
     * @param weaponCard The card drawn.
     * @param cardToDiscardIndex The index of the card to discard,
     *                           it is set to -1 if there is no swapping
     * @param sp SpawnPoint of the card.
     */
    @FXML
    private void drawWeapon(MouseEvent event, GuiWeaponCard weaponCard, int cardToDiscardIndex, GuiSpawnPoint sp) {
        weaponCard.setCursor(Cursor.HAND);
        weaponCard.setFitWidth(weaponHand.getWidth()/3);
        weaponCard.setFitHeight(weaponHand.getHeight());

        weaponCard.setOnMousePressed(null);

        List<PowerUpCard> powerUpCards = this.model.getPlayerHand().getPowerupHand().isEmpty() ?
                Collections.emptyList() :
                askPowerups();

        Info collectInfo = actionParser.createCollectEvent(sp.getRow(),sp.getCol(),
                weaponCard.getIndex(),
                model.getPlayerHand().getWeaponHand().size() == 3 ? model.getPlayerHand().getWeaponHand().get(cardToDiscardIndex) : null,
                powerUpCards);
        this.gui.getConnection().send(collectInfo);
    }

    private void showWeapons(GuiSpawnPoint spawnPoint) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setGraphic(null);
        alert.setTitle("WEAPONS AVAILABLE");

        GridPane cards = new GridPane();
        for(int i = 0; i < spawnPoint.getCardsOnSpawnPoint().size(); i ++){
            GuiWeaponCard weapon = new GuiWeaponCard(
                    spawnPoint.getCardsOnSpawnPoint().get(i),
                    i,
                    getClass().getResource(spawnPoint.getCardsOnSpawnPoint().get(i).getPath()).toExternalForm());
            weapon.setOnMousePressed(e -> {
                weapon.setPreserveRatio(true);
                alert.close();
                Platform.runLater(() -> {
                    if(model.getPlayerHand().getWeaponHand().size() < 3){
                        drawWeapon(e,weapon,-1,spawnPoint);
                    }else{
                        int weaponToDiscardIndex = askWeaponToDiscard();
                        drawWeapon(e,weapon,weaponToDiscardIndex,spawnPoint);
                    }
                });
                spawnPoint.getCardsOnSpawnPoint().remove(weapon.getIndex());
            });
            cards.add(weapon,i,0);
        }

        cards.setBackground(Background.EMPTY);
        alert.getDialogPane().setContent(cards);
        alert.setHeaderText("Choose a card to draw. (Press OK to cancel)");
        alert.setOnCloseRequest(e -> alert.close());
        alert.showAndWait();
    }

    private int askWeaponToDiscard() {
        Stage empytStage = new Stage(StageStyle.TRANSPARENT);
        empytStage.initModality(Modality.NONE);
        logText("Choose a weapon to swap\n");
        AtomicReference<Integer> result = new AtomicReference<>();
        for(Node card : weaponHand.getChildren()){
            if(card instanceof GuiWeaponCard && GridPane.getColumnIndex(card) == ((GuiWeaponCard)card).getIndex()) {
                card.setOnMousePressed(e -> {
                    result.set(((GuiWeaponCard)card).getIndex());
                    empytStage.close();
                });
            }
        }
        empytStage.showAndWait();
        return result.get();
    }

    /**
     * Method called when the players presses the MOVE action button,
     * it sets the event on the tiles.
     */
    public void enableMove(){
        if(!this.model.isToSpawn() || !this.model.isToRespawn()) {
            for (GuiTile tile : tiles) {
                tile.setOnMousePressed(e -> {
                    Info moveInfo = this.actionParser.createMoveEvent(tile.getRow(), tile.getCol());
                    this.gui.getConnection().send(moveInfo);
                });
            }
            logText("Select a square to move!\n");
        }else{
            logText(SPAWN_ALERT);
        }
    }

    /**
     * Method called when the players presses the COLLECT action button,
     * it sets the event on the tiles.
     */
    public void enableCollect() {
        if(!this.model.isToSpawn() || !this.model.isToRespawn()) {
            for (GuiSquare square : squares) {
                square.setOnMousePressed(e -> {
                    Info collectInfo = this.actionParser.createCollectEvent(square.getRow(), square.getCol(), Constants.NO_CHOICE, null, Collections.emptyList());
                    this.gui.getConnection().send(collectInfo);
                });
            }
            for (GuiSpawnPoint sp : spawnPoints) {
                sp.setOnMousePressed(e -> {
                    showWeapons(sp);
                });
            }
            logText("Select a square!\n");
        } else {
            logText(SPAWN_ALERT);
        }
    }

    /**
     * Method called when the players presses the SHOOT action button,
     * it sets the event on the weapon cards in hand.
     */
    public void enableShoot() {
        if(!this.model.isToSpawn() || !this.model.isToRespawn()){
            logText("Select a weapon to use!\n");
            for(Node card : weaponHand.getChildren()){
                card.setDisable(false);
            }
        }else{
            logText(SPAWN_ALERT);
        }
    }

    /**
     * Method called when the players presses the POWERUP action button,
     * it sets the event on the weapon cards in hand.
     */
    public void enablePowerup(){
        if(!this.model.isToSpawn() || !this.model.isToRespawn()){
            logText("Select a powerup to use! \n");
            for(Node card : powerupHand.getChildren()){
                card.setDisable(false);
            }
        }else{
            logText(SPAWN_ALERT);
        }
    }

    private void setSpawn(PowerUpCard card) {
        Info spawn = this.actionParser.createSpawnEvent(card);
        if(model.isToSpawn()){
            this.gui.getConnection().send(spawn);
            if(this.gui.getConnection().getCurrentID() == this.gui.getConnection().getClientID()){
                this.model.setToSpawn(false);
            }
        }else if(model.isToRespawn()) {
            this.gui.getConnection().sendAsynchronous(spawn);
            this.model.setToRespawn(false);
        }
    }

    /**
     * Shows the scoreboard containing the playerboards of all the players.
     */
    @FXML
    public void showScoreboard() {
        final int PLAYERBOARDS_OFFSET = 1;
        final int KILLSHOT_TRACK_INDEX = 0;
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setGraphic(null);
        alert.setTitle("SCOREBOARD");
        alert.setContentText(null);
        alert.setHeaderText(null);
        alert.setOnCloseRequest(e -> alert.close());
        GridPane box = new GridPane();
        box.add(new GuiKillshotTrack(this.gui.getGameModel().getGameBoard().getResult().getTrack()),0,KILLSHOT_TRACK_INDEX);
        for(int i = 0; i < this.gui.getGameModel().getGameBoard().getResult().getActiveCharacters().size() ; i++){
            box.add(new GuiPlayerBoard(this.gui.getGameModel().getGameBoard().getPlayerBoard(i)),0,i + PLAYERBOARDS_OFFSET);
        }
        alert.getDialogPane().setContent(box);
        alert.setOnCloseRequest(e -> alert.close());
        alert.showAndWait();
    }

    /**
     * Adds text to the log.
     * @param string The message to add.
     */
    void logText(String string){
        log += string;
        this.textLogger.setText(log);
        textLogger.appendText("");
    }

    /**
     * Updates the Weapon hand and powerup hand based on the information coming from the gamemodel,
     * it is called by updaterGui whenever there is a playerhand notification.
     */
    void updateHand() {
        emptyHand();
        int i = 0;
        GuiWeaponCard cardToAdd;
        GuiPowerupCard powerupCard;
        for(WeaponCard card : this.model.getPlayerHand().getPlayerHand().getWeaponHand()){
            cardToAdd = new GuiWeaponCard(card, i, getClass().getResource(card.getPath()).toExternalForm());
            this.weaponHand.add(cardToAdd,i,0);
            cardToAdd.setCursor(Cursor.HAND);
            cardToAdd.setFitWidth(weaponHand.getWidth()/3);
            cardToAdd.setFitHeight(weaponHand.getHeight());
            if(!card.isReady()){
                cardToAdd.setOpacity(0.6);
            }else{
                cardToAdd.setOpacity(1.0);
            }
            GuiWeaponCard finalCardToAdd = cardToAdd;
            cardToAdd.setOnMousePressed(e -> {
                Info info = this.actionParser.createShootEvent(finalCardToAdd.getWeaponCard());
                this.gui.getConnection().send(info);
                for(Node node : weaponHand.getChildren()){
                    node.setDisable(true);
                }
            });
            cardToAdd.setDisable(true);
            i++;
        }
        i = 0;
        for(PowerUpCard card : this.model.getPlayerHand().getPowerupHand()){
            powerupCard = new GuiPowerupCard(
                    getClass().getResource(card.getPath()).toExternalForm(),
                    card,
                    i);
            powerupCard.setCursor(Cursor.HAND);
            powerupCard.setFitHeight(POWERUP_HEIGHT);
            powerupCard.setPreserveRatio(true);


            GuiPowerupCard finalPowerupCard = powerupCard;
            powerupCard.setOnMousePressed(e -> {
                if(model.isToSpawn() || model.isToRespawn()){
                    setSpawn(card);
                }else{
                    Info info = this.actionParser.createPowerUpEvent(finalPowerupCard.getCard());
                    this.gui.getConnection().send(info);
                    for(Node node : powerupHand.getChildren()) {
                        node.setOnMousePressed(null);
                    }
                }
            });
            if(!model.isToSpawn() && !model.isToRespawn())powerupCard.setDisable(true);
            this.powerupHand.add(powerupCard,i,0);
            i++;
        }
    }

    private void emptyHand() {
        this.weaponHand.getChildren().removeAll();
        for(int i = 0; i < Constants.MAX_WEAPON_HAND; i++){
            GuiWeaponCard weaponCard = new GuiWeaponCard(getClass().getResource("/Grafica/cards/AD_weapons_IT_0225.png").toExternalForm(),i);
            weaponCard.setFitWidth(weaponHand.getWidth()/3);
            weaponCard.setFitHeight(weaponHand.getHeight());
            this.weaponHand.add(weaponCard, i, 0);
        }

        this.powerupHand.getChildren().removeAll();
        for(int i = 0; i < Constants.MAX_POWERUP_HAND + 1; i++){
            GuiPowerupCard powerupCard = new GuiPowerupCard(
                    getClass().getResource("/Grafica/cards/AD_powerups_IT_02.png").toExternalForm(), i);
            powerupCard.setFitHeight(POWERUP_HEIGHT);
            powerupCard.setPreserveRatio(true);
            this.powerupHand.add(powerupCard, i, 0);
        }
    }

    /**
     * Updates the map based on the information contained in the gameModel, it is called when
     * by the updaterGui when there is a gameboard notification. It handles the movement of players.
     */
    void updateGameboard() {
        if(this.model != null) {
            //update position of players
            int id;
            for (GameCharacter character : model.getGameBoard().getResult().getActiveCharacters()) {
                id = character.getConcretePlayer().getClientID();
                if(character.getConcretePlayer().getPlayerState() != PlayerState.TOBESPAWNED) {
                    if (this.idClientGuiCharacterMap.get(id) == null) {
                        this.idClientGuiCharacterMap.put(id, placeEnemy(
                                character.getConcretePlayer().getPlayerBoard(),
                                character.getConcretePlayer().getName())
                        );
                    } else {
                        this.idClientGuiCharacterMap.get(id).setPosition(getTile(
                                character.getConcretePlayer().getPlayerBoard().getRow(),
                                character.getConcretePlayer().getPlayerBoard().getCol()
                        ));
                    }
                }
            }


        }
    }

    private GuiCharacter placeEnemy(PlayerBoard playerBoard, String name) {
        for(GuiTile tile : tiles){
            if(tile.getCol() == playerBoard.getCol() && tile.getRow() == playerBoard.getRow()){
                return new GuiCharacter(tile,playerBoard,name);
            }
        }
        return null;
    }

    private GuiTile getTile(int row, int col){
        for(GuiTile tile : tiles){
            if(tile.getCol() == col && tile.getRow() == row){
                return tile;
            }
        }
        return null;
    }

    /**
     * Updates the map based on the information contained in the gameModel, it is called when
     * by the updaterGui when there is a gameboard notification.
     * It handles the restoration of ammotiles and weapons on squares..
     */
    void restoreSquares() {
        for (GuiTile tile : tiles) {
            tile.setSquare(this.model.getMap().getSquare(tile.getRow(),tile.getCol()));
            tile.restore();
        }
    }

    /**
     * {@link InputAbstract#getChoice(MacroEffect)}
     */
    public boolean askMacro(MacroEffect macroEffect) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("MACRO ACTIVATION CONFIRMATION");
        alert.setHeaderText("Do you wanna activate this macro effect?\n" + macroEffect);
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    /**
     * {@link InputAbstract#getChoice(MicroEffect)}
     */
    public boolean askMicro(MicroEffect microEffect) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("MICRO ACTIVATION CONFIRMATION");
        alert.setHeaderText("Do you wanna activate this micro effect?\n" + microEffect);
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    /**
     *{@link InputAbstract#chooseOneMacro(Weapon)}
     */
    public MacroEffect chooseOneMacro(Weapon weapon) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setGraphic(null);
        AtomicReference<MacroEffect> result = new AtomicReference<MacroEffect>();
        alert.setTitle("CHOOSE ONE MACRO");
        alert.setHeaderText("Select a macro");
        GridPane box = new GridPane();
        int i = 0;
        for(MacroEffect macroEffect : weapon.getMacroEffects()){
            TextArea text = new TextArea();
            text.setPrefWidth(300.0);
            text.setText(macroEffect.toString());
            text.setWrapText(true);
            text.setEditable(false);

            Button button = new Button();
            button.setText("MACRO #" + i);
            button.setAlignment(Pos.CENTER);
            int finalI = i;
            button.setOnMousePressed(e -> {
                alert.close();
                result.set(weapon.getMacroEffects().get(finalI));
            });

            box.add(text,i,0);
            box.add(button,i,1);
            i++;
        }
        alert.getDialogPane().setContent(box);
        alert.setOnCloseRequest(e -> alert.close());
        alert.showAndWait();
        return result.get();
    }

    /**
     *{@link InputAbstract#askPlayers(int)} (Weapon)}
     * {@link InputAbstract#askRooms(int)}
     */
    public List<String> askPlayersOrRooms(int maxTargetPlayerSize, boolean askedPlayers) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setGraphic(null);
        String playersOrRooms = askedPlayers ? "players" : "rooms";
        List<String> result = new ArrayList<>();
        List<CheckBox> checkBoxList = new ArrayList<>();
        alert.setTitle("CHOOSE PLAYERS");
        alert.setHeaderText("Select up to " + maxTargetPlayerSize + " " + playersOrRooms + " then press OK");

        VBox box = new VBox();
        if(askedPlayers){
            List<GameCharacter> characters = this.model.getGameBoard().getResult().getActiveCharacters();
            for(GameCharacter character: characters){
                if(model.getClientID() != character.getConcretePlayer().getClientID()) {
                    CheckBox choice = new CheckBox(character.getConcretePlayer().getName());
                    box.getChildren().add(choice);
                    checkBoxList.add(choice);
                }
            }
        }else{
            List<Room> rooms = this.model.getGameBoard().getResult().getMap().getRooms();
            for(Room room: rooms){
                CheckBox choice = new CheckBox(room.getColor().toString());
                box.getChildren().add(choice);
                checkBoxList.add(choice);
            }
        }

        alert.getDialogPane().setContent(box);
        alert.setOnCloseRequest(e -> {
            for(CheckBox checkBox : checkBoxList){
                if(checkBox.isSelected()){
                    result.add(checkBox.getText());
                }
            }
            if(result.size() < maxTargetPlayerSize)alert.close();
        });
        alert.showAndWait();
        return result;
    }

    /**
     *{@link InputAbstract#askSquares(int)}
     */
    public List<SquareInfo> askSquares(int maxSquares) {
        int iteration = 0;
        List<SquareInfo> result = new ArrayList<>();
        Stage empytStage = new Stage(StageStyle.TRANSPARENT);
        empytStage.initModality(Modality.NONE);

        while(iteration < maxSquares){
            logText("Select a square!");
            for(GuiTile tile : tiles){
                tile.setOnMousePressed(e -> {
                    result.add(new SquareInfo(tile.getRow(),tile.getCol()));
                    for(GuiTile tileToDisable : tiles){
                        tileToDisable.setOnMousePressed(null);
                    }
                    empytStage.close();
                });
            }
            iteration++;
        }
        empytStage.showAndWait();
        return result;
    }

    /**
     *{@link InputAbstract#getMoveChoice()}
     */
    public boolean getMoveChoice() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Move before shoot");
        alert.setHeaderText("Do you want to move a square before shooting?");
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    /**
     *{@link InputAbstract#askPowerUps()}
     */
    public List<PowerUpCard> askPowerups() {
        List<PowerUpCard> listToAsk = this.model.getPlayerHand().getPowerupHand();
        AtomicReference<List<PowerUpCard>> result = new AtomicReference<>();
        if(!listToAsk.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setGraphic(null);
            if (listToAsk.isEmpty()) return Collections.emptyList();
            List<CheckBox> checkBoxList = new ArrayList<>();
            alert.setTitle("CHOOSE POWERUPS");
            alert.setHeaderText("Select optional powerups to use as ammo");

            VBox box = new VBox();
            alert.getDialogPane().setContent(box);

            for (PowerUpCard card : listToAsk) {
                CheckBox choice = new CheckBox(card.getName() + " - " + card.getColor().toString());
                choice.setId(String.valueOf(card.getCardId()));
                box.getChildren().add(choice);
                checkBoxList.add(choice);
            }

            alert.setOnCloseRequest(e -> {
                result.set(getCheckBoxInput(checkBoxList, listToAsk));
            });
            alert.showAndWait();
        }else {
            return Collections.emptyList();
        }
        return result.get();
    }

    /**
     *{@link InputAbstract#askTargetingScopes()}
     */
    public List<ScopePack> askTargetingScopes() {
        List<ScopePack> result = new ArrayList<>();
        if(model != null && !model.getPlayerHand().getPlayerHand().getTargetingScopes().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setGraphic(null);
            List<PowerUpCard> listToAsk = this.model.getPlayerHand().getPlayerHand().getTargetingScopes();
            List<CheckBox> checkBoxList = new ArrayList<>();
            alert.setTitle("CHOOSE TARGETING SCOPES");
            alert.setHeaderText("Select optional targeting scopes to use:");

            VBox box = new VBox();
            for(PowerUpCard card : listToAsk){
                CheckBox choice = new CheckBox("Targeting scope - " + card.getColor().toString());
                choice.setId(String.valueOf(card.getCardId()));
                box.getChildren().add(choice);
                checkBoxList.add(choice);
            }

            alert.getDialogPane().setContent(box);
            alert.setOnCloseRequest(e -> {
                for(CheckBox checkBox : checkBoxList){
                    if(checkBox.isSelected()){
                        String id = checkBox.getId();
                        for(PowerUpCard card : listToAsk){
                            if(Integer.valueOf(id) == card.getCardId()){
                                result.add(this.input.manageScope(card));
                            }
                        }
                    }
                }
            });
            alert.showAndWait();
        }else{
            return Collections.emptyList();
        }
        return result;
    }

    /**
     *{@link GuiInput#manageScope(PowerUpCard)} )}
     */
    public Color chooseAmmoColor() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setGraphic(null);
        AtomicReference<Color> result = new AtomicReference<>();
        ToggleGroup group = new ToggleGroup();
        alert.setTitle("CHOOSE AMMO TO USE");
        alert.setHeaderText("Select ammo color to use");

        VBox box = new VBox();
        for(Color color: Color.values()){
            if(color.isAmmoColor()) {
                RadioButton choice = new RadioButton(color.toString());
                box.getChildren().add(choice);
                choice.setToggleGroup(group);
            }
        }

        alert.getDialogPane().setContent(box);
        alert.setOnCloseRequest(e -> {
            RadioButton selectedToggle = (RadioButton) group.getSelectedToggle();
            result.set(Color.fromString(selectedToggle.getText()));
        });
        alert.showAndWait();
        return result.get();
    }

    public void setInput(GuiInput input) {
        this.input = input;
    }

    /**
     *Method that asks the player if he wants to use a tagback Grenade if he has one.
     */
    void handleGrenade() {
        if(this.model.getPlayerHand().getPlayerHand().getNumberOfTagbacks() > 0) {
            List<PowerUpCard> grenadeList = new ArrayList<>();
            AtomicReference<List<PowerUpCard>> activatedGrenades = new AtomicReference<>();
            for (PowerUpCard card : this.model.getPlayerHand().getPowerupHand()) {
                if (card.getPowerUp() instanceof TagbackGrenade) grenadeList.add(card);
            }
            if (!grenadeList.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setGraphic(null);
                List<CheckBox> checkBoxList = new ArrayList<>();
                alert.setTitle("CHOOSE TAGBACK GRENADES");
                alert.setHeaderText("Select, if you want, the grenades to use:");

                VBox box = new VBox();
                for (PowerUpCard card : grenadeList) {
                    CheckBox choice = new CheckBox(card.getName() + " - " + card.getColor());
                    choice.setId(String.valueOf(card.getCardId()));
                    checkBoxList.add(choice);
                    box.getChildren().add(choice);
                }

                alert.setOnCloseRequest(e -> activatedGrenades.set(getCheckBoxInput(checkBoxList, grenadeList)));
                alert.getDialogPane().setContent(box);
                alert.showAndWait();
                List<Info> infoList = new ArrayList<>();
                for (PowerUpCard card : activatedGrenades.get()) {
                    infoList.add(actionParser.createPowerUpEvent(card));
                    logText("Grenade used\n");
                }
                for (Info info : infoList) {
                    this.gui.getConnection().sendAsynchronous(info);
                }
            }
        }
        this.model.setPlayTagback(false);
        this.gui.getConnection().sendAsynchronous(new TagbackStopInfo());
    }

    private List<PowerUpCard> getCheckBoxInput(List<CheckBox> checkBoxes, List<PowerUpCard> listToAsk){
        List<PowerUpCard> result = new ArrayList<>();
        for(CheckBox choice : checkBoxes) {
            if (choice.isSelected()) {
                String id = choice.getId();
                for (PowerUpCard card : listToAsk) {
                    if (Integer.valueOf(id) == card.getCardId()) {
                        result.add(card);
                    }
                }
            }
        }
        return result;
    }

    /**
     * Method called when the Reload Button is pressed, it sends the info to the server.
     * If the player doesn't have a weapon to reload or doesn't want to reload any weapon
     * a empyt info is sent.
     */
    public void reload() {
        this.gui.getConnection().send(askReload());
    }

    private List<WeaponCard> askWeaponsToReload() {
        List<WeaponCard> result = new ArrayList<>();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setGraphic(null);
        alert.setTitle("RELOAD");
        alert.setHeaderText("Choose weapons to reload");

        VBox box = new VBox();
        List<CheckBox> checkBoxList = new ArrayList<>();
        for(WeaponCard card : this.model.getPlayerHand().getWeaponHand()){
            if(!card.isReady()){
                CheckBox choice = new CheckBox(card.getName());
                checkBoxList.add(choice);
                choice.setId(String.valueOf(card.getId()));
                box.getChildren().add(choice);
            }
        }

        alert.setOnCloseRequest(e -> {
            for(CheckBox choice : checkBoxList){
                if(choice.isSelected()){
                    String id = choice.getId();
                    for(WeaponCard card : this.model.getPlayerHand().getWeaponHand()){
                        if(card.getId() == Integer.valueOf(id)) result.add(card);
                    }
                }
            }
        });

        alert.getDialogPane().setContent(box);
        alert.showAndWait();
        return result;
    }

    /**
     * Shows a popup to the client in case of soft disconnection from the server,
     * the player can reconnect by pressing the ok button
     */
    void disconnectPopupShow() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setGraphic(null);
        alert.setTitle("DISCONNECTED");
        alert.setContentText("YOU HAVE BEEN DISCONNECTED FOR INACTIVITY, PRESS OK TO RECONNECT");

        alert.setOnCloseRequest(e -> {
            this.gui.getConnection().send(new ReconnectInfo(this.gui.getConnection().getClientID()));
        });
        alert.showAndWait();
    }

    public Info askReload() {
        boolean notAllReady = false;
        for(WeaponCard card : model.getPlayerHand().getWeaponHand()){
            if(!card.isReady())notAllReady = true;
        }
        if(notAllReady){
            List<WeaponCard> weaponCards = askWeaponsToReload();
            List<PowerUpCard> powerUpCards;
            if(!weaponCards.isEmpty()){
                powerUpCards = askPowerups();
            }else{
                powerUpCards = Collections.emptyList();
            }
            return actionParser.createReloadEvent(weaponCards,powerUpCards);

        } else {
            return actionParser.createReloadEvent(Collections.emptyList(),Collections.emptyList());
        }
    }

    /**
     * Enables the spawn by pressing on a powerup
     */
    void enableSpawn() {
        for(Node card : powerupHand.getChildren()){
            card.setDisable(false);
        }
    }

    /**
     * Shows a popup notifying the client of the end of the match.
     * On close request the client is closed
     */
    void gameOver() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("GAME OVER");
        alert.setHeaderText("Game has ended, the winner is: " + this.model.getGameOverAnswer().getWinner().getName()  + ", thank you for playing!");
        alert.setContentText(null);
        alert.setGraphic(null);
        alert.setOnCloseRequest(e -> System.exit(0));
        alert.showAndWait();
    }

    @Override
    public void addGui(UpdaterGui updaterGui) {
        this.gui = updaterGui;
    }

    /**
     * Method called when the server disconnects
     */
    public void close() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("SERVER DISCONNECTED");
        alert.setHeaderText("The server went offline.");
        alert.setContentText(null);
        alert.setGraphic(null);
        alert.setOnCloseRequest(e -> System.exit(1));
        alert.showAndWait();
    }
}
