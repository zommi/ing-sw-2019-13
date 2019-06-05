package server.controller.playeraction;


public class ShootActuator {
    public void actuate(ShootInfo shootInfo){
        for(MacroInfo macroInfo : shootInfo.getActivatedMacros()){
            for(MicroInfo microInfo : macroInfo.getActivatedMicros())
                microInfo.actuate(shootInfo);
        }
    }
}
