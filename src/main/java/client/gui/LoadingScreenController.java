package client.gui;

import client.gui.guielements.GuiController;

public class LoadingScreenController implements GuiController {

    private UpdaterGui gui;

    @Override
    public void addGui(UpdaterGui updaterGui) {
        this.gui = updaterGui;
    }

    @Override
    public void init() {

    }

    @Override
    public void enableAll() {
    }

    @Override
    public void disableAll() {

    }

}
