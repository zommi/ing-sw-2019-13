package client.gui;

import client.gui.guielements.GuiController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;


public class MapSelectionController implements GuiController {

    @FXML
    private GridPane mapGridPane;

    @FXML
    private ChoiceBox skullBox;

    private UpdaterGui gui;


    @Override
    public void addGui(UpdaterGui updaterGui) {
        this.gui = updaterGui;
    }

    @Override
    public void init() {
        this.skullBox.getItems().addAll(5,6,7,8);
    }

    @Override
    public void enableAll() {
        for(Node map : mapGridPane.getChildren()){
            map.setDisable(false);
        }
        this.skullBox.setDisable(false);
    }

    @Override
    public void disableAll() {
        for(Node map : mapGridPane.getChildren()){
            map.setDisable(true);
        }
        this.skullBox.setDisable(true);
    }

    public void initialize(){
        init();
    }

    @FXML
    void selectMap(MouseEvent event) {
        if(skullBox.getValue() != null){
            for(int i = 1; i <= 4 ; i++){ //le mappe vanno da 1 a 4 
                if(event.getSource().equals(this.mapGridPane.getChildren().get(i))){
                    this.gui.setMapIndex(i);
                    break;
                }
            }
            this.gui.changeStage("loading_screen.fxml");
            this.gui.setInitialSkulls((int)skullBox.getValue());
            this.gui.getConnection().configure();
            this.gui.getConnection().add(this.gui.getPlayerName(),this.gui.getMapIndex(),this.gui.getInitialSkulls());
            this.gui.getConnection().addPlayerCharacter(this.gui.getCharacter());
            this.gui.setPlayerId(this.gui.getConnection().getClientID());
            this.gui.getConnection().addPlayerCharacter(this.gui.getCharacter());
        }
    }

}
