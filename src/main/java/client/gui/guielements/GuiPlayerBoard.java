package client.gui.guielements;

import constants.Color;
import constants.Constants;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import server.model.cards.WeaponCard;
import server.model.player.PlayerBoard;

public class GuiPlayerBoard extends StackPane {

    private static final int COLUMN = 3;

    private static final int ROW = 2;

    public GuiPlayerBoard(PlayerBoard playerBoard){
        super();
        this.getStylesheets().add("style.css");
        this.setWidth(600.0);
        this.setHeight(100.0);
        BackgroundFill backgroundFill = new BackgroundFill(
                Paint.valueOf(Color.fromCharacter(playerBoard.getCharacterName()).getPlayerboardCol()),
                new CornerRadii(10.0),
                new Insets(1.0,1.0,1.0,1.0));
        Background background = new Background(backgroundFill);
        this.setBackground(background);

        GridPane mainGridpane = new GridPane();
        mainGridpane.setPrefWidth(600.0);
        mainGridpane.setPrefHeight(100.0);
        mainGridpane.setPadding(new Insets(5.0,5.0,5.0,5.0));
        for(int i = 0; i < COLUMN; i++){
            mainGridpane.getColumnConstraints().add(new ColumnConstraints(600.0 / COLUMN));
        }
        for(int i = 0; i < ROW; i++){
            mainGridpane.getRowConstraints().add(new RowConstraints(100.0 / ROW));
        }


        GridPane hpGrid = new GridPane();
        hpGrid.setPrefWidth(180.0);
        hpGrid.setPrefHeight(15.0);
        hpGrid.setAlignment(Pos.CENTER_LEFT);
        for(int i = 0; i < playerBoard.getDamageTaken(); i++){
            StackPane pane = new StackPane();
            Circle token = new Circle(7,Paint.valueOf(playerBoard.getDamage().get(i).getNormalColor()));
            Label label = new Label(String.valueOf(i+1));
            label.setAlignment(Pos.CENTER);
            pane.getChildren().add(token);
            pane.getChildren().add(label);
            hpGrid.add(pane,i,0);
        }
        Label hpLabel = new Label("HP");
        hpGrid.add(hpLabel,playerBoard.getDamageTaken(),0);

        GridPane marksGrid = new GridPane();
        marksGrid.setPrefWidth(45.0);
        marksGrid.setPrefHeight(15.0);
        marksGrid.setAlignment(Pos.CENTER_LEFT);
        for (int i = 0; i < playerBoard.getMarks().size() ; i++){
            marksGrid.add(new Circle(5,Paint.valueOf(playerBoard.getMarks().get(i).getNormalColor())),i,0);
        }
        Label marksLabel = new Label("MARKS");
        marksGrid.add(marksLabel,playerBoard.getMarks().size(),0);

        GridPane ammoGrid = new GridPane();
        ammoGrid.setPadding(new Insets(1.0,1.0,1.0,1.0));
        ammoGrid.setHgap(2.0);
        ammoGrid.setVgap(2.0);

        for(int i = 0; i < playerBoard.getRedAmmo();i++){
            ammoGrid.add(
                    new Rectangle(10.0,10.0,Paint.valueOf(Color.RED.getNormalColor())),
                    i,
                    0
            );
        }
        Label redLabel = new Label("RED AMMO");
        ammoGrid.add(redLabel,Constants.MAX_AMMO_CUBES_PER_COLOR,0);

        for(int i = 0; i < playerBoard.getBlueAmmo();i++){
            ammoGrid.add(
                    new Rectangle(10.0,10.0,Paint.valueOf(Color.BLUE.getNormalColor())),
                    i,
                    1
            );
        }
        Label blueLabel = new Label("BLUE AMMO");
        ammoGrid.add(blueLabel,Constants.MAX_AMMO_CUBES_PER_COLOR,1);

        for(int i = 0; i < playerBoard.getYellowAmmo();i++){
            ammoGrid.add(
                    new Rectangle(10.0,10.0,Paint.valueOf(Color.YELLOW.getNormalColor())),
                    i,
                    2
            );
        }
        Label yellowLabel = new Label("YELLOW AMMO");
        ammoGrid.add(yellowLabel,Constants.MAX_AMMO_CUBES_PER_COLOR,2);

        GridPane unloadedWeaponsGrid = new GridPane();
        int index = 0;
        for(WeaponCard card : playerBoard.getUnloadedWeapons()){
            GuiWeaponCard cardToAdd = new GuiWeaponCard(
                    card,
                    index,
                    getClass().getResource(card.getPath()).toExternalForm());
            cardToAdd.setFitWidth(200.0 / 3.0);
            cardToAdd.setFitHeight(100.0 / 2.0);
            cardToAdd.setPreserveRatio(true);
            unloadedWeaponsGrid.add(cardToAdd,
                    index,
                    0);
            index++;
        }


        mainGridpane.add(hpGrid,0,0);
        mainGridpane.add(marksGrid,0,1);
        mainGridpane.add(ammoGrid,1,0);

        mainGridpane.add(unloadedWeaponsGrid,2,0);
        this.getChildren().add(mainGridpane);
    }
}
