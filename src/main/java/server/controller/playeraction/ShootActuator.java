package server.controller.playeraction;


public class ShootActuator {
    public void actuate(ShootInfo shootInfo){
        for (int i=0; i<shootInfo.getActivatedMacros().size(); i++) {
            for (MicroInfo microInfo : shootInfo.getActivatedMacro(i).getActivatedMicros()) {
                microInfo.actuate(shootInfo);
            }
        }
    }
}
