package server.model.gameboard;

import constants.Color;

import java.io.Serializable;

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
    private int[] multiplicityToken;

    private GameBoard gameBoard;
    /**
     * constructor for the killshotTrack
     * @param initialSkull number of skulls in a game
     */
    public KillshotTrack(GameBoard gameBoard, int initialSkull) {
        this.gameBoard = gameBoard;
        this.initialSkulls = initialSkull;
        this.remainingSkulls = initialSkull;
        this.multiplicityToken = new int[initialSkull];
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

    public int[] getMultiplicityToken() {
        return this.multiplicityToken;
    }
    /**
     *
     * @param index index of a cell in multiplicityToken
     * @return the number of token at index
     */
    public int getMulticiplity(int index) {
        return this.multiplicityToken[index];
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
    public boolean swapDamageSkull(int token2Add, Color color2Add) {
        multiplicityToken[remainingSkulls-1] = token2Add;
        damageTokens[remainingSkulls-1] = color2Add;
        remainingSkulls--;
        return remainingSkulls <= 0;
    }

    public int getInitialSkulls() {
        return initialSkulls;
    }

    public int getTokensOfColor(Color c){
        int counter = 0;
        for(int i = 0; i < initialSkulls; i++){
            if(damageTokens[i] == c)counter+= multiplicityToken[i];
        }
        return counter;
    }
}