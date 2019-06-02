package constants;

import server.model.game.Game;

public enum Color {
    RED("r",0, "#ff0000", "#af2b2b"),
    BLUE("b",1, "#0000ff", "#2b4daf"),
    YELLOW("y",2, "#ffff00", "#d8de25"),
    GREEN("g",3, "#00ff00", "#298c2c"),
    WHITE("w",4, "#ffffff", "e5e3e3"),
    PURPLE("p",5, "#b600ff", "#791c89"),
    GREY("e",6, "#6d6d6d", "#614c4c"),
    UNDEFINED("u",7, "#000000", "#000000");

    private int index;
    private String abbreviation;
    private String hexValue;
    private String spHexValue;

    private Color(String abbreviation, int index, String normalHexvalue, String spHexvalue) {
        this.abbreviation = abbreviation;
        this.index = index;
        this.hexValue = normalHexvalue;
        this.spHexValue = spHexvalue;
    }
    public String getAbbreviation() { return abbreviation; }

    public static Color fromString(String s) {
        for (Color color : Color.values()) {
            if (color.abbreviation.equalsIgnoreCase(s)) {
                return color;
            }
        }
        return null;
    }

    public static Color fromIndex(int index){
        for(Color c : Color.values()){
            if(c.index == index) return c;
        }
        return null;
    }

    public static Color fromCharacter(String name){
        switch (name){
            case  "DESTRUCTOR":
                return YELLOW;
            case "BANSHEE" :
                return BLUE;
            case  "VIOLET":
                return PURPLE;
            case "DOZER" :
                return GREY;
            case  "SPROG":
                return GREEN;
            default:
                return UNDEFINED;
        }
    }

    @Override
    public String toString() {
        return abbreviation;
    }

    public int getIndex() {
        return index;
    }

    public String getHexValue() {
        return hexValue;
    }

    public String getSpHexValue() {
        return spHexValue;
    }
}
