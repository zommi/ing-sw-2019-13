package view;

import java.io.Serializable;

public class SetSpawnAnswer implements Serializable, ServerAnswer {

    private boolean result;

    public SetSpawnAnswer(boolean result){
        this.result = result;
    }

    public boolean getResult(){
        return this.result;
    }
}
