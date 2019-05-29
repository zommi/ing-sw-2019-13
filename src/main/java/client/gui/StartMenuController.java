package client.gui;

import client.ConnectionRMI;
import client.ConnectionSocket;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

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

    private MainGui gui;


    @FXML
    public void initialize(){
        init();
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
        this.gui.setCharacter((String)this.characterBox.getValue());
        this.gui.setModel();
        this.gui.attachToObserver();
    }

    public void setGui(MainGui mainGui) {
        this.gui = mainGui;
    }

    @Override
    public void addGui(MainGui mainGui) {
        this.gui = mainGui;
    }

    @Override
    public void init() {
        connectionBox.getItems().addAll("RMI","SOCKET");
        characterBox.getItems().addAll("DESTRUCTOR","VIOLET","BANSHEE","DOZER","SPROG");
    }

    public void connectGame(MouseEvent mouseEvent) throws RemoteException{

        this.gui.setPlayerName(this.nameField.getText());
        this.gui.setConnection(
                this.connectionBox.getValue().toString().equals("RMI") ?
                        new ConnectionRMI(0) :
                        new ConnectionSocket(0)
        );
        this.gui.changeStage("loading_screen.fxml");
        this.gui.setModel();
        this.gui.attachToObserver();
        this.gui.getConnection().configure();
        this.gui.setCharacter((String)this.characterBox.getValue());
        this.gui.setInitialSkulls(this.gui.getConnection().getInitialSkulls());
        String mapName;
        int initialSkulls;
        this.gui.getConnection().add(this.gui.getPlayerName(), 0,0);

    }


}
