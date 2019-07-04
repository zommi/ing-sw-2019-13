package client.gui;

import client.ConnectionRMI;
import client.ConnectionSocket;
import client.info.SetupInfo;
import client.gui.guielements.GuiController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.rmi.RemoteException;

public class StartMenuController implements GuiController {

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

    /**
     * Method called by UpdaterGui when there is a lobby waiting for players.
     */
    public void connect(){
        this.gui.changeStage("loading_screen.fxml");
        SetupInfo setupInfo = new SetupInfo();
        setupInfo.setCharacterName(this.gui.getCharacter());
        this.gui.getConnection().send(setupInfo);
    }

    /**
     * Method called to initialize the client with standard values.
     */
    @Override
    public void init() {
        connectionBox.getItems().addAll("RMI","SOCKET");
        characterBox.getItems().addAll("DESTRUCTOR","VIOLET","BANSHEE","DOZER","SPROG");
    }

    /**
     * Event activated when the "Start" button is pressed, it connects the client to the
     * server and based on the answer received it will either open the map
     * selection menu if it's the first player connecting, or it will open
     * a loading menu waiting for the game to start
     * @param mouseEvent The event starting the method
     * @throws RemoteException Throws remote exception it the client
     * is not able to connect to the RMI server
     */
    public void start(MouseEvent mouseEvent) throws RemoteException{
        this.gui.setPlayerName(this.nameField.getText());
        this.gui.setConnection(
                this.connectionBox.getValue().toString().equals("RMI") ?
                        new ConnectionRMI() :
                        new ConnectionSocket()
        );
        this.gui.setModel();
        this.gui.attachToObserver();
        this.gui.getConnection().configure(gui.getPlayerName());
        this.gui.setCharacter((String)this.characterBox.getValue());
    }

    /**
     * Null method because there's no need to enable all buttons
     */
    @Override
    public void enableAll() { }

    /**
     * Null method because there's no need to disable all buttons
     */
    @Override
    public void disableAll() { }

    public void setGui(UpdaterGui updaterGui) {
        this.gui = updaterGui;
    }

    @Override
    public void addGui(UpdaterGui updaterGui) {
        this.gui = updaterGui;
    }
}
