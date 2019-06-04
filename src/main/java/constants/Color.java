package constants;

public enum Color {
    RED("r",0, "#ff0000", "#af2b2b", "\u001B[31m"),
    BLUE("b",1, "#0000ff", "#2b4daf", "\u001B[34m"),
    YELLOW("y",2, "#ffff00", "#d8de25", "\u001B[33m"),
    GREEN("g",3, "#00ff00", "#298c2c", "\u001B[32m"),
    WHITE("w",4, "#ffffff", "e5e3e3", "\u001B[37m"),
    PURPLE("p",5, "#b600ff", "#791c89", "\u001B[35m"),
    GREY("e",6, "#6d6d6d", "#614c4c", ""),
    UNDEFINED("u",7, "#000000", "#000000", "");

    private int index;
    private String abbreviation;
    private String hexValue;
    private String spHexValue;
    private String ansi;

    private Color(String abbreviation, int index, String normalHexvalue, String spHexvalue, String ansi) {
        this.abbreviation = abbreviation;
        this.index = index;
        this.hexValue = normalHexvalue;
        this.spHexValue = spHexvalue;
        this.ansi = ansi;
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

    public String getAnsi() {
        return ansi;
    }
}
