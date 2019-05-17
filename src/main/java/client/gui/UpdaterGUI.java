package client.gui;

import client.GameModel;
import client.Updater;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Observable;

public class UpdaterGUI extends Application implements Updater {



    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/gui.fxml"));
        primaryStage.setTitle("ADRENALINE.EXE");
        primaryStage.setScene(new Scene(root, 1200, 800));
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void update(Observable gameModel, Object object){
        if(object.equals("GameBoard")){

        }

        if(object.equals("Map")){

        }

        if(object.equals("PlayerBoard")){

        }

        if(object.equals("PlayerHand")){

        }
    }

    public void set(){

    }

    public void run(){

    }
}
