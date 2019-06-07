package client.gui;

import client.*;
import client.gui.guielements.*;
import client.weapons.MacroEffect;
import client.weapons.MicroEffect;
import client.weapons.Weapon;
import constants.Color;
import constants.Constants;
import constants.Direction;
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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import server.model.cards.PowerUpCard;
import server.model.cards.WeaponCard;
import server.model.map.GameMap;
import server.model.map.SpawnPoint;
import server.model.map.Square;
import server.model.map.SquareAbstract;
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

    boolean iteration = true;

    private int weaponHandSize = 0;

    private UpdaterGui gui;

    private ActionParser actionParser;

    private int side = 175;

    private List<GuiSquare> squares = new ArrayList<>();

    private List<GuiSpawnPoint> spawnPoints = new ArrayList<>();

    private List<GuiTile> tiles = new ArrayList<>();

    private List<GuiTile> tilesToUpdate = new ArrayList<>();

    private GuiCharacter myCharacter;

    private Map<Integer,GuiCharacter> idClientGuiCharacterMap = new HashMap<>();
    @Override
    public void init(){
        log = "Game started! \n";
        initializeMap();
        this.actionParser = gui.getActionParser();
        this.actionParser.addGameModel(this.model,this.gui.getPlayerName());
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
                    spawnPoint.getCardsOnSpawnPoint().get(i),
                    i,
                    getClass().getResource(spawnPoint.getCardsOnSpawnPoint().get(i).getPath()).toExternalForm());
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
            logText("Select a square to move!\n");
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
                this.myCharacter = new GuiCharacter(spawnPoint,model.getMyPlayer().getPlayerBoard(), model.getMyPlayer().getName());
                this.idClientGuiCharacterMap.put(this.gui.getConnection().getClientID(),myCharacter);
                this.gui.getGameModel().setToSpawn(false);
                disableMouseEvent();
                break;
            }
        }
        Info spawn = this.actionParser.createSpawnEvent(card);
        this.gui.getConnection().send(spawn);
    }

    @FXML
    public void showScoreboard(MouseEvent mouseEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
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


    public void restoreSquares() {
        for (GuiTile tile : tiles) {
            tile.setSquare(this.model.getMap().getSquare(tile.getRow(),tile.getCol()));
            tile.restore();
        }
    }

    public boolean askMacro(MacroEffect macroEffect) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("MACRO ACTIVATION CONFIRMATION");
        alert.setContentText("Do you wanna activate this macro effect?\n" + macroEffect);
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    public boolean askMicro(MicroEffect microEffect) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("MICRO ACTIVATION CONFIRMATION");
        alert.setContentText("Do you wanna activate this micro effect?\n" + microEffect);
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    public MacroEffect chooseOneMacro(Weapon weapon) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        AtomicReference<MacroEffect> result = new AtomicReference<MacroEffect>();
        alert.setTitle("CHOOSE ONE MACRO");
        alert.setContentText("Select a macro");
        alert.setHeaderText(null);
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

    public List<String> askPlayersOrRooms(int maxTargetPlayerSize, List<String> players,List<String> rooms, boolean askedPlayers) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        List<String> listToAsk = askedPlayers ? players : rooms;
        String playersOrRooms = askedPlayers ? "players" : "rooms";
        List<String> result = new ArrayList<>();
        List<CheckBox> checkBoxList = new ArrayList<>();
        alert.setTitle("CHOOSE PLAYERS");
        alert.setContentText("Select up to " + maxTargetPlayerSize + " " + playersOrRooms + " then press OK");

        VBox box = new VBox();
        for(String name : listToAsk){
            CheckBox choice = new CheckBox(name);
            box.getChildren().add(choice);
            checkBoxList.add(choice);
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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        List<SquareInfo> result = new ArrayList<>();
        alert.setTitle("SQUARE SELECTION");
        alert.setHeaderText("Do you want to select a square?");
        ButtonType selectButton = new ButtonType("Select");
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        AtomicInteger squareAsked = new AtomicInteger();

        alert.getButtonTypes().setAll(selectButton,cancelButton);

        while (squareAsked.get() < maxSquares) {
            Optional<ButtonType> choice = alert.showAndWait();
            if (choice.get() == selectButton) {
                synchronized (alert) {
                    alert.close();
                    logText("Select a tile on the map.\n");
                    for (GuiTile tile : tiles) {
                        tile.setOnMousePressed(ev -> {
                            result.add(new SquareInfo(tile.getRow(), tile.getCol()));
                            for (GuiTile tileToDisable : tiles) {
                                tileToDisable.setOnMousePressed(null);
                            }
                        });
                    }
                    try {
                        alert.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    squareAsked.getAndIncrement();
                }
            } else {
                break;
            }
        }

        return result;
//        selectButton.setOnMousePressed(e -> {
//            alert.hide();
//            logText("Select a tile on the map.\n");
//            for(GuiTile tile : tiles){
//                tile.setOnMousePressed( ev -> {
//                    result.add(new SquareInfo(tile.getRow(),tile.getCol()));
//                    for(GuiTile tileToDisable : tiles){
//                        tileToDisable.setOnMousePressed(null);
//                    }
//                    alert.show();
//                });
//            }
//        });
//
//        alert.setOnCloseRequest(e -> {
//            alert.close();
//        });
//
//        alert.getDialogPane().setContent(selectButton);
//        alert.showAndWait();
//        while(squareAsked.get() < maxSquares){
//            synchronized (lock) {
//                try {
//                    alert.wait();
//                } catch (InterruptedException ex) {
//                    ex.printStackTrace();
//                }
//            }
//            squareAsked.getAndIncrement();
//        }
//        return result;
    }
}
