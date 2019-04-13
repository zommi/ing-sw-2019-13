package Constants;

public enum Color {
    RED("r"),
    BLUE("b"),
    YELLOW("y"),
    GREEN("g"),
    WHITE("w"),
    PURPLE("p"),
    UNDEFINED("u");

    private Color(String abbreviation) { this.abbreviation = abbreviation; }
    public String getAbbreviation() { return abbreviation; }

    private String abbreviation;

    public static Color fromString(String s) {
        for (Color color : Color.values()) {
            if (color.abbreviation.equalsIgnoreCase(s)) {
                return color;
            }
        }
        return null;
    }
}
