package client.gui.guielements;

import constants.Constants;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import view.PlayerBoardAnswer;

public class GuiPlayerBoard extends StackPane {

    private final int COLUMN = 3;

    private final int ROW = 2;

    public GuiPlayerBoard(PlayerBoardAnswer playerBoard){
        super();
        this.setWidth(600.0);
        this.setHeight(200.0);

        GridPane mainGridpane = new GridPane();
        mainGridpane.setPrefWidth(600.0);
        mainGridpane.setPrefHeight(200.0);
        for(int i = 0; i < COLUMN; i++){
            mainGridpane.getColumnConstraints().add(new ColumnConstraints(200.0));
        }
        for(int i = 0; i < ROW; i++){
            mainGridpane.getRowConstraints().add(new RowConstraints(100.0));
        }

        GridPane hpGrid = new GridPane();
        hpGrid.setPrefWidth(180.0);
        hpGrid.setPrefHeight(15.0);
        hpGrid.setGridLinesVisible(true);
        hpGrid.setAlignment(Pos.CENTER_LEFT);
        for(int i = 0; i < Constants.MAX_HP ; i++){
            hpGrid.getColumnConstraints().add(new ColumnConstraints(15.0));
        }

        GridPane marksGrid = new GridPane();
        marksGrid.setPrefWidth(45.0);
        marksGrid.setPrefHeight(15.0);
        marksGrid.setGridLinesVisible(true);
        marksGrid.setAlignment(Pos.CENTER_LEFT);
        for (int i = 0; i < Constants.MAX_MARKS ; i++){
            mainGridpane.getColumnConstraints().add(new ColumnConstraints(15.0));
        }


        mainGridpane.add(hpGrid,0,0);
        mainGridpane.add(marksGrid,1,0);
        this.getChildren().add(mainGridpane);
    }
}
