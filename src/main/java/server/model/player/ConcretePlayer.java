package server.model.player;

import client.weapons.Cost;
import client.weapons.Weapon;
import server.model.cards.PowerUpCard;
import server.model.cards.WeaponCard;
import server.model.game.Game;
import server.model.map.*;
import constants.*;

import java.util.*;

/**
 *
 */
public class ConcretePlayer extends PlayerAbstract {

    private boolean connected;

    private String name;

    private GameCharacter gameCharacter;

    private transient PlayerHand hand;

    private PlayerBoard playerBoard;

    private transient Game currentGame;

    private PlayerState state;

    private int clientID;

    private boolean characterChosen;

    private int points;

    //added after createCopy
    private PlayerAbstract justDamagedBy;

    public ConcretePlayer(String name) {
        this.name = name;
        this.hand = new PlayerHand(this);
        this.playerBoard = new PlayerBoard(this);
        this.state = PlayerState.NORMAL;
        this.points = 0;
        connected = true;
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    @Override
    public void addPoints(int i) {
        points+=i;
    }

    @Override
    public int getPoints(){
        return points;
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
        characterChosen = true;
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
        if(!hand.powerupsFull()) {
            hand.addCard(currentGame.getCurrentGameBoard().getPowerupDeck().draw());
        }
    }

    @Override
    public boolean canPay(Cost cost) {
        return playerBoard.getRedAmmo() >= cost.getRed() &&
                playerBoard.getBlueAmmo() >= cost.getBlue() &&
                playerBoard.getYellowAmmo() >= cost.getYellow();
    }

    @Override
    public void addDamage(int damage, Color color) {
        playerBoard.addDamage(damage, color);
    }

    @Override
    public void addMarks(int marks, Color color) {
        playerBoard.addMarks(marks, color);
    }

    @Override
    public void setPosition(SquareAbstract square) {
        gameCharacter.getPosition().removeCharacter(gameCharacter);
        gameCharacter.setPosition(square);
        playerBoard.setPosition(square.getRow(), square.getCol());
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

    public void setCurrentGame(Game game){
        this.currentGame = game;
    }

    public void setCharacterChosen(boolean choice){
        this.characterChosen = choice;
    }

    public boolean isCharacterChosen(){
        return this.characterChosen;
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
    public PlayerAbstract getJustDamagedBy() {
        return justDamagedBy;
    }

    @Override
    public void setJustDamagedBy(PlayerAbstract justDamagedBy) {
        this.justDamagedBy = justDamagedBy;
    }

    public void setState(PlayerState state){
        this.state = state;
    }

    public String getName() {
        return name;
    }

    @Override
    public PlayerState getPlayerState() {
        return this.state;
    }

    public SquareAbstract getPosition(){ return this.gameCharacter.getPosition();}

    public GameCharacter getGameCharacter(){
        return gameCharacter;
    }

    public PlayerBoard getPlayerBoard(){return playerBoard;}
}
