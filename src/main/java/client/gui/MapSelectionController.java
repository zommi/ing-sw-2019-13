package client.gui;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;


public class MapSelectionController implements GuiController {

    @FXML
    private GridPane mapGridPane;

    @FXML
    private ChoiceBox skullBox;

    private UpdaterGUI gui;


    @Override
    public void addGui(UpdaterGUI updaterGUI) {
        this.gui = updaterGUI;
    }

    public void initialize(){
        this.skullBox.getItems().addAll(5,6,7,8);
    }

    @FXML
    void selectMap(MouseEvent event) {
        if(skullBox.getValue() != null){
            for(int i = 0; i < 4; i++){
                if(event.getSource().equals(this.mapGridPane.getChildren().get(i))){
                    this.gui.setMapIndex(i);
                    break;
                }
            }
            this.gui.setInitialSkulls((int)skullBox.getValue());
            this.gui.getConnection().configure();
            this.gui.getConnection().add(this.gui.getPlayerName(),this.gui.getMapIndex(),this.gui.getInitialSkulls());
            this.gui.changeStage("loading_screen.fxml");
        }
    }

}
