package server.model.gameboard;

import constants.Color;

/**
 * 
 */
public class KillshotTrack {

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

    public KillshotTrack(){

    }
    /**
     * constructor for the killshotTrack
     * @param initialSkull number of skulls in a game
     */
    public KillshotTrack(int initialSkull) {
        this.remainingSkulls = initialSkull;
        this.multiciplityToken = new int[initialSkull];
        this.damageTokens = new Color[initialSkull];
    }

    public KillshotTrack CreateCopy(KillshotTrack killshotTrackToCopy){
        KillshotTrack killshotTrack = new KillshotTrack();
        killshotTrack.remainingSkulls = killshotTrackToCopy.getRemainingSkulls();
        killshotTrack.damageTokens = killshotTrackToCopy.getDamageTokens();
        killshotTrack.multiciplityToken = killshotTrackToCopy.getMulticiplityToken();
        return killshotTrack;
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
    public void removeSkull(int token2Add, Color color2Add) {
        this.multiciplityToken[remainingSkulls-1] = token2Add;
        this.damageTokens[remainingSkulls-1] = color2Add;
        remainingSkulls--;
    }

}