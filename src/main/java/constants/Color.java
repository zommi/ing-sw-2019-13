package constants;

public enum Color {
    RED("r", false,true,0, "#ff0000", "#af2b2b", "\u001B[31m"),
    BLUE("b", true,true,1, "#0000ff", "#2b4daf", "\u001B[34m"),
    YELLOW("y", true,true,2, "#ffff00", "#d8de25", "\u001B[33m"),
    GREEN("g",true, false, 3, "#00ff00", "#298c2c", "\u001B[32m"),
    WHITE("w", false,false, 4, "#ffffff", "e5e3e3", "\u001B[37m"),
    PURPLE("p", true,false, 5, "#b600ff", "#791c89", "\u001B[35m"),
    GREY("e",true, false, 6, "#6d6d6d", "#614c4c", ""),
    UNDEFINED("u", false,false, 7, "#000000", "#000000", "");

    private int index;
    private String abbreviation;
    private String hexValue;
    private String spHexValue;
    private String ansi;
    private boolean ammoColor;
    private boolean characterColor;

    private Color(String abbreviation, boolean characterColor, boolean ammoColor, int index, String normalHexvalue, String spHexvalue, String ansi) {
        this.abbreviation = abbreviation;
        this.index = index;
        this.hexValue = normalHexvalue;
        this.spHexValue = spHexvalue;
        this.ansi = ansi;
        this.ammoColor = ammoColor;
        this.characterColor = characterColor;
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

    public String getHexValue() {
        return hexValue;
    }

    public String getSpHexValue() {
        return spHexValue;
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
