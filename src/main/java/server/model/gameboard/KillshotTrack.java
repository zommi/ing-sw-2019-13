package server.model.gameboard;

import client.Info;
import constants.Color;
import constants.Constants;
import server.model.player.PlayerAbstract;

import java.io.Serializable;
import java.util.*;

/**
 * 
 */
public class KillshotTrack implements Serializable {

    private int initialSkulls;

    /**
     * int containing the number of skulls remaining in a given game
     */
    private int remainingSkulls;

    /**
     * array containing the color of the player who earned a skull
     */
    private Color[] damageTokens;

    /**
     * array containing the number of tokens at a given index
     */
    private int[] multiciplityToken;

    private GameBoard gameBoard;
    /**
     * constructor for the killshotTrack
     * @param initialSkull number of skulls in a game
     */
    public KillshotTrack(GameBoard gameBoard, int initialSkull) {
        this.gameBoard = gameBoard;
        this.initialSkulls = initialSkull;
        this.remainingSkulls = initialSkull;
        this.multiciplityToken = new int[initialSkull];
        this.damageTokens = new Color[initialSkull];
    }

    /**
     *
     * @return the value of remainingSulls
     */
    public int getRemainingSkulls() {
        return this.remainingSkulls;
    }

    public Color[] getDamageTokens() {
        return this.damageTokens;
    }

    public int[] getMulticiplityToken() {
        return this.multiciplityToken;
    }
    /**
     *
     * @param index index of a cell in multiplicityToken
     * @return the number of token at index
     */
    public int getMulticiplity(int index) {
        return this.multiciplityToken[index];
    }

    /**
     *
     * @param index index of a cell in multiplicityToken
     * @return the color of the token at index
     */
    public Color getColorAtIndex(int index){return this.damageTokens[index];}

    /**
     * removes a skull from the kill shot track
     * @param token2Add number of token to be added
     * @param color2Add color of the token that is being added
     */
    public boolean removeSkull(int token2Add, Color color2Add) {
        this.multiciplityToken[remainingSkulls-1] = token2Add;
        this.damageTokens[remainingSkulls-1] = color2Add;
        remainingSkulls--;
        return this.remainingSkulls <= 0;
    }

    public int getInitialSkulls() {
        return initialSkulls;
    }

    public void computeTrack(List<PlayerAbstract> playersInGame) {
        int[] points = Constants.POINT_VALUE;
        Map<Color, Integer> colorIntegerMap = new HashMap<>();
        for (Color c : Color.values()) {
            colorIntegerMap.put(c, getTokensOfColor(c));
        }
        List<PlayerAbstract> playersInOrder = new ArrayList<>();
        int max = 0;
        Color color = Color.UNDEFINED;
        while (!colorIntegerMap.isEmpty()){
            for (Map.Entry<Color, Integer> entry : colorIntegerMap.entrySet()) {
                if(entry.getValue() == 0)colorIntegerMap.remove(entry.getKey());
                if (entry.getValue() > max) {
                    max = entry.getValue();
                    color = entry.getKey();
                }
            }
            for(PlayerAbstract playerAbstract : playersInGame){
                if(playerAbstract.getColor() == color){
                    playersInOrder.add(playerAbstract);
                    break;
                }
            }
        }
        for(int i = 0; i < playersInGame.size(); i++){
            playersInGame.get(i).addPoints(i < points.length ? points[i] : 1);
        }
    }

    public int getTokensOfColor(Color c){
        int counter = 0;
        for(int i = 0; i < initialSkulls; i++){
            if(damageTokens[i] == c)counter+=multiciplityToken[i];
        }
        return counter;
    }
}