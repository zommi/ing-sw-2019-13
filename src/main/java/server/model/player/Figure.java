package server.model.player;

import constants.Color;

public enum Figure {
    DESTRUCTOR(0, Color.YELLOW),
    BANSHEE(1, Color.BLUE),
    DOZER(2, Color.GREY),
    VIOLET(3, Color.PURPLE),
    SPROG(4, Color.GREEN);

    private int id;
    private Character owner;
    private Color color;

    Figure(int id, Color color){
        this.id = id;
        this.color = color;
        this.owner = null;
    }

    public void setOwner(Character c){
        this.owner = c;
    }

    public Character getOwner(){
        return owner;
    }

    public int getId() {
        return this.id;
    }

    public static Figure getValue(int id){
        switch(id) {
            case 0 : return DESTRUCTOR;
            case 1 : return BANSHEE;
            case 2 : return DOZER;
            case 3 : return VIOLET;
            case 4 : return SPROG;
            default: return null;
        }
    }

    @Override
    public String toString() {
        switch(getId()) {
            case 0 : return "Destructor";
            case 1 : return "Banshee";
            case 2 : return "Dozer";
            case 3 : return "Violet";
            case 4 : return "Sprog";
            default: return "";
        }
    }

    public Color getColor(){
        return this.color;
    }
}
