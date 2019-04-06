public enum Figure {
    DESTRUCTOR (0),
    BANSHEE (1),
    DOZER (2),
    VIOLET (3),
    SPROG (4);

    private static final int NUM_FIGURES = 5;
    private static boolean[] figureChosen = new boolean[NUM_FIGURES];
    private int id;

    Figure(int id){
        this.id = id;
    }

    public static void setTaken(){
        figureChosen[id] = true;
    }

    public static boolean isTaken(){
        return figureChosen[id];
    }

}
