package client.gui;

import client.GameModel;
import client.Updater;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Observable;

public class UpdaterGUI extends Updater {
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
