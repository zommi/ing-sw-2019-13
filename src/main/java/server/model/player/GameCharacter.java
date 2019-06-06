package server.model.player;

import constants.Color;
import constants.Constants;
import server.model.map.SquareAbstract;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * 
 */
public class GameCharacter implements Serializable {


    private Figure figure;
    private static boolean[] figureChosen = new boolean[Constants.NUM_FIGURES];


    private SquareAbstract position;
    private ConcretePlayer concretePlayer;
    private transient SquareAbstract oldPosition;

    public GameCharacter(){

    }

    public GameCharacter(Figure f){
        this.figure = f;
        f.setOwner(this);
        setTaken(f);
    }

    public static void initialize(){
        for(int i = 0; i < Constants.NUM_FIGURES; i++) figureChosen[i]=false;
    }

    public static void setTaken(Figure f){
        figureChosen[f.getId()] = true;
    }

    public static boolean isTaken(int id){
        return figureChosen[id];
    }

    public Figure getFigure(){ return this.figure;}

    public void setConcretePlayer(ConcretePlayer concretePlayer) {
        this.concretePlayer = concretePlayer;
    }

    public ConcretePlayer getConcretePlayer(){
        //TODO implement with clone
        return concretePlayer;
    }

    public static List<Figure> getValidFigures(){
        //Maybe implement in java functional
        List<Figure> res = new ArrayList<Figure>();
        for(Figure f : Figure.values()){
            if(!isTaken(f.getId())){
                res.add(f);
            }
        }
        return res;
    }

    public static List<GameCharacter> getTakenCharacters(){
        List<GameCharacter> res = new ArrayList<GameCharacter>();
        for(Figure f : Figure.values()){
            if(isTaken(f.getId())){
                res.add(f.getOwner());
            }
        }
        return res;
    }


    public void setPosition(SquareAbstract position){
        this.position = position;
        position.addCharacter(this);
    }
    /**
     *
     * @param sq
     */
    public void move(SquareAbstract sq) {
        getPosition().removeCharacter(this);
        this.concretePlayer.getPlayerBoard().setPosition(sq.getRow(), sq.getCol());
        this.position = sq;
        sq.addCharacter(this);
    }

    /**
     * @return
     */
    public SquareAbstract getPosition() {
        return position;
    }

    public void spawn(SquareAbstract sp) {
        if(position == null){
            sp.addCharacter(this);
            this.position = sp;
        } else{
            System.out.println("Invalid move");
        }
    }

    public Color getColor(){
        return this.figure.getColor();
    }

    public void setOldPosition(){
        this.oldPosition = position;
    }

    public SquareAbstract getOldPosition(){
        return oldPosition;
    }
}