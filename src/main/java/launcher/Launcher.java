package launcher;

import client.cli.UpdaterCLI;
import client.gui.UpdaterGui;
import constants.Constants;
import server.Server;

import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Properties;

public class Launcher {
    public int i;

    public int getI() {
        return i;
    }

    public static void main(String[] args){
        if(args.length == 1){
            switch (args[0]) {
                case "server":
                    try {
                        Server.main(null);
                    } catch (RemoteException e) {
                        break;
                    }
                    break;
                case "gui":
                    UpdaterGui.main(null);
                    break;
                case "cli":
                    UpdaterCLI.main(null);
                    break;
                default: break;
            }
        }
    }
}
