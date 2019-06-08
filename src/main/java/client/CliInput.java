package client;

import client.weapons.MacroEffect;
import client.weapons.MicroEffect;
import client.weapons.Weapon;
import constants.Constants;
import server.model.cards.PowerUp;
import server.model.cards.PowerUpCard;
import server.model.map.Room;
import server.model.player.GameCharacter;
import server.model.player.PlayerAbstract;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class CliInput extends InputAbstract{
    private Scanner scanner;

    public CliInput() {
        scanner = new Scanner(System.in);
    }

    @Override
    public boolean getChoice(MacroEffect macroEffect) {
        System.out.println("Do you wanna activate this macro effect?\n"
                + macroEffect + "[y to activate]\n");
        String s = scanner.nextLine();
        return s.equals("y");
    }

    @Override
    public boolean getChoice(MicroEffect microEffect) {
        System.out.println("Do you wanna activate this micro effect?\n"
                + microEffect + "[y to activate]\n");
        String s = scanner.nextLine();
        return s.equals("y");
    }

    @Override
    public List<SquareInfo> askSquares(int maxSquares) {
        String rowString;
        String colString;
        boolean ask;
        boolean exit = false;
        int row;
        int col;
        List<SquareInfo> list = new ArrayList<>();
        int counter = maxSquares;      //always != 0
        do{
            ask = true;
            while(ask) {
                if(maxSquares != 1 && counter != maxSquares)
                    System.out.println("Give me the row: [or say 'stop']\n");
                else
                    System.out.println("Give me the row: \n");
                rowString = scanner.nextLine();

                if (rowString.equals("stop") && maxSquares != 1 && counter != maxSquares) {
                    exit = true;
                    break;
                }
                else{
                    try{
                        System.out.println("Give me the column:\n");
                        colString = scanner.nextLine();
                        row = Integer.parseInt(rowString);
                        col = Integer.parseInt(colString);
                        list.add(new SquareInfo(row, col));
                        ask = false;
                    }catch(NumberFormatException e){
                        System.out.println("Not a valid choice, try again.");
                    }
                }
            }
            if(exit)
                break;
            else
                counter = counter - 1;
        }while(counter > 0);
        return list;
    }

    @Override
    public List<String> askPlayers(int maxTargetPlayerSize){
        List<GameCharacter> characters = new ArrayList<>();
        for(GameCharacter gameCharacter : gameModel.getGameBoard().getResult().getActiveCharacters()){
            if(!gameCharacter.getConcretePlayer().equals(gameModel.getMyPlayer()))
                characters.add(gameCharacter);
        }

        String choice;
        boolean ask;
        boolean exit = false;
        int number = 0;
        List<String> list = new ArrayList<>();
        int counter = maxTargetPlayerSize;      //always != 0
        do{
            ask = true;
            while(ask) {
                for(int i = 0; i<characters.size(); i++){
                    System.out.println(characters.get(i).getColor().getAnsi() + characters.get(i).getConcretePlayer().getName()
                            + Constants.ANSI_RESET + " (" + i + ")");
                }
                if(maxTargetPlayerSize != 1 && counter != maxTargetPlayerSize)  //maybe maxTarget check is useless
                    System.out.println("Choose a player [or say 'stop']:\n");
                else
                    System.out.println("Choose a player:\n");
                choice = scanner.nextLine();
                if (choice.equals("stop") && maxTargetPlayerSize != 1 && counter != maxTargetPlayerSize) {
                    exit = true;
                    break;
                }
                else{
                    try{
                        number = Integer.parseInt(choice);
                        if(number < characters.size()){
                            list.add(characters.get(number).getConcretePlayer().getName());
                            System.out.println("You chose: " + characters.get(number).getConcretePlayer().getColor().getAnsi()
                                    + characters.get(number).getConcretePlayer().getName() + Constants.ANSI_RESET);
                            ask = false;
                        }
                        else{
                            System.out.println("Number not valid.");
                        }
                    }catch(NumberFormatException e){
                        System.out.println("Not a valid choice, try again.");
                    }
                }
            }
            if(exit)
                break;
            else
                counter--;
        }while(counter > 0);
        return list;
    }

    @Override
    public List<String> askRooms(int max){
        List<Room> rooms = new ArrayList<>(gameModel.getGameBoard().getResult().getMap().getRooms());

        String choice;
        boolean ask;
        boolean exit = false;
        int number = 0;
        List<String> list = new ArrayList<>();
        int counter = max;      //always != 0
        do{
            ask = true;
            while(ask) {
                for(int i = 0; i<rooms.size(); i++){
                    System.out.println(rooms.get(i).getColor().getAnsi() + rooms.get(i).getColor().name()
                            + Constants.ANSI_RESET + " (" + i + ")");
                }
                if(max != 1 && counter != max)  //maybe maxTarget check is useless
                    System.out.println("Choose a room [or say 'stop']:\n");
                else
                    System.out.println("Choose a room:\n");
                choice = scanner.nextLine();
                if (choice.equals("stop") && max != 1 && counter != max) {
                    exit = true;
                    break;
                }
                else{
                    try{
                        number = Integer.parseInt(choice);
                        if(number < rooms.size()){
                            list.add(rooms.get(number).getColor().name());
                            System.out.println("You chose " + number + ": " + rooms.get(number).getColor().name());
                            ask = false;
                        }
                        else{
                            System.out.println("Number not valid.");
                        }
                    }catch(NumberFormatException e){
                        System.out.println("Not a valid choice, try again.");
                    }
                }
            }
            if(exit)
                break;
            else
                counter--;
        }while(counter > 0);
        return list;
    }

    @Override
    public boolean getMoveChoice() {
        System.out.println("Do you wanna move before shooting? [y to activate] ");
        String s = scanner.nextLine();
        return s.equals("y");
    }

    @Override
    public List<PowerUpCard> askPowerUps() {
        System.out.println("Do you wanna use your PowerUps as ammo? [y to activate] ");
        String s = scanner.nextLine();
        if(s.equalsIgnoreCase("y")){
            List<PowerUpCard> returnedList = new ArrayList<>();
            List<PowerUpCard> cardsToAsk = new ArrayList<>(gameModel.getPlayerHand().getPowerupHand());
            boolean ask = true;
            while(ask){
                System.out.println("Choose the card you want to use, or say \"stop\":");
                for(int i = 0; i<cardsToAsk.size(); i++){
                    System.out.println(cardsToAsk.get(i) + " (" + (i+1) + ")");
                }

                try{
                    String strChoice = scanner.nextLine();
                    if(strChoice.equalsIgnoreCase("stop")){
                        break;
                    }

                    int choice = Integer.parseInt(strChoice) - 1;
                    if(choice>=0 && choice < cardsToAsk.size()){
                        returnedList.add(cardsToAsk.get(choice));
                        cardsToAsk.remove(choice);
                        if(cardsToAsk.isEmpty())
                            ask = false;
                    }
                    else{
                        System.out.println("Please insert a valid number.");
                    }
                }catch(NumberFormatException e){
                    System.out.println("Please insert a valid number, or say \"stop\".");
                }
            }
            return returnedList;
        }
        else
            return Collections.emptyList();

    }

    @Override
    public MacroEffect chooseOneMacro(Weapon weapon){
        System.out.println("Choose one of the following macro effects:");
        for(MacroEffect macroEffect : weapon.getMacroEffects()){
            System.out.println((macroEffect.getNumber()+1) + ": " + macroEffect);
        }
        String s;
        int i = 0;
        boolean ask = true;
        while(ask) {
            System.out.println("Choose a number\n");
            s = scanner.nextLine();
            try {
                i = Integer.parseInt(s) - 1;
                if(i>=0 && i < weapon.getMacroEffects().size())
                    ask = false;
                else
                    System.out.println("Choice not valid.");
            } catch (NumberFormatException e) {
                //ask = true
                System.out.println("Number not valid.");
            }
        }
        return weapon.getMacroEffect(i);
    }
}
