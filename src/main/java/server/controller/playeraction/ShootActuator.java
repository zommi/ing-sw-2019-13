package server.controller.playeraction;

import client.MicroInfo;
import client.ShootInfo;
import exceptions.NoSuchEffectException;

public class ShootActuator {
    public void actuate(ShootInfo shootInfo){
        try {
            for (int i=0; i<shootInfo.getActivatedMacros().size(); i++) {
                for (MicroInfo microInfo : shootInfo.getActivatedMacro(i).getActivatedMicros()) {
                    microInfo.actuate(shootInfo);
                }
            }
        }catch(NoSuchEffectException e){
            //this should never happen
        }
    }
}
