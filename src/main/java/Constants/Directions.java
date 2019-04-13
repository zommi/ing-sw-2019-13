package Constants;

public enum Directions {

        NORTH("n"), SOUTH("s"), WEST("w"), EAST("e");

        private Directions(String abbreviation) { this.abbreviation = abbreviation; }
        public String getAbbreviation() { return abbreviation; }

        private String abbreviation;

        public static Directions fromString(String s) {
                for (Directions dir : Directions.values()) {
                        if (dir.abbreviation.equalsIgnoreCase(s)) {
                                return dir;
                        }
                }
                return null;
        }

}
