package client.gui;

import client.Connection;
import client.GameModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import server.model.map.GameMap;

import java.io.File;
import java.util.HashMap;

public class MainGui extends Application {


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

    private UpdaterGUI updater;


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
            this.updater = new UpdaterGUI(this);
            new Thread(this.updater).start();
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

    public void setConnection(Connection connection){
        this.connection = connection;
    }

    public void setModel(){
        this.updater.setModel(connection.getGameModel());
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
        return this.updater.getGameModel();
    }

    public void attachToObserver() {
        this.updater.attachToObserver();
    }

    public GuiController getControllerFromString(String name){
        return this.controllerMap.get(name);
    }
}
