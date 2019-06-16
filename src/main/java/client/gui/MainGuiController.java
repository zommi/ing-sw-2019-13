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
import javafx.animation.PauseTransition;
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
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import server.model.cards.PowerUpCard;
import server.model.cards.TagbackGrenade;
import server.model.cards.WeaponCard;
import server.model.game.Game;
import server.model.map.*;
import server.model.player.GameCharacter;
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

    @FXML
    public Button scoreboardButton;

    private String log;

    @FXML
    private ScrollPane chat;

    private GameModel model;

    private int weaponHandSize = 0;

    private UpdaterGui gui;

    private ActionParser actionParser;

    private int side = 175;

    private List<GuiSquare> squares = new ArrayList<>();

    private List<GuiSpawnPoint> spawnPoints = new ArrayList<>();

    private List<GuiTile> tiles = new ArrayList<>();

    private List<GuiTile> tilesToUpdate = new ArrayList<>();

    private GuiCharacter myCharacter;

    private GuiInput input;

    private Map<Integer,GuiCharacter> idClientGuiCharacterMap = new HashMap<>();
    @Override
    public void init(){
        log = "Game started! \n";
        initializeMap();
        this.actionParser = gui.getActionParser();
        this.actionParser.addGameModel(this.model);
        textLogger.setText(log);
        textLogger.setWrapText(true);
        logText("Welcome " + this.model.getMyPlayer().getName() + ", you chose " + this.gui.getCharacter() + "\n");
        this.textLogger.setEditable(false);
    }

    public void spawn() {
        logText("Choose a powerup and Spawn in that color! \n");
        DrawInfo action = new DrawInfo();
        this.gui.getConnection().send(action);
    }

    public void spawnAfterDeath(){
        logText("You died!\nChoose a powerup and Spawn in that color! \n");
        DrawInfo action = new DrawInfo();
        this.gui.getConnection().sendAsynchronous(action);
    }

    @Override
    public void enableAll() {
        for(GuiTile tile : tiles){
            tile.setDisable(false);
        }
        for(Node buttonNode : buttonGrid.getChildren()){
            buttonNode.setDisable(false);
        }
//        for(Node weaponNode : weaponHand.getChildren()){
//            weaponNode.setDisable(false);
//        }
//        for(Node powerupNode : powerupHand.getChildren()){
//            powerupNode.setDisable(false);
//        }
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
        scoreboardButton.setDisable(false);
        logText("It's not your turn, disabled commands \n");
    }

    private void initializeMap() { //NOSONAR
        this.model = gui.getGameModel();
        GameMap map = model.getMap();
        SquareAbstract currentSquare = map.iterator().next();
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
                        side,side,Paint.valueOf(currentSquare.getColor().getTileColor()));
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

    @FXML
    void drawWeapon(MouseEvent event, GuiWeaponCard weaponCard, GuiWeaponCard cardToDiscard, GuiSpawnPoint sp) {
        if(weaponHandSize == 3) return;
        GuiWeaponCard cardToAdd = weaponCard;

        cardToAdd.setCursor(Cursor.HAND);
        cardToAdd.setFitWidth(weaponHand.getWidth()/3);
        cardToAdd.setFitHeight(weaponHand.getHeight());

        cardToAdd.setOnMousePressed(null);
        //this.weaponHand.add(cardToAdd,weaponHandSize,0);

        //null da cambiare con la WeaponCard da scartare
        //far scegliere anche i powerup con cui pagare eventualmente l'arma

        List<PowerUpCard> powerUpCards = this.model.getPlayerHand().getPowerupHand().isEmpty() ?
                Collections.emptyList() :
                askPowerups();

        Info collectInfo = actionParser.createCollectEvent(sp.getRow(),sp.getCol(),
                weaponCard.getIndex(),
                weaponHandSize<3 ? null : cardToDiscard.getWeaponCard(),
                powerUpCards);
        this.gui.getConnection().send(collectInfo);
        weaponHandSize++;
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
                    if(weaponHandSize < 3){
                        drawWeapon(e,weapon,null,spawnPoint);
                    }else{
                        GuiWeaponCard weaponToDiscard = askWeaponToDiscard();
                        drawWeapon(e,weapon,weaponToDiscard,spawnPoint);
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

    private GuiWeaponCard askWeaponToDiscard() {
        Stage empytStage = new Stage(StageStyle.TRANSPARENT);
        empytStage.initModality(Modality.NONE);
        logText("Choose a weapon to swap\n");
        AtomicReference<GuiWeaponCard> result = new AtomicReference<>();
        for(Node card : weaponHand.getChildren()){
            card.setOnMousePressed(e -> {
                result.set((GuiWeaponCard) card);
                empytStage.close();
            });
        }
        empytStage.showAndWait();
        return result.get();
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
            logText("Select a square to move!\n");
        }else{
            logText("You first need to spawn!\n");
        }
    }

    public void enableCollect(MouseEvent mouseEvent) {
        if(!this.model.getToSpawn()) {
            for (GuiSquare square : squares) {
                square.setOnMousePressed(e -> {
                    Info collectInfo = this.actionParser.createCollectEvent(square.getRow(), square.getCol(), Constants.NO_CHOICE, null, Collections.emptyList());
                    this.gui.getConnection().send(collectInfo);
                    this.tilesToUpdate.add(square);
                });
            }
            for (GuiSpawnPoint sp : spawnPoints) {
                sp.setOnMousePressed(e -> {
                    showWeapons(sp);
                    this.tilesToUpdate.add(sp);
                });
            }
            logText("Select a square!\n");
        } else {
            logText("You first need to spawn!\n");
        }
    }

    public void enableShoot(MouseEvent mouseEvent) {
        if(!this.model.getToSpawn()){
            logText("Select a weapon to use!\n");
            for(Node card : weaponHand.getChildren()){
                card.setDisable(false);
            }
        }else{
            logText("You first need to spawn!\n");
        }
    }

    public void enablePowerup(MouseEvent mouseEvent){
        if(!this.model.getToSpawn()){
            logText("Select a powerup to use! \n");
            for(Node card : powerupHand.getChildren()){
                card.setDisable(false);
            }
        }else{
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
        boolean firstSpawn = myCharacter == null;
        for(GuiSpawnPoint spawnPoint : this.spawnPoints){
            if(card.getColor() == spawnPoint.getColor()){
                if(myCharacter == null) {
                    this.myCharacter = new GuiCharacter(spawnPoint, model.getMyPlayer().getPlayerBoard(), model.getMyPlayer().getName());
                    this.idClientGuiCharacterMap.put(this.gui.getConnection().getClientID(), myCharacter);
                }
                this.gui.getGameModel().setToSpawn(false);
                disableMouseEvent();
                break;
            }
        }
        Info spawn = this.actionParser.createSpawnEvent(card);
        if(firstSpawn){
            this.gui.getConnection().send(spawn);
        }else {
            this.gui.getConnection().sendAsynchronous(spawn);
        }
    }

    @FXML
    public void showScoreboard(MouseEvent mouseEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setGraphic(null);
        alert.setTitle("SCOREBOARD");
        alert.setContentText(null);
        alert.setHeaderText(null);
        alert.setOnCloseRequest(e -> alert.close());
        GridPane box = new GridPane();
        for(int i = 0; i < this.gui.getGameModel().getGameBoard().getCharacterNames().size() ; i++){
            box.add(new GuiPlayerBoard(this.gui.getGameModel().getGameBoard().getPlayerBoard(i)),0,i);
        }
        alert.getDialogPane().setContent(box);
        alert.setOnCloseRequest(e -> alert.close());
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
            cardToAdd = new GuiWeaponCard(card, i, getClass().getResource(card.getPath()).toExternalForm());
            this.weaponHand.add(cardToAdd,i,0);
            cardToAdd.setCursor(Cursor.HAND);
            cardToAdd.setFitWidth(weaponHand.getWidth()/3);
            cardToAdd.setFitHeight(weaponHand.getHeight());
            if(!card.isReady()){
                cardToAdd.setOpacity(0.6);
            }
            GuiWeaponCard finalCardToAdd = cardToAdd;
            cardToAdd.setOnMousePressed(e -> {
                Info info = this.actionParser.createShootEvent(finalCardToAdd.getWeaponCard().getWeapon());
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
            powerupCard.setFitWidth(powerupHand.getWidth()/3);
            powerupCard.setFitHeight(powerupHand.getHeight());

            int finalI = i;
            GuiPowerupCard finalPowerupCard = powerupCard;
            powerupCard.setOnMousePressed(e -> {
                GuiPowerupCard defaultCard = new GuiPowerupCard(
                        getClass().getResource("/Grafica/cards/AD_powerups_IT_02.png").toExternalForm(),
                        finalI);
                defaultCard.setOnMousePressed(null);
                defaultCard.setCursor(Cursor.DEFAULT);
                defaultCard.setFitWidth(powerupHand.getWidth() / 3);
                defaultCard.setFitHeight(powerupHand.getHeight());
                if(model.getToSpawn()){
                    setSpawn(card);
                }else{
                    Info info = this.actionParser.createPowerUpEvent(finalPowerupCard.getCard());
                    this.gui.getConnection().send(info);
                    for(Node node : powerupHand.getChildren()) {
                        node.setOnMousePressed(null);
                    }
                }
                powerupHand.add(defaultCard, finalI, 0);
            });
            if(!model.getToSpawn())powerupCard.setDisable(true);
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
        for(int i = 0; i < Constants.MAX_POWERUP_HAND; i++){
            GuiPowerupCard powerupCard = new GuiPowerupCard(
                    getClass().getResource("/Grafica/cards/AD_powerups_IT_02.png").toExternalForm(), i);
            powerupCard.setFitWidth(powerupHand.getWidth()/3);
            powerupCard.setFitHeight(powerupHand.getHeight());
            this.powerupHand.add(powerupCard, i, 0);
        }
    }

    //metodo che gestisce la visualizzazione di giocatori e ammotile
    public void updateGameboard() {
        if(this.model != null) {
            //update position of players
            for (int i = 0; i < this.model.getPlayersNames().size(); i++) {
                if (this.idClientGuiCharacterMap.get((Object) i) == null) {
                    this.idClientGuiCharacterMap.put(i, spawnEnemy(this.model.getPlayerBoard(i),model.getPlayersNames().get(i)));
                } else {
                    this.idClientGuiCharacterMap.get(i).setPosition(getTile(
                            this.model.getPlayerBoard(i).getRow(),
                            this.model.getPlayerBoard(i).getCol()
                            ));
                }
            }


        }
    }

    private GuiCharacter spawnEnemy(PlayerBoardAnswer playerBoard, String name) {
        for(GuiSpawnPoint sp : spawnPoints){
            if(sp.getCol() == playerBoard.getCol() && sp.getRow() == playerBoard.getRow()){
                return new GuiCharacter(sp,playerBoard.getResult(),name);
            }
        }
        return null;
    }

    public GuiTile getTile(int row, int col){
        for(GuiTile tile : tiles){
            if(tile.getCol() == col && tile.getRow() == row){
                return tile;
            }
        }
        return null;
    }

    public void restoreSquares() {
        for (GuiTile tile : tiles) {
            tile.setSquare(this.model.getMap().getSquare(tile.getRow(),tile.getCol()));
            tile.restore();
        }
    }

    public boolean askMacro(MacroEffect macroEffect) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("MACRO ACTIVATION CONFIRMATION");
        alert.setHeaderText("Do you wanna activate this macro effect?\n" + macroEffect);
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    public boolean askMicro(MicroEffect microEffect) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("MICRO ACTIVATION CONFIRMATION");
        alert.setHeaderText("Do you wanna activate this micro effect?\n" + microEffect);
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

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
                CheckBox choice = new CheckBox(character.getConcretePlayer().getName());
                box.getChildren().add(choice);
                checkBoxList.add(choice);
            }
        }else{
            List<Room> rooms = this.model.getGameBoard().getResult().getMap().getRooms();
            for(Room room: rooms){
                CheckBox choice = new CheckBox(room.toString());
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

    public boolean getMoveChoice() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Move before shoot");
        alert.setHeaderText("Do you want to move a square before shooting?");
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    public void removeMyCharacter() {
        this.myCharacter = null;
    }

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

    public Color chooseAmmoColor() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setGraphic(null);
        AtomicReference<Color> result = new AtomicReference<>();
        ToggleGroup group = new ToggleGroup();
        alert.setTitle("CHOOSE AMMO TO USE");
        alert.setHeaderText("Select ammo color to use");

        VBox box = new VBox();
        for(Color color: Color.values()){
            RadioButton choice = new RadioButton(color.toString());
            box.getChildren().add(choice);
            choice.setToggleGroup(group);
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

    public void handleGrenade() {
        if(this.gui.getConnection().getClientID() == this.gui.getConnection().getGrenadeID()){
            List<PowerUpCard> grenadeList = new ArrayList<>();
            AtomicReference<List<PowerUpCard>> activatedGrenades = new AtomicReference<>();
            for(PowerUpCard card : this.model.getPlayerHand().getPowerupHand()){
                if(card.getPowerUp() instanceof TagbackGrenade) grenadeList.add(card);
            }
            if(!grenadeList.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setGraphic(null);
                List<CheckBox> checkBoxList = new ArrayList<>();
                alert.setTitle("CHOOSE TAGBACK GRENADES");
                alert.setHeaderText("Select, if you want, the grenades to use:");

                VBox box = new VBox();
                for(PowerUpCard card : grenadeList){
                    CheckBox choice = new CheckBox(card.getName() + " - " + card.getColor());
                    choice.setId(String.valueOf(card.getCardId()));
                    checkBoxList.add(choice);
                    box.getChildren().add(choice);
                }

                alert.setOnCloseRequest(e -> {
                    activatedGrenades.set(getCheckBoxInput(checkBoxList, grenadeList));
                });
                alert.getDialogPane().setContent(box);
                alert.showAndWait();
                List<Info> infoList = new ArrayList<>();
                for(PowerUpCard card : activatedGrenades.get()){
                    infoList.add(actionParser.createPowerUpEvent(card));
                    logText("Grenade used\n");
                }
                this.model.setGrenadeAction(infoList);
            }
            this.model.setClientChoice(true);
        }else{
            logText("Waiting for player to use a tagback grenade\n");
        }
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

    public void reload(MouseEvent mouseEvent) {
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
            this.gui.getConnection().send(actionParser.createReloadEvent(weaponCards,powerUpCards));

        } else {
            this.gui.getConnection().send(actionParser.createReloadEvent(Collections.emptyList(),Collections.emptyList()));
        }
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
                    for(WeaponCard card : this.model.getMyPlayer().getHand().getWeaponHand()){
                        if(card.getId() == Integer.valueOf(id)) result.add(card);
                    }
                }
            }
        });

        alert.getDialogPane().setContent(box);
        alert.showAndWait();
        return result;
    }
}
