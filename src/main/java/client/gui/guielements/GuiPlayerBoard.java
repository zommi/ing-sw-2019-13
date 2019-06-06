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
import view.PlayerBoardAnswer;

public class GuiPlayerBoard extends StackPane {

    private final int COLUMN = 3;

    private final int ROW = 2;

    public GuiPlayerBoard(PlayerBoardAnswer playerBoard){
        super();
        this.getStylesheets().add("style.css");
        this.setWidth(600.0);
        this.setHeight(200.0);
        BackgroundFill backgroundFill = new BackgroundFill(
                Paint.valueOf(Color.fromCharacter(playerBoard.getCharacterName()).getPlayerboardCol()),
                new CornerRadii(10.0),
                new Insets(5.0,5.0,5.0,5.0));
        Background background = new Background(backgroundFill);
        this.setBackground(background);

        GridPane mainGridpane = new GridPane();
        mainGridpane.setPrefWidth(600.0);
        mainGridpane.setPrefHeight(200.0);
        mainGridpane.setPadding(new Insets(5.0,5.0,5.0,5.0));
        for(int i = 0; i < COLUMN; i++){
            mainGridpane.getColumnConstraints().add(new ColumnConstraints(600.0 / COLUMN));
        }
        for(int i = 0; i < ROW; i++){
            mainGridpane.getRowConstraints().add(new RowConstraints(200.0 / ROW));
        }


        GridPane hpGrid = new GridPane();
        hpGrid.setPrefWidth(180.0);
        hpGrid.setPrefHeight(15.0);
        hpGrid.setAlignment(Pos.CENTER_LEFT);
        for(int i = 0; i < playerBoard.getResult().getDamageTaken(); i++){
            hpGrid.add(new Circle(7,Paint.valueOf(playerBoard.getResult().getDamage()[i].getNormalColor())),i,0);
        }
        Label hpLabel = new Label("HP");
        hpGrid.add(hpLabel,playerBoard.getResult().getDamageTaken(),0);

        GridPane marksGrid = new GridPane();
        marksGrid.setPrefWidth(45.0);
        marksGrid.setPrefHeight(15.0);
        marksGrid.setAlignment(Pos.CENTER_LEFT);
        for (int i = 0; i < playerBoard.getResult().getMarks().size() ; i++){
            marksGrid.add(new Circle(7,Paint.valueOf(playerBoard.getResult().getMarks().get(i).getNormalColor())),i,0);
        }
        Label marksLabel = new Label("MARKS");
        marksGrid.add(marksLabel,playerBoard.getResult().getMarks().size(),0);

        /*GridPane pointGrid = new GridPane();
        for(int i = 0; i < Constants.POINT_VALUE.length; i++){
            pointGrid.add(new Label(((Integer) Constants.POINT_VALUE[i]).toString()),i,0);
        }
        for(int i = 0; i < playerBoard.getResult().)*/




        mainGridpane.add(hpGrid,0,1);
        mainGridpane.add(marksGrid,0,2);
        this.getChildren().add(mainGridpane);
    }
}
