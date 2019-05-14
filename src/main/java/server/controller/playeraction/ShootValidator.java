package server.controller.playeraction;

import exceptions.NoSuchEffectException;
import server.model.cards.*;

public class ShootValidator {

    public boolean validate(ShootInfo shootInfo){
        //checks if macros order is correct and there are no duplicates
        if(shootInfo.getActivatedMacros().isEmpty())
            return false;
        for(int i = 0; i<shootInfo.getActivatedMacros().size() - 1; i++){
            if(shootInfo.getActivatedMacros().get(i).getMacroNumber() >= shootInfo.getActivatedMacros().get(i+1).getMacroNumber())
                return false;
        }

        //checks if macros are activated correctly, accordingly to weapon type
        if(shootInfo.getWeapon().getType() != WeaponType.EXTRA){
            if(shootInfo.getActivatedMacros().size() > 1)   //we know size is not zero
                return false;

        }
        else {
            //checks if all mandatory macros are activated(extra type weapon base effect)
            for(int i = 0; i < shootInfo.getWeapon().getMacroEffects().size(); i++){
                if(shootInfo.getWeapon().getMacroEffects().get(i).isMandatory()){
                    try{
                        shootInfo.getActivatedMacro(i);
                    }
                    catch (NoSuchEffectException e){
                        return false;
                    }
                }
            }
        }

        //now every single activated macro is processed
        Cost totalCost = new Cost(0,0,0);
        boolean limitedActivated = false;
        MacroEffect weaponMacro;
        for(MacroInfo macroInfo : shootInfo.getActivatedMacros()){
            //weapon must have this macro
            try{
                weaponMacro = shootInfo.getWeapon().getMacroEffect(macroInfo.getMacroNumber());
            }catch(NoSuchEffectException e){
                return false;
            }

            //player should have enough ammo
            totalCost = totalCost.sum(weaponMacro.getCost());
            if(!shootInfo.getAttacker().canPay(totalCost))
                return false;

            //checks if macro depends on the activation of another macro
            if(weaponMacro.isConditional()){
                try{
                    shootInfo.getActivatedMacro(weaponMacro.getMacroEffectIndex());
                }catch (NoSuchEffectException e){
                    return false;
                }
            }

            //checks if micros order is correct and there are no duplicates
            if(macroInfo.getActivatedMicros().isEmpty())
                return false;
            for(int i = 0; i < macroInfo.getActivatedMicros().size() - 1; i++){
                if(macroInfo.getActivatedMicros().get(i).getMicroNumber() >= macroInfo.getActivatedMicros().get(i+1).getMicroNumber())
                    return false;
            }

            //checks if all mandatory micros are activated
            for(int i = 0; i < weaponMacro.getMicroEffects().size(); i++){
                if(weaponMacro.getMicroEffects().get(i).isMandatory()){
                    try{
                        shootInfo.getActivatedMicro(macroInfo.getMacroNumber(), i);
                    }catch (NoSuchEffectException e){
                        return false;
                    }
                }
            }

            //now every activated micro for that specific macro is processed
            for(MicroInfo microInfo : macroInfo.getActivatedMicros()){
                //weapon must have the couple macro-micro
                MicroEffect weaponMicro;
                try{
                    weaponMicro = weaponMacro.getMicroEffect(microInfo.getMicroNumber());
                }catch (NoSuchEffectException e){
                    return false;
                }

                //checks if micro is limited
                if(weaponMicro.isLimited() && limitedActivated)
                    return false;
                else if(weaponMicro.isLimited())
                    limitedActivated = true;

                //checks if micro is conditional
                if(weaponMicro.isConditional()){
                    try{
                        shootInfo.getActivatedMicro(weaponMicro.getMacroEffectIndex(), weaponMicro.getMicroEffectIndex());
                    }catch (NoSuchEffectException e){
                        return false;
                    }
                }

                //checks if input is ok
                if(!shootInfo.areDimensionsOk(microInfo, weaponMicro))
                    return false;

                //checks every micro policy
                for(WeaponPolicy weaponPolicy : weaponMicro.getPolicies()){
                    if(!weaponPolicy.isVerified(shootInfo, microInfo))
                        return false;
                }
            }
        }





        return true;
    }
}
