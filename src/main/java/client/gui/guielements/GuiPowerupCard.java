package client.gui.guielements;

import constants.Color;
import javafx.scene.image.ImageView;
import server.model.cards.PowerUpCard;

public class GuiPowerupCard extends ImageView {

    private int index;

    private Color color;

    private String name;

    private PowerUpCard card;

    public GuiPowerupCard(String path,PowerUpCard card, int index){
        super(path);
        this.color = card.getColor();
        this.name = card.getName();
        this.card = card;
        this.index = index;
    }

    public GuiPowerupCard(String path, int index){
        super(path);
        this.color = Color.UNDEFINED;
        this.name = "default";
        this.card = null;
        this.index = index;
    }



    public int getIndex() {
        return index;
    }

    public Color getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public PowerUpCard getCard() {
        return card;
    }
}
