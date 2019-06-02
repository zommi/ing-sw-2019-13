package client.gui.guielements;

import client.gui.UpdaterGui;

public interface GuiController {

    public void addGui(UpdaterGui updaterGui);

    void init();

    void enableAll();

    void disableAll();

}
