package view;


public class InitialMapAnswer implements ServerAnswer {
    private int numMap;

    public InitialMapAnswer(int numMap){
        this.numMap = numMap;
    }

    public int getNumMap(){
        return this.numMap;
    }
}
