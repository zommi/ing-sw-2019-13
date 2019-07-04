package server.model.player;

import constants.Color;

public enum Figure {
    DESTRUCTOR(0, Color.YELLOW),
    BANSHEE(1, Color.BLUE),
    DOZER(2, Color.WHITE),      //it should be grey but white is the new grey
    VIOLET(3, Color.PURPLE),
    SPROG(4, Color.GREEN);

    private int id;
    private GameCharacter owner;
    private Color color;

    Figure(int id, Color color){
        this.id = id;
        this.color = color;
        this.owner = null;
    }

    public static Figure fromString(String name){
        if(name.equalsIgnoreCase("DESTRUCTOR"))
            return DESTRUCTOR;
        else if(name.equalsIgnoreCase("BANSHEE"))
            return BANSHEE;
        else if(name.equalsIgnoreCase("DOZER"))
            return DOZER;
        else if(name.equalsIgnoreCase("VIOLET"))
            return VIOLET;
        else if(name.equalsIgnoreCase("SPROG"))
            return SPROG;
        else
            return null;
    }


    public void setOwner(GameCharacter c){
        this.owner = c;
    }

    public GameCharacter getOwner(){
        return owner;
    }

    public int getId() {
        return this.id;
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
