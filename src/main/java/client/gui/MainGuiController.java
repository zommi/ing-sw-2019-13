package client.gui;

import client.GameModel;
import constants.Direction;
import exceptions.NoSuchSquareException;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import server.model.map.GameMap;
import server.model.map.SpawnPoint;
import server.model.map.SquareAbstract;

public class MainGuiController implements GuiController{

    @FXML
    private GridPane weaponHand;

    @FXML
    private GridPane powerupHand;

    @FXML
    private GridPane buttonGrid;

    @FXML
    private StackPane mapStackPane;

    @FXML
    private ScrollPane map_scrollPane;

    @FXML
    private ScrollPane chat;

    private GameModel model;

    boolean iteration = true;

    private int weaponHandSize = 0;

    private MainGui gui;

    private int side = 175;

    @FXML
    void drawWeapon(MouseEvent event, GuiWeaponCard weaponCard) {
        if(weaponHandSize == 3) return;
        GuiWeaponCard cardToAdd = weaponCard;

        cardToAdd.setCursor(Cursor.HAND);
        cardToAdd.setFitWidth(weaponHand.getWidth()/3);
        cardToAdd.setFitHeight(weaponHand.getHeight());

        cardToAdd.setOnMousePressed(null);
        this.weaponHand.add(cardToAdd,weaponHandSize,0);
        weaponHandSize++;
    }

    public void initializeMap() throws NoSuchSquareException { //NOSONAR
        this.model = gui.getGameModel();
        GameMap map = model.getMap();
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
                GuiSquare square = new GuiSquare(side,side,Paint.valueOf(currentSquare.getColor().getSpHexValue()));
                square.setCursor(Cursor.HAND);
                square.setOnMousePressed(e -> drawAmmoTile(square));
                mapGridPane.add(
                        square,
                        currentSquare.getCol()*2,
                        currentSquare.getRow()*2);
            } else {
                GuiSpawnPoint spawnPoint = new GuiSpawnPoint(side,side,Paint.valueOf(currentSquare.getColor().getSpHexValue()));
                spawnPoint.setCursor(Cursor.HAND);
                mapGridPane.add(
                        spawnPoint,
                        currentSquare.getCol()*2,
                        currentSquare.getRow()*2);
                spawnPoint.setOnMousePressed(e -> showWeapons(spawnPoint));
                spawnPoint.setCardsOnSpawnPoint((SpawnPoint) currentSquare);
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

    private void drawAmmoTile(GuiSquare square) {
        square.drawAmmo();
    }

    private void showWeapons(GuiSpawnPoint spawnPoint) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("WEAPONS AVAILABLE");

        GridPane cards = new GridPane();
        for(int i = 0; i < 3; i ++){
            GuiWeaponCard weapon = new GuiWeaponCard(
                    getClass().getResource(spawnPoint.getCardsOnSpawnPoint().get(i)).toExternalForm(),i);
            weapon.setOnMousePressed(e -> {
                if(weaponHandSize < 3){
                    drawWeapon(e,weapon);
                    // ask server to draw card
                    // String cardToDraw = model.getGameBoard().getWeaponDeck().draw().getPath();
                    spawnPoint.getCardsOnSpawnPoint().remove(weapon.getIndex());
                    // add the card draw to the spawnpoint
                    // spawnPoint.restore(cardToDraw,weapon.getIndex());
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

    @Override
    public void addGui(MainGui mainGui) {
        this.gui = mainGui;
    }

    @Override
    public void init(){
        try {
            initializeMap();
        } catch (NoSuchSquareException e) {
            e.printStackTrace();
            System.out.println("ERROR IN INITIALIZATION");
        }
    }
}
