package client.gui;

import client.GameModel;
import client.Updater;
import exceptions.GameAlreadyStartedException;
import exceptions.NotEnoughPlayersException;
import javafx.application.Platform;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Observable;

public class UpdaterGUI implements Runnable, Updater {

    private MainGui gui;

    private GameModel model;

    public UpdaterGUI(MainGui gui){
        this.gui = gui;
    }

    @Override
    public void update(Observable gameModel, Object object) {
        if(object.equals("GameBoard")){

        }

        if(object.equals("Map initialized")){
            System.out.println("Map initialized");
            this.gui.getCurrentController().init();
        }

        if(object.equals("Map")){
            System.out.println("Map received");
            Platform.runLater(() -> {
                this.gui.getControllerFromString("gui.fxml").init();
                this.gui.changeStage("gui.fxml");});
        }

        if(object.equals("PlayerBoard")){

        }

        if(object.equals("PlayerHand")){

        }
    }

    @Override
    public void set() throws NotBoundException, RemoteException, NotEnoughPlayersException, GameAlreadyStartedException {

    }

    @Override
    public void run() {

    }

    public void attachToObserver(){
        this.model.addObserver(this);
    }

    public void setModel(GameModel gameModel) {
        this.model = gameModel;
    }

    public GameModel getGameModel() {
        return model;
    }

    public MainGui getGui() {
        return gui;
    }

}
