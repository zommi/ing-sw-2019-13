package client;

public class GuiInput implements InputInterface {
    @Override
    public boolean getChoice(MacroEffect macroEffect) {
        return false;
    }

    @Override
    public MacroEffect chooseOneMacro(Weapon weapon) {
        return null;
    }
}
