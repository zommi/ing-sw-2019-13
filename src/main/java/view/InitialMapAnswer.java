package view;


import java.io.Serializable;

public class InitialMapAnswer implements ServerAnswer, Serializable {
    int numMap;

    public InitialMapAnswer(int n){
        this.numMap = n;
    }

    public int getNumMap(){
        return this.numMap;
    }
}
