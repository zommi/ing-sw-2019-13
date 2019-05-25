package client;

import client.weapons.MacroEffect;
import client.weapons.MicroEffect;
import client.weapons.Weapon;

public interface InputInterface {
    public boolean getChoice(MacroEffect macroEffect);
    public MacroEffect chooseOneMacro(Weapon weapon);

    public boolean getChoice(MicroEffect microEffect);
}
