package client.gui;

import client.SetupInfo;
import client.gui.guielements.GuiController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.ImageView;
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
            for(Node map : mapGridPane.getChildren()){
                if(map.getId() != null && map instanceof ImageView) {
                    this.gui.setMapIndex(Integer.valueOf(map.getId()));
                }
            }
            this.gui.changeStage("loading_screen.fxml");
            this.gui.setInitialSkulls((int)skullBox.getValue());
            SetupInfo setupInfo = new SetupInfo();
            setupInfo.setCharacterName(this.gui.getCharacter());
            setupInfo.setInitialSkulls(this.gui.getInitialSkulls());
            setupInfo.setMapChoice(this.gui.getMapIndex());
            this.gui.getConnection().send(setupInfo);
        }
    }

}
