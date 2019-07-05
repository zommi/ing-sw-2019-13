package server.model.gameboard;

import constants.Color;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
    private List<Color> damageTokens;

    /**
     * array containing the number of tokens at a given index
     */
    private List<Integer> multiplicityToken;

    /**
     * constructor for the killshotTrack
     * @param initialSkull number of skulls in a game
     */
    public KillshotTrack(int initialSkull) {
        this.initialSkulls = initialSkull;
        this.remainingSkulls = initialSkull;
        this.multiplicityToken = new ArrayList<>();
        this.damageTokens = new ArrayList<>();
    }

    /**
     * @return the value of remainingSulls
     */
    public int getRemainingSkulls() {
        return this.remainingSkulls;
    }

    public List<Color> getDamageTokens() {
        return this.damageTokens;
    }

    /**
     * @param index index of a cell in multiplicityToken
     * @return the number of token at index
     */
    public int getMulticiplity(int index) {
        return this.multiplicityToken.get(index);
    }

    /**
     * removes a skull from the kill shot track
     * @param token2Add number of token to be added
     * @param color2Add color of the token that is being added
     */
    public void swapDamageSkull(int token2Add, Color color2Add) {
        multiplicityToken.add(token2Add);
        damageTokens.add(color2Add);
        if(remainingSkulls > 0)remainingSkulls--;
    }

    public int getInitialSkulls() {
        return initialSkulls;
    }

    public int getTokensOfColor(Color c){
        int counter = 0;
        for(int i = 0; i < damageTokens.size(); i++){
            if(damageTokens.get(i) == c)counter+= multiplicityToken.get(i);
        }
        return counter;
    }
}