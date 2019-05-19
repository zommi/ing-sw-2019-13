package constants;

public enum Direction {

        NORTH("n"), SOUTH("s"), WEST("w"), EAST("e");

        private Direction(String abbreviation) { this.abbreviation = abbreviation; }
        public String getAbbreviation() { return abbreviation; }

        private String abbreviation;

        public static Direction fromString(String s) {
                for (Direction dir : Direction.values()) {
                        if (dir.abbreviation.equalsIgnoreCase(s)) {
                                return dir;
                        }
                }
                return null;
        }

}
