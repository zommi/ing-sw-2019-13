package client.gui;

import client.info.SetupInfo;
import client.gui.guielements.GuiController;
import constants.Constants;
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

    /**
     * Method that initializes the ChoiceBox
     */
    @Override
    public void init() {
        for(int i = Constants.MIN_SKULLS; i <= Constants.MAX_SKULLS; i++) {
            this.skullBox.getItems().add(i);
        }
    }

    /**
     * Enables the pressing of the maps.
     */
    @Override
    public void enableAll() {
        for(Node map : mapGridPane.getChildren()){
            map.setDisable(false);
        }
        this.skullBox.setDisable(false);
    }

    /**
     * Disables the pressing of the maps.
     */
    @Override
    public void disableAll() {
        for(Node map : mapGridPane.getChildren()){
            map.setDisable(true);
        }
        this.skullBox.setDisable(true);
    }

    /**
     * Event launched when a map is pressed. It sends the map chosen and
     * the number of skulls to the server
     * @param event The pressing of the button
     */
    @FXML
    void selectMap(MouseEvent event) {
        if(skullBox.getValue() != null){
            for(Node map : mapGridPane.getChildren()){
                if(map.getId() != null && map instanceof ImageView && event.getSource().equals(map)) {
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

    public void initialize(){
        init();
    }

}
