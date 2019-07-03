package client.gui.guielements;

import constants.Color;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import server.model.cards.WeaponCard;

public class GuiWeaponCard extends ImageView {

    private int index;

    private WeaponCard card;

    public GuiWeaponCard(WeaponCard card, int index, String path){
        super(path);
        this.card = card;
        this.index = index;
    }

    public GuiWeaponCard(String path, int index){
        super(path);
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return this.card.getName();
    }

    public WeaponCard getWeaponCard() {
        return card;
    }
}
