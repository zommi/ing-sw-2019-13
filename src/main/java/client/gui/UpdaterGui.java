package client.gui;

import client.ActionParser;
import client.info.Connection;
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
     * Controller for the game stage, it can be used before the game has started
     */
    private MainGuiController mainGuiController;

    /**
     * Reference to the scene shown
     */
    private Scene currentScene;

    private Stage stage;

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

    private static final String GUI = "gui.fxml";
    private static final String LOADING_SCREEN = "loading_screen.fxml";
    private static final String MAP_SELECTION= "map_selection.fxml";
    private static final String START_MENU = "start_menu.fxml";
    private static final String RESPAWN_MESSAGE = "You died!\nChoose a powerup and Spawn in that color! \n";

    @Override
    public void start(Stage primaryStage) {
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
            List<String> fxmls = new ArrayList<>(Arrays.asList(GUI,LOADING_SCREEN,MAP_SELECTION,START_MENU));
            for(String path: fxmls) {
                FXMLLoader loader = new FXMLLoader((getClass().getResource(File.separatorChar + "fxml" + File.separatorChar +path)));
                this.sceneMap.put(path, new Scene(loader.load()));
                GuiController controller = loader.getController();
                this.controllerMap.put(path, controller);
                controller.addGui(this);
            }
            currentScene = sceneMap.get(START_MENU);
            mainGuiController = (MainGuiController)getControllerFromString(GUI);
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
        currentScene = sceneMap.get(scene);
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
     * are called on the controller for the gui. This handles all updates on the answers.
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
            Platform.runLater(() -> mainGuiController.updateHand());
        }

        if(object.equals("Player died")){
            System.out.println("gameboard updated");
            Platform.runLater(() -> mainGuiController.updateGameboard());
        }

        if(object.equals("Respawn")){
            System.out.println("Respawn");
            Platform.runLater(() -> {
                mainGuiController.logText(RESPAWN_MESSAGE);
                mainGuiController.updateHand();
            });
        }

        if(object.equals("Disconnected")){
            Platform.runLater(() -> mainGuiController.disconnectPopupShow());
        }

        if(object.equals("Grenade")){
            Platform.runLater(() -> mainGuiController.handleGrenade());
        }

        if(object.equals("Game Over")){
            System.out.println("Game over");
            Platform.runLater(() -> mainGuiController.gameOver());
        }

        if(object.equals("Message")){
            System.out.println("message received");
            Platform.runLater(() -> mainGuiController.logText(this.model.getMessage() + "\n"));
        }

        if(object.equals("Server offline")){
            Platform.runLater(() -> mainGuiController.close());
        }
    }


    private void handleTurn(){
        Platform.runLater(() -> {
            if(getConnection().getClientID() == getConnection().getCurrentID()){
                this.mainGuiController.enableAll();
                if (this.model.isToSpawn()){
                    mainGuiController.logText("Choose a powerup and Spawn in that color! \n");
                }else if(this.model.isToRespawn()){
                    mainGuiController.logText(RESPAWN_MESSAGE);
                }
            } else {
                this.mainGuiController.disableAll();
                if(this.model.isToRespawn()){
                    mainGuiController.enableSpawn();
                    mainGuiController.logText(RESPAWN_MESSAGE);
                }
            }
        });
    }

    public void setConnection(Connection connection){
        this.connection = connection;
    }

    void setModel(){
        this.model = connection.getGameModel();
    }

    void setMapIndex(int mapIndex) {
        this.mapIndex = mapIndex;
    }

    void setInitialSkulls(int initialSkulls) {
        this.initialSkulls = initialSkulls;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getCharacter() {
        return character;
    }

    void setPlayerName(String name) {
        this.playerName = name;
    }

    String getPlayerName() {
        return playerName;
    }

    int getMapIndex() {
        return mapIndex;
    }

    int getInitialSkulls() {
        return initialSkulls;
    }

    public Connection getConnection(){
        return this.connection;
    }

    public GameModel getGameModel(){
        return this.model;
    }

    void attachToObserver() {
        this.model.addObserver(this);
    }

    public GuiController getControllerFromString(String name){
        return this.controllerMap.get(name);
    }

    ActionParser getActionParser() {
        return actionParser;
    }

}
