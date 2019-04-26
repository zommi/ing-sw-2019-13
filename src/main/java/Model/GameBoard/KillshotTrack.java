package Model.GameBoard;

import Constants.Color;

/**
 * 
 */
public class KillshotTrack {

    private int remainingSkulls;
    private Color[] damageTokens;
    private int[] multiciplityToken;

    public KillshotTrack(int initialSkull) {
        this.remainingSkulls = initialSkull;
        this.multiciplityToken = new int[initialSkull];
        this.damageTokens = new Color[initialSkull];
    }


    /**
     * @return
     */
    public int getRemainingSkulls() {
        return this.remainingSkulls;
    }

    /**
     * @return
     */
    public int getMulticiplity(int index) {
        return this.multiciplityToken[index];
    }

    public Color getColorAtIndex(int index){return this.damageTokens[index];}

    /**
     * @return
     */
    public void removeSkull(int token2Add, Color color2Add) {
        this.multiciplityToken[remainingSkulls-1] = token2Add;
        this.damageTokens[remainingSkulls-1] = color2Add;
        remainingSkulls--;
    }

}