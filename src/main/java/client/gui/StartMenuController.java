package client.gui;

import client.Connection;
import client.ConnectionRMI;
import client.ConnectionSocket;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import server.model.player.Figure;

import java.rmi.RemoteException;

public class StartMenuController implements GuiController{

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

    private UpdaterGUI gui;


    @FXML
    public void initialize(){
        connectionBox.getItems().addAll("RMI","SOCKET");
        characterBox.getItems().addAll("DESTRUCTOR","VIOLET","BANSHEE","DOZER","SPROG");
    }

    @FXML
    public void createGame() throws RemoteException {
        this.gui.setPlayerName(this.nameField.getText());
        this.gui.setConnection(
                this.connectionBox.getValue().toString().equals("RMI") ?
                new ConnectionRMI(0) :
                new ConnectionSocket(0)
        );
        this.gui.changeStage("map_selection.fxml");
        this.gui.setModel();
        this.gui.attachToObserver();
    }

    public void setGui(UpdaterGUI updaterGUI) {
        this.gui = updaterGUI;
    }

    @Override
    public void addGui(UpdaterGUI updaterGUI) {
        this.gui = updaterGUI;
    }

    public void connectGame(MouseEvent mouseEvent) throws RemoteException{
        this.gui.setPlayerName(this.nameField.getText());
        this.gui.setConnection(
                this.connectionBox.getValue().toString().equals("RMI") ?
                        new ConnectionRMI(0) :
                        new ConnectionSocket(0)
        );
        this.gui.setModel();
        this.gui.attachToObserver();
        this.gui.getConnection().configure();
        this.gui.setInitialSkulls(this.gui.getConnection().getInitialSkulls());
        this.gui.setMapIndex(this.gui.getConnection().getMapIndex());
        this.gui.getConnection().add(this.gui.getPlayerName(),this.gui.getMapIndex(),this.gui.getInitialSkulls());
        this.gui.changeStage("loading_screen.fxml");
    }
}
