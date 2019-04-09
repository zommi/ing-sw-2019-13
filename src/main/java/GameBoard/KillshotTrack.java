package GameBoard;

import Constants.Color;
import Items.DamageToken;

import java.util.*;

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

    /**
     * @return
     */
    public void removeSkull(int token2Add, Color color2Add) {
        this.multiciplityToken[remainingSkulls] = token2Add;
        this.damageTokens[remainingSkulls] = color2Add;
        remainingSkulls--;
    }

}