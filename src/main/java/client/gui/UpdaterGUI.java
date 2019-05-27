package client.gui;

import client.Connection;
import client.GameModel;
import client.Updater;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import server.model.gameboard.GameBoard;

import java.io.File;
import java.util.HashMap;
import java.util.Observable;

public class UpdaterGUI extends Application implements Updater {

    private GameModel model;

    private HashMap<String, Parent> sceneMap = new HashMap<>();

    private Scene currentScene;

    private Stage stage;

    private Connection connection;

    private String playerName;

    private int mapIndex;

    private int initialSkulls;


    @Override
    public void start(Stage primaryStage) throws Exception{
        set();
        this.stage = primaryStage;
        run();
    }

    public void attachToObserver(){
        this.model.addObserver(this);
    }

    public void setModel(){
        this.model = connection.getGameModel();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void update(Observable gameModel, Object object){
        if(object.equals("GameBoard")){

        }

        if(object.equals("Map initialized")){
            changeStage("gui.fxml");
        }

        if(object.equals("Map")){

        }

        if(object.equals("PlayerBoard")){

        }

        if(object.equals("PlayerHand")){

        }
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
                controller.addGui(this);
            }
            currentScene = new Scene(sceneMap.get("start_menu.fxml"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void run(){
        stage.setTitle("ADRENALINE.EXE");
        stage.setScene(currentScene);
        stage.setResizable(false);
        stage.show();
    }

    public void changeStage(String scene){
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        currentScene = new Scene(sceneMap.get(scene));
        stage.setScene(currentScene);
        stage.setX((screenBounds.getWidth() - currentScene.getWidth()) / 2);
        stage.setY((screenBounds.getHeight() - currentScene.getHeight()) / 2);
        stage.setResizable(false);
        stage.show();
    }

    public void setConnection(Connection connection){
        this.connection = connection;
    }


    public void setMapIndex(int mapIndex) {
        this.mapIndex = mapIndex;
    }

    public void setInitialSkulls(int initialSkulls) {
        this.initialSkulls = initialSkulls;
    }

    public void createGame(){
        this.connection.add(this.playerName,this.mapIndex,this.initialSkulls);
    }

    public void setPlayerName(String name) {
        this.playerName = name;
    }

    public GameModel getGameModel() {
        return model;
    }

    public GameModel getModel() {
        return model;
    }

    public HashMap<String, Parent> getSceneMap() {
        return sceneMap;
    }

    public Scene getCurrentScene() {
        return currentScene;
    }

    public Stage getStage() {
        return stage;
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
}
