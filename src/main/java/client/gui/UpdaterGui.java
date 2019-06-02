package client.gui;

import client.Connection;
import client.GameModel;
import client.Updater;
import client.gui.guielements.GuiController;
import exceptions.GameAlreadyStartedException;
import exceptions.NotEnoughPlayersException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Observable;

public class UpdaterGui extends Application implements Updater {


    private HashMap<String, Parent> sceneMap = new HashMap<>();

    private HashMap<String, GuiController> controllerMap = new HashMap<>();

    private GuiController currentController;

    private Scene currentScene;

    private Stage stage;

    private String stageName;

    private Connection connection;

    private String playerName;

    private String character;

    private int mapIndex;

    private int initialSkulls;

    private int playerId;

    boolean mapInitialized;

    private GameModel model;

    @Override
    public void start(Stage primaryStage) throws Exception{
        set();
        this.stage = primaryStage;
        run();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void set(){
        try {
            File fxmlDir = new File(getClass().getResource("/fxml").toURI());
            for(File file: fxmlDir.listFiles()) {
                String[] subDir = file.getPath().split("/");
                FXMLLoader loader = new FXMLLoader((getClass().getResource(
                        "/" + subDir[subDir.length - 2] + "/" + subDir[subDir.length - 1])));
                this.sceneMap.put(file.getName(), loader.load());
                GuiController controller = loader.getController();
                this.controllerMap.put(file.getName(), controller);
                this.currentController = controller;
                controller.addGui(this);
            }
            currentScene = new Scene(sceneMap.get("start_menu.fxml"));
            this.stageName = "stage_menu.fxml";
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void run(){
        stage.setTitle("ADRENALINE.EXE");
        stage.setScene(currentScene);
        format();
        stage.show();
    }

    public void changeStage(String scene){
        this.stageName = scene;
        currentScene = new Scene(sceneMap.get(scene));
        currentController = controllerMap.get(scene);
        stage.setScene(currentScene);
        format();
        stage.show();
    }

    private void format(){
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - currentScene.getWidth()) / 2);
        stage.setY((screenBounds.getHeight() - currentScene.getHeight()) / 2);
        stage.setResizable(false);
    }

    @Override
    public void update(Observable gameModel, Object object) {
        if(object.equals("GameBoard")){

        }

        if(object.equals("Change player")){
            System.out.println("Player changed");
            handleTurn();
        }


        if(object.equals("Map") && !mapInitialized){
            System.out.println("Game initialised");
            Platform.runLater(() -> {
                getControllerFromString("gui.fxml").init();
                changeStage("gui.fxml");
                mapInitialized = true;
            });
            handleTurn();
        }

        if(object.equals("PlayerBoard")){

        }

        if(object.equals("PlayerHand")){
            System.out.println("Hand Updated");
            Platform.runLater(() -> {
                MainGuiController controllerConverted = (MainGuiController) currentController;
                controllerConverted.updateHand();
            });
        }
    }


    public void handleTurn(){
        Platform.runLater(() -> {
            if(getConnection().getClientID() == getConnection().getCurrentID()){
                this.currentController.enableAll();
            } else {
                this.currentController.disableAll();
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

    public void createGame(){
        this.connection.add(this.playerName,this.mapIndex,this.initialSkulls);
    }

    public void setPlayerName(String name) {
        this.playerName = name;
    }

    public Scene getCurrentScene() {
        return currentScene;
    }

    public GuiController getCurrentController(){
        return currentController;
    }

    public Stage getStage() {
        return stage;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
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

    public String getStageName() {
        return stageName;
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

}
