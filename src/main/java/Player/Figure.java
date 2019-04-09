package Player;

public enum Figure {
    DESTRUCTOR(0),
    BANSHEE(1),
    DOZER(2),
    VIOLET(3),
    SPROG(4);

    private int id;

    Figure(int id){
        this.id = id;
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
}
