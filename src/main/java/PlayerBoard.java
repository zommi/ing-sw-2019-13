
import java.util.*;

/**
 * 
 */
public class PlayerBoard{
    ConcretePlayer player;

    private DamageToken[] damage;
    private int damageTaken;

    private DamageToken[] marks;
    private int marksOnBoard;

    private int[] pointValue;
    private int currentPointValueCursor;
    private int numberOfDeaths;

    private ArrayList<AmmoCube> redAmmo;
    private int validRedAmmo;

    private ArrayList<AmmoCube> yellowAmmo;
    private int validYellowAmmo;

    private ArrayList<AmmoCube> blueAmmo;
    private int validBlueAmmo;


    /**
     * Default constructor
     */
    public PlayerBoard(ConcretePlayer p) {
        this.player = p;
        this.damage = new DamageToken[Constants.MAX_HP];
        this.damageTaken = 0;
        this.marks = new DamageToken[Constants.MAX_MARKS];
        this.marksOnBoard = 0;
        this.numberOfDeaths = 0;
        this.pointValue = Constants.POINT_VALUE;
        this.currentPointValueCursor = 0;
        this.redAmmo = new ArrayList<AmmoCube>();
        this.validRedAmmo = 0;
        this.yellowAmmo = new ArrayList<AmmoCube>();
        this.validYellowAmmo = 0;
        this.blueAmmo = new ArrayList<AmmoCube>();
        this.validBlueAmmo = 0;
    }


    /**
     * @return
     */
    public int getDamageTaken() {
        return damageTaken;
    }

    /**
     * @return
     */
    public int getNumberOfDeaths() {
        return numberOfDeaths;
    }

    /**
     * @return
     */
    public List<AmmoCube> getValidRedAmmoCube() {
        return (ArrayList<AmmoCube>) redAmmo.clone();
    }

    public List<AmmoCube> getValidYellowAmmoCube() {
        return (ArrayList<AmmoCube>) yellowAmmo.clone();
    }

    public List<AmmoCube> getValidBlueAmmoCube() {
        return (ArrayList<AmmoCube>) blueAmmo.clone();
    }

    /**
     * @return
     */
    //add easter egg when someone dies 7 times in the same game and make him win.
    public void addSkull() {
        if(damageTaken > 10){
            numberOfDeaths++;
            currentPointValueCursor++;
        } else{
            System.err.println("PLAYER STILL ALIVE");
        }
    }

    public int getPointValue(){
        return pointValue[currentPointValueCursor];
    }

    public void addDamage(List<DamageToken> damage) {
        //TODO
        damageTaken += damage.size();
    }

    public void addMarks(int marks) {
    }
}