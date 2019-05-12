package constants;

import java.io.File;

public class Constants {
    public static final int MAX_HP = 12;
    public static final int MAX_MARKS = 3;
    public static final int NUM_FIGURES = 5;
    public static final int MAX_AMMO_CUBES_PER_COLOR = 3;
    public static final int MAX_NUMBER_OF_DEATHS = 6;
    public static final int[] POINT_VALUE = {8,6,4,2,1,1};
    public static final int[] INITIAL_AMMO = {1,1,1};

    public static final int NUMBER_OF_WEAPONS = 21;
    public static final int NUMBER_OF_POWERUP_CARDS = 24;
    public static final int NUMBER_OF_WEAPON_PER_SPAWN_POINT = 3;

    public static final int NO_POWERUP = -10;

    public static final int FIRST_POWERUP_EXTRA = 0;
    public static final int SECOND_POWERUP_EXTRA = 1;
    public static final int THIRD_POWERUP_EXTRA = 2;

    public static final int FIRST_ACTION = 0;
    public static final int SECOND_ACTION = 1;


    public static final int DEATH_THRESHOLD = 10;

    public static final int FIRST_EFFECT_WEAPON_COST_INDEX_FROM_FILE = 5;
    public static final int SECOND_EFFECT_WEAPON_COST_INDEX_FROM_FILE = 14;
    public static final int THIRD_EFFECT_WEAPON_COST_INDEX_FROM_FILE = 23;

    public static final String PATH_TO_RESOURCES_FOLDER = "." + File.separatorChar + "Resources";

    public static final String PATH_TO_AMMOTILE_JSON =  PATH_TO_RESOURCES_FOLDER +
                                                            File.separatorChar +  "AmmoTiles.json";

    public static final String PATH_TO_POWERUP_JSON =  PATH_TO_RESOURCES_FOLDER +
                                                            File.separatorChar + "PowerUps.json";

    public static final String PATH_TO_WEAPONS_JSON =  PATH_TO_RESOURCES_FOLDER +
            File.separatorChar + "weapons.json";

    public static final String PATH_TO_MAP_11 = PATH_TO_RESOURCES_FOLDER +
        File.separatorChar + "maps" + File.separatorChar + "map11.txt";

    public static final String PATH_TO_MAP_12 = PATH_TO_RESOURCES_FOLDER +
            File.separatorChar + "maps" + File.separatorChar + "map12.txt";

    public static final String PATH_TO_MAP_21 = PATH_TO_RESOURCES_FOLDER +
            File.separatorChar + "maps" + File.separatorChar + "map21.txt";

    public static final String PATH_TO_MAP_22 = PATH_TO_RESOURCES_FOLDER +
            File.separatorChar + "maps" + File.separatorChar + "map22.txt";

    public static final int NUMBER_OF_AMMOTILE = 36;

    public static final int MAX_NUMBER_OF_ADRENALINE_MOVEMENTS = 2;

    public static final int MAX_NUMBER_OF_NORMAL_MOVEMENTS = 1;

    public static final int MAX_NUMBER_OF_CARDS = 3;

    public static final int NO_CHOICE = -99999;

    public static final int TARGETS_OF_EFFECT_ZERO = 0;
    public static final int TARGETS_OF_EFFECT_ONE = 1;
    public static final int TARGETS_OF_EFFECT_TWO = 2;
}
