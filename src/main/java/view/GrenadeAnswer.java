package view;

public class GrenadeAnswer implements ServerAnswer {
    //USED TO NOTIFY THAT THE GRENADE IS -1 AGAIN
    private int i;

    public GrenadeAnswer(int i){
        this.i = i;
    }

    public int getResult(){
        return this.i;
    }
}
