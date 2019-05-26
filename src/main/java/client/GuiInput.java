package client;

import client.weapons.MacroEffect;
import client.weapons.MicroEffect;
import client.weapons.Weapon;

public class GuiInput implements InputInterface {
    @Override
    public boolean getChoice(MacroEffect macroEffect) {
        return false;
    }

    @Override
    public MacroEffect chooseOneMacro(Weapon weapon) {
        return null;
    }

    @Override
    public boolean getChoice(MicroEffect microEffect) {
        return false;
    }
}
