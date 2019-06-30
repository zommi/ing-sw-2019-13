package constants;

import java.io.File;

public class Constants {

    //match rules
    public static final int MIN_PLAYERS = 2;
    public static final int MAX_PLAYERS = 5;
    public static final int MIN_PLAYERS_TO_CONTINUE = 2;

    public static final long TAGBACK_TIMEOUT_MSEC = 180000;     //default 3 minutes
    public static final long ACTION_TIMEOUT_MSEC =  180000;     //default 3 minutes
    public static final long SPAWN_TIMEOUT_SEC =    180000;     //default 3 minutes

    public static final long PING_DELAY_SEC = 1;

    public static final String ANSI_RESET = "\u001B[0m";

    public static final String REGISTRATION_ROOM_NAME = "gameproxy";



    //game rules

    public static final int MIN_SKULLS = 1;     //default 5
    public static final int MAX_SKULLS = 8;     //default 8

    public static final int FIRST_MAP = 1;
    public static final int LAST_MAP = 4;

    public static final int NUMBER_OF_WEAPON_PER_SPAWN_POINT = 3;

    public static final int BETTERCOLLECTDAMAGE = 2;
    public static final int BETTERSHOOTDAMAGE = 5;

    public static final int MAX_MOVES_BETTERER_COLLECT = 3;
    public static final int MAX_MOVES_BETTER_COLLECT = 2;
    public static final int MAX_MOVES_NORMAL_COLLECT = 1;
    public static final int MAX_MOVES_BETTER_SHOOT = 1;

    public static final int NO_CHOICE = -99999;
    public static final int CHOICE = 99999;



    //token rules
    public static final int NUMBER_OF_WEAPONS = 21;
    public static final int NUMBER_OF_AMMOTILE = 36;
    public static final int MAX_AMMO_CUBES_PER_COLOR = 3;
    public static final int NUM_FIGURES = 5;



    //player rules
    public static final int MAX_HP = 12;                //default 12
    public static final int DEATH_THRESHOLD = 10;       //default 10

    public static final int MAX_MARKS = 3;              //default 3

    public static final int MAX_WEAPON_HAND = 3;        //default 3
    public static final int MAX_POWERUP_HAND = 3;       //default 3

    public static final int NUM_POWERUP_START = 10;      //default 2
    public static final int NUM_AMMO_START = 99;         //default 1

    public static final int[] POINT_VALUE = {8,6,4,2,1,1};
    public static final int[] INITIAL_AMMO = {1,1,1};

    public static final String CUBE = "∎";
    public static final String PLAYER_CLI_ICON = "∎";
    public static final String DEAD_PLAYER_CLI_ICON = "X";



    //POWERUP RULES
    public static final int TARGET_SCOPE_DMG = 1;
    public static final int TAGBACK_GRENADE_MARKS = 1;
    public static final int NEWTON_MAX_DISTANCE = 2;
    public static final String TARGETING_SCOPE = "Targeting Scope";



    //paths

    public static final String PATH_TO_RESOURCES_FOLDER = "." + File.separatorChar + "src" +
            File.separatorChar + "main" + File.separatorChar + "resources";

    public static final String PATH_TO_AMMOTILE_JSON = File.separatorChar + "ammo_tiles.json";

    public static final String PATH_TO_POWERUP_JSON = File.separatorChar + "powerups.json";

    public static final String PATH_TO_WEAPONS_JSON = File.separatorChar + "weapons.json";

    public static final String PATH_TO_MAP_11 = File.separatorChar + "maps" + File.separatorChar + "map11.txt";

    public static final String PATH_TO_MAP_12 = File.separatorChar + "maps" + File.separatorChar + "map12.txt";

    public static final String PATH_TO_MAP_21 = File.separatorChar + "maps" + File.separatorChar + "map21.txt";

    public static final String PATH_TO_MAP_22 = File.separatorChar + "maps" + File.separatorChar + "map22.txt";

    public static final String PATH_TO_CONFIG = File.separatorChar + "config.properties";
}
