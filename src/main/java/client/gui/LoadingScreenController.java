package client.gui;

public class LoadingScreenController implements GuiController{

    private UpdaterGUI gui;

    @Override
    public void addGui(UpdaterGUI updaterGUI) {
        this.gui = updaterGUI;
    }

}
