package constants;

public enum Color {
    RED("r", false,true,0, "#ff0000", "#af2b2b", "\u001B[31m", "#eb0a0a50"),
    BLUE("b", true,true,1, "#0000ff", "#2b4daf", "\u001B[34m", "#3a0aeb50"),
    YELLOW("y", true,true,2, "#ffff00", "#d8de25", "\u001B[33m", "#f4f80950"),
    GREEN("g",true, false, 3, "#00ff00", "#298c2c", "\u001B[32m", "#17951550"),
    WHITE("w", false,false, 4, "#ffffff", "e5e3e3", "\u001B[37m", ""),
    PURPLE("p", true,false, 5, "#b600ff", "#791c89", "\u001B[35m", "#b51bc250"),
    GREY("e",true, false, 6, "#6d6d6d", "#614c4c", "\u001B[30m", "#6d6d6d50"),
    UNDEFINED("u", false,false, 7, "#000000", "#000000", "", "");

    private int index;
    private String abbreviation;
    private String normalColor;
    private String tileColor;
    private String ansi;
    private boolean ammoColor;
    private boolean characterColor;
    private String playerboardCol;

    private Color(String abbreviation, boolean characterColor, boolean ammoColor, int index, String normalColor, String tileColor, String ansi, String playerboardCol) {
        this.abbreviation = abbreviation;
        this.index = index;
        this.tileColor = tileColor;
        this.normalColor = normalColor;
        this.ansi = ansi;
        this.ammoColor = ammoColor;
        this.characterColor = characterColor;
        this.playerboardCol = playerboardCol;
    }
    public String getAbbreviation() { return abbreviation; }

    public static Color fromString(String s) {
        for (Color color : Color.values()) {
            if (color.abbreviation.equalsIgnoreCase(s) || color.name().equalsIgnoreCase(s)) {
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
        switch (name.toUpperCase()){
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

    public String getTileColor() {
        return tileColor;
    }

    public String getPlayerboardCol() {
        return playerboardCol;
    }

    public String getNormalColor() {
        return normalColor;
    }

    public String getAnsi() {
        return ansi;
    }

    public boolean isAmmoColor(){
        return ammoColor;
    }

    public boolean isCharacterColor() {
        return characterColor;
    }
}
