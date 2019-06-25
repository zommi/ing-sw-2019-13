package client.gui;

import client.ConnectionRMI;
import client.ConnectionSocket;
import client.SetupInfo;
import client.gui.guielements.GuiController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import server.model.player.Figure;

import java.rmi.RemoteException;

public class StartMenuController implements GuiController {

    /*
    @FXML
    private StackPane backStackPane;

    @FXML
    private ImageView background;

    @FXML
    private StackPane forwardStackPane;

    @FXML
    private GridPane mainGridPane;

    @FXML
    private GridPane buttonGridPane;

    @FXML
    private GridPane inputGridPane;

    @FXML
    private
    */

    @FXML
    private Button createButton;

    @FXML
    private Button connectButton;

    @FXML
    private ChoiceBox connectionBox;

    @FXML
    private TextField nameField;

    @FXML
    private ChoiceBox characterBox;

    private UpdaterGui gui;


    @FXML
    public void initialize(){
        init();
    }

    public void createGame(){
        this.gui.changeStage("map_selection.fxml");
    }

    public void connect(){
        this.gui.changeStage("loading_screen.fxml");
        SetupInfo setupInfo = new SetupInfo();
        setupInfo.setCharacterName(this.gui.getCharacter());
        this.gui.getConnection().send(setupInfo);
    }

    public void setGui(UpdaterGui updaterGui) {
        this.gui = updaterGui;
    }

    @Override
    public void addGui(UpdaterGui updaterGui) {
        this.gui = updaterGui;
    }

    @Override
    public void init() {
        connectionBox.getItems().addAll("RMI","SOCKET");
        characterBox.getItems().addAll("DESTRUCTOR","VIOLET","BANSHEE","DOZER","SPROG");
    }

    public void start(MouseEvent mouseEvent) throws RemoteException{
        this.gui.setPlayerName(this.nameField.getText());
        this.gui.setConnection(
                this.connectionBox.getValue().toString().equals("RMI") ?
                        new ConnectionRMI(0) :
                        new ConnectionSocket(0)
        );
        this.gui.setModel();
        this.gui.attachToObserver();
        this.gui.getConnection().configure(gui.getPlayerName());
        this.gui.setCharacter((String)this.characterBox.getValue());
    }

    @Override
    public void enableAll() {
    }

    @Override
    public void disableAll() {

    }

}
