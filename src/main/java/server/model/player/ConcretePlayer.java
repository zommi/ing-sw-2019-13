package server.model.player;

import client.weapons.Cost;
import client.weapons.Weapon;
import server.model.cards.PowerUpCard;
import server.model.cards.WeaponCard;
import server.model.game.Game;
import server.model.items.AmmoCube;
import server.model.map.*;
import constants.*;

import java.util.*;

/**
 *
 */
public class ConcretePlayer extends PlayerAbstract {

    private String name;
    private GameCharacter gameCharacter;
    private transient PlayerHand hand;
    private PlayerBoard playerBoard;
    private transient Game currentGame;
    private PlayerState state;
    private int clientID;
    private boolean ifCharacter;

    //added after createCopy
    private PlayerAbstract justDamagedBy;       //TODO check create copy

    public ConcretePlayer(String name) {
        this.name = name;
        this.hand = new PlayerHand(this);
        this.playerBoard = new PlayerBoard(this);
        this.state = PlayerState.NORMAL;
    }

    public void setCurrentGame(Game game){
        this.currentGame = game;
    }

    public void setIfCharacter(boolean choice){
        this.ifCharacter = choice;
    }

    public boolean getIfCharacter(){
        return this.ifCharacter;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public int getClientID(){
        return this.clientID;
    }

    public PlayerHand getHand() {
        return hand;
    }

    @Override
    public WeaponCard getWeaponCard(Weapon weapon) {
        for(WeaponCard weaponCard : hand.getWeaponHand()){
            if(weaponCard.getWeapon().equals(weapon))
                return weaponCard;
        }
        return null;
    }

    @Override
    public PowerUpCard getPowerUpCard(PowerUpCard powerUpCard) {
        for(PowerUpCard powerUpCard1 : hand.getPowerupHand()){
            if(powerUpCard1.equals(powerUpCard))
                return powerUpCard1;
        }
        return null;
    }

    @Override
    public WeaponCard getWeaponCard(WeaponCard weaponCard) {
        for(WeaponCard weaponCard1 : hand.getWeaponHand()){
            if(weaponCard1.equals(weaponCard))
                return weaponCard1;
        }
        return null;
    }

    public String getCharacterName(){
         return this.gameCharacter.getFigure().fromFigure();
    }

    public void setPlayerCharacter(Figure figure){
        this.gameCharacter = new GameCharacter(chooseFigure(figure));
        this.gameCharacter.setConcretePlayer(this);
        this.playerBoard.setCharacterName(figure.toString());
    }

    public GameCharacter getGameCharacter(){
        return gameCharacter;
    }

    public PlayerBoard getPlayerBoard(){return playerBoard;}

    public Game getCurrentGame(){return this.currentGame;}


    /**
     * Moves the gameCharacter to an adjacent square in a direction
     * @param direction direction to follow
     */
    public void move(Direction direction) {
        SquareAbstract currentPos = gameCharacter.getPosition();
        switch (direction){
            case NORTH:
                gameCharacter.move(currentPos.getnSquare());
                break;
            case SOUTH:
                gameCharacter.move(currentPos.getsSquare());
                break;
            case EAST:
                gameCharacter.move(currentPos.geteSquare());
                break;
            case WEST:
                gameCharacter.move(currentPos.getwSquare());
                break;
            default:
                break;
            }
    }


    /**
     * Uses a powerup in the player's hand
     * @param powerupIndex int between 0 and 2 corresponding to a powerupCard
     */
    public void usePowerup(int powerupIndex) {
        hand.playPowerup(powerupIndex);
    }


    public void collect(Square square){
        this.playerBoard.processAmmoTile(square.getAmmoTile());
    }

    public void collect(SpawnPoint spawnPoint, int choice){
        this.hand.addCard(spawnPoint.getWeaponCards().get(choice));
    }


    /**
     * Shows a player his own hand
     */
    public void showHand(){
        System.out.println(hand.toString());
    }

    /**
     * @return
     */
    public PlayerState getPlayerState() {
        return this.state;
    }

    public SquareAbstract getPosition(){ return this.gameCharacter.getPosition();}

    /**
     * Places a gameCharacter in a Spawnpoint
     * @param sp SpawnPoint in which the player wants to place its gameCharacter
     */
    public void spawn(SquareAbstract sp){
        gameCharacter.spawn(sp);
        playerBoard.spawn(getPosition().getRow(), getPosition().getCol());
    }


    public Color getColor(){
        return this.gameCharacter.getColor();
    }

    @Override
    public PlayerState currentState() {
        return state;
    }

    @Override
    public void drawPowerup() {
        if(!this.hand.powerupsFull()) {
            this.hand.addCard(currentGame.getCurrentGameBoard().getPowerupDeck().draw());
        }
    }


    public void setState(PlayerState state){
        this.state = state;
    }

    @Override
    public boolean canPay(ArrayList<AmmoCube> cost) {
        return this.playerBoard.canPay(cost);
    }

    @Override
    public boolean canPay(Cost cost) {      //TODO
        return this.playerBoard.getRedAmmo() >= cost.getRed() &&
                this.playerBoard.getBlueAmmo() >= cost.getBlue() &&
                this.playerBoard.getYellowAmmo() >= cost.getYellow();
    }

    public String getName() {
        return name;
    }

    @Override
    public void setOldPosition() {
        this.gameCharacter.setOldPosition();
    }

    @Override
    public SquareAbstract getOldPosition() {
        return this.gameCharacter.getOldPosition();
    }

    @Override
    public void addDamage(int damage, Color color) {
        this.playerBoard.addDamage(damage, color);
    }

    @Override
    public void addMarks(int marks, Color color) {
        this.playerBoard.addMarks(marks, color);
    }

    @Override
    public void setPosition(SquareAbstract square) {
        this.gameCharacter.getPosition().removeCharacter(this.gameCharacter);
        this.gameCharacter.setPosition(square);
        this.playerBoard.setPosition(square.getRow(), square.getCol());
    }

    @Override
    public PlayerAbstract getJustDamagedBy() {
        return justDamagedBy;
    }

    @Override
    public void setJustDamagedBy(PlayerAbstract justDamagedBy) {
        this.justDamagedBy = justDamagedBy;
    }

    @Override
    public void pay(Cost cost) {
        for(int i = 1; i<=cost.getBlue(); i++)
            playerBoard.decreaseAmmo(Color.BLUE);
        for(int i = 1; i<=cost.getRed(); i++)
            playerBoard.decreaseAmmo(Color.RED);
        for(int i = 1; i<=cost.getYellow(); i++)
            playerBoard.decreaseAmmo(Color.YELLOW);
    }

    public void payWithPowerUps(Cost cost, List<PowerUpCard> powerUpCards){
        Cost updatedCost = new Cost(cost);
        List<PowerUpCard> oneCard = new ArrayList<>();
        for(PowerUpCard powerUpCard : powerUpCards){
            Cost oldCost =  new Cost(updatedCost);
            oneCard.add(powerUpCard);
            updatedCost = updatedCost.subtract(Cost.powerUpListToCost(oneCard));
            if(!oldCost.equals(updatedCost))
                //discard the card
                getHand().removePowerUpCard(powerUpCard);

            oneCard.clear();
        }
        pay(updatedCost);
    }

    @Override
    public boolean hasPowerUpCards(List<PowerUpCard> powerUpCards) {
        //checks duplicate (no duplicate allowed because of the id)
        for(PowerUpCard powerUpCard : powerUpCards){
            for(PowerUpCard powerUpCard1 : powerUpCards){
                if(powerUpCard != powerUpCard1 && powerUpCard.equals(powerUpCard1))
                    return false;
            }
        }

        //checks possession
        for(PowerUpCard powerUpCard : powerUpCards){
            if(getPowerUpCard(powerUpCard) == null)
                return false;
        }
        return true;
    }

    @Override
    public boolean hasWeaponCards(List<WeaponCard> weaponCards) {
        //checks duplicates
        for(WeaponCard weaponCard : weaponCards){
            for(WeaponCard weaponCard1 : weaponCards){
                if(weaponCard != weaponCard1 && weaponCard.equals(weaponCard1))
                    return false;
            }
        }

        //checks possession
        for(WeaponCard weaponCard : weaponCards){
            if(getWeaponCard(weaponCard) == null)
                return false;
        }
        return true;
    }

    @Override
    public void die() {
        this.playerBoard.processDeath();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof ConcretePlayer))
            return false;
        ConcretePlayer player2 = (ConcretePlayer) obj;

        return this.gameCharacter.equals(player2.gameCharacter);

    }

    public boolean isOverkilled(){
        return this.playerBoard.getDamageTaken() == Constants.MAX_HP;
    }

    public Color getKillerColor(){
        return this.playerBoard.getKillerColor();
    }

    public void setHand(PlayerHand hand) {
        this.hand = hand;
    }
}
