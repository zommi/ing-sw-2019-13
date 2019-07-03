package client.gui;

import client.ActionParser;
import client.Connection;
import client.GameModel;
import client.Updater;
import client.gui.guielements.GuiController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class UpdaterGui extends Application implements Updater {

    /**
     * Map used to link Scenes to relative Strings
     */
    private HashMap<String, Scene> sceneMap = new HashMap<>();

    /**
     * Map used to link Controllers to their stage's name
     */
    private HashMap<String, GuiController> controllerMap = new HashMap<>();

    /**
     * Controller for the shown stage
     */
    private GuiController currentController;

    /**
     * Controller for the game stage, it can be used before the game has started
     */
    private MainGuiController mainGuiController;

    /**
     * Reference to the scene shown
     */
    private Scene currentScene;

    private Stage stage;

    private String stageName;

    /**
     * Type of connection chosen by the client
     */
    private Connection connection;

    /**
     * Name chosen by the client
     */
    private String playerName;

    /**
     * Character chosen by the client.
     */
    private String character;

    /**
     * Index of the map chosen by the first client.
     */
    private int mapIndex;

    private int initialSkulls;

    private boolean mapInitialized;

    /**
     * Reference to the light model used.
     */
    private GameModel model;

    private ActionParser actionParser;

    private final String GUI = "gui.fxml";
    private final String LOADING_SCREEN = "loading_screen.fxml";
    private final String MAP_SELECTION= "map_selection.fxml";
    private final String START_MENU = "start_menu.fxml";

    @Override
    public void start(Stage primaryStage) throws Exception{
        set();
        this.stage = primaryStage;
        this.stage.setResizable(false);
        run();
    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Loads all fxml files used for gui and sets the first stage
     */
    public void set(){
        try {
            List<String> fxmls = new ArrayList<String>(Arrays.asList(GUI,LOADING_SCREEN,MAP_SELECTION,START_MENU));
            for(String path: fxmls) {
                FXMLLoader loader = new FXMLLoader((getClass().getResource(File.separatorChar + "fxml" + File.separatorChar +path)));
                this.sceneMap.put(path, new Scene(loader.load()));
                GuiController controller = loader.getController();
                this.controllerMap.put(path, controller);
                this.currentController = controller;
                controller.addGui(this);
            }
            currentScene = sceneMap.get(START_MENU);
            mainGuiController = (MainGuiController)getControllerFromString(GUI);
            this.stageName = START_MENU;
            this.actionParser = new ActionParser(this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Shows the starting window
     */
    public void run(){
        stage.setTitle("ADRENALINE.EXE");
        stage.setScene(currentScene);
        stage.show();
    }

    /**
     * Changes stage shown to the client by using a map linking stages and names
     * @param scene Name of the stage that needs to be shown
     */
    public void changeStage(String scene){
        this.stageName = scene;
        currentScene = sceneMap.get(scene);
        currentController = controllerMap.get(scene);
        stage.setScene(currentScene);
        format();
        stage.show();
    }

    /**
     * Centers the window in the center of the screen and makes it not resizable
     */
    private void format(){
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - currentScene.getWidth()) / 2);
        stage.setY((screenBounds.getHeight() - currentScene.getHeight()) / 2);
        stage.setResizable(false);
    }

    /**
     * Method that is called whenever there is a change notified on the GameModel,
     * when this method is called based on the notification received different methods
     * are called on the controller for the gui. This handles all updates on the view.
     * @param gameModel The object that is observed
     * @param object Parameter used to notify what has changed on the observed object
     */
    @Override
    public void update(Observable gameModel, Object object) {

        if(object.equals("setup")){
            Platform.runLater(() -> {
                StartMenuController controller = (StartMenuController)getControllerFromString(START_MENU);
                if(model.getSetupRequestAnswer().isFirstPlayer()){
                    controller.createGame();
                }else{
                    controller.connect();
                }
                if(model.getSetupRequestAnswer().isReconnection()){
                    mainGuiController.restoreSquares();
                    mainGuiController.updateGameboard();
                    //mainGuiController.updateHand();
                }
            });
        }

        if(object.equals("GameBoard")){
            System.out.println("gameboard updated");
            Platform.runLater(() -> {
                if(!mapInitialized){
                    mainGuiController.init();
                    changeStage(GUI);
                    mapInitialized = true;
                    handleTurn();
                }
                mainGuiController.restoreSquares();
                mainGuiController.updateGameboard();
            });
        }

        if(object.equals("Change player")){
            System.out.println("Player changed");
            Platform.runLater(() -> {
                mainGuiController.restoreSquares();
                mainGuiController.updateHand();
            });
            handleTurn();
        }

        if(object.equals("PlayerHand")){
            System.out.println("Hand Updated");
            Platform.runLater(() -> {
                mainGuiController.updateHand();
            });
        }

        if(object.equals("Player died")){
            System.out.println("gameboard updated");
            Platform.runLater(() -> {
                mainGuiController.updateGameboard();
            });
        }

        if(object.equals("Respawn")){
            System.out.println("Respawn");
            Platform.runLater(() -> {
                mainGuiController.spawnAfterDeath();
                mainGuiController.updateHand();
            });
        }

        if(object.equals("Disconnected")){
            Platform.runLater(() -> mainGuiController.disconnectPopupShow());
        }

        if(object.equals("Grenade")){
            Platform.runLater(() -> {
                mainGuiController.handleGrenade();
            });
        }

        if(object.equals("Game Over")){
            System.out.println("Game over");
            Platform.runLater(() -> {
                mainGuiController.gameOver();
            });
        }

        if(object.equals("Message")){
            System.out.println("message received");
            Platform.runLater(() -> {
                mainGuiController.logText(this.model.getMessage() + "\n");
            });
        }
    }


    public void handleTurn(){
        Platform.runLater(() -> {
            try{
                TimeUnit.MILLISECONDS.sleep(250);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            if(getConnection().getClientID() == getConnection().getCurrentID()){
                this.mainGuiController.enableAll();
                if (this.model.isToSpawn()){
                    mainGuiController.spawn();
                }else if(this.model.isToRespawn()){
                    mainGuiController.spawnAfterDeath();
                }
            } else {
                this.mainGuiController.disableAll();
                if(this.model.isToRespawn()){
                    mainGuiController.enableSpawn();
                    mainGuiController.spawnAfterDeath();
                }
            }
        });
    }

    public void setConnection(Connection connection){
        this.connection = connection;
    }

    public void setModel(){
        this.model = connection.getGameModel();
    }

    public void setMapIndex(int mapIndex) {
        this.mapIndex = mapIndex;
    }

    public void setInitialSkulls(int initialSkulls) {
        this.initialSkulls = initialSkulls;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getCharacter() {
        return character;
    }

    public void setPlayerName(String name) {
        this.playerName = name;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getMapIndex() {
        return mapIndex;
    }

    public int getInitialSkulls() {
        return initialSkulls;
    }

    public Connection getConnection(){
        return this.connection;
    }

    public GameModel getGameModel(){
        return this.model;
    }

    public void attachToObserver() {
        this.model.addObserver(this);
    }

    public GuiController getControllerFromString(String name){
        return this.controllerMap.get(name);
    }

    public ActionParser getActionParser() {
        return actionParser;
    }

}
