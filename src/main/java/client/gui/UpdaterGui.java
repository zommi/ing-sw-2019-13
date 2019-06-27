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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.HashMap;
import java.util.Observable;
import java.util.concurrent.TimeUnit;

public class UpdaterGui extends Application implements Updater {


    private HashMap<String, Parent> sceneMap = new HashMap<>();

    private HashMap<String, GuiController> controllerMap = new HashMap<>();

    private GuiController currentController;

    private MainGuiController mainGuiController;

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

    boolean handInitialized;

    private GameModel model;

    private ActionParser actionParser;

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

    public void set(){
        try {
//            List<Path> files = new ArrayList<>();
//            Files.newDirectoryStream(Paths.get(getClass().getResource("/fxml").toURI()),path -> path.toFile().isFile()).forEach(files::add);
//            //File fxmlDir = new File(getClass().getResourceAsStream(File.separatorChar + "fxml"));
//            for(Path path: files) {
//                String[] subDir = path.toString().split("/");
//                FXMLLoader loader = new FXMLLoader((getClass().getResource(
//                        "/" + subDir[subDir.length - 2] + "/" + subDir[subDir.length - 1])));
//                this.sceneMap.put(path.getFileName().toString(), loader.load());
//                GuiController controller = loader.getController();
//                this.controllerMap.put(path.getFileName().toString(), controller);
//                this.currentController = controller;
//                controller.addGui(this);
//            }
            List<String> fxmls = new ArrayList<String>(Arrays.asList("gui.fxml","loading_screen.fxml","map_selection.fxml","start_menu.fxml"));
            for(String path: fxmls) {
                FXMLLoader loader = new FXMLLoader((getClass().getResource(File.separatorChar + "fxml" + File.separatorChar +path)));
                this.sceneMap.put(path, loader.load());
                GuiController controller = loader.getController();
                this.controllerMap.put(path, controller);
                this.currentController = controller;
                controller.addGui(this);
            }
            currentScene = new Scene(sceneMap.get("start_menu.fxml"));
            mainGuiController = (MainGuiController)getControllerFromString("gui.fxml");
            this.stageName = "stage_menu.fxml";
            this.actionParser = new ActionParser(this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void run(){
        stage.setTitle("ADRENALINE.EXE");
        stage.setScene(currentScene);
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

        if(object.equals("setup")){
            Platform.runLater(() -> {
                StartMenuController controller = (StartMenuController)getControllerFromString("start_menu.fxml");
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

        if(object.equals("GameBoard") && mapInitialized){
            System.out.println("gameboard updated");
            Platform.runLater(() -> {
                mainGuiController.restoreSquares();
                mainGuiController.updateGameboard();
                mainGuiController.updateHand();
            });
            if(connection.getGrenadeID() != -1) {
                Platform.runLater(() -> {
                    mainGuiController.handleGrenade();
                });
            }
        }

        if(object.equals("Change player")){
            System.out.println("Player changed");
            Platform.runLater(() -> {
                mainGuiController.restoreSquares();
            });
            handleTurn();
        }

        if(object.equals("GameBoard") && !mapInitialized){
            System.out.println("Game initialised");
            Platform.runLater(() -> {
                getControllerFromString("gui.fxml").init();
                changeStage("gui.fxml");
                mainGuiController.restoreSquares();
                mainGuiController.updateGameboard();
                mainGuiController.updateHand();
                mapInitialized = true;
            });
            handleTurn();
        }

        if(object.equals("Map") && mapInitialized){

        }

        if(object.equals("PlayerHand") && !handInitialized && !mapInitialized){
            System.out.println("Hand initilized");
            this.handInitialized = true;
        }

        if(object.equals("PlayerHand") && handInitialized && mapInitialized){
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

        if(object.equals("Spawn phase")){
            System.out.println("spawn commanded");
            Platform.runLater(() -> {
                mainGuiController.spawnAfterDeath();
                mainGuiController.updateHand();
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
                }
            } else {
                this.mainGuiController.disableAll();
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

    public ActionParser getActionParser() {
        return actionParser;
    }

}
