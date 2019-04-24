package GameBoard;

import Cards.AmmoTile;

import Constants.*;
import Exceptions.ReadJsonErrorException;
import Items.AmmoCube;
import Items.AmmoTileType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class AmmoTileDeck {

    LinkedList<AmmoTile> deck = new LinkedList<>();

    AmmoTileDeck(){
        try {
            initializeDeck();
        }catch (ReadJsonErrorException e){
            e.printStackTrace();
        }
    }

    public void initializeDeck() throws ReadJsonErrorException {
        ObjectMapper mapper = new ObjectMapper();
        String pathToFile = "." + File.separatorChar +  "AmmoTiles.json";
        File file = new File(pathToFile);

        AmmoTileType[] arrayOfTiles = new AmmoTileType[0];

        try {
            arrayOfTiles = mapper.readValue(file, AmmoTileType[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(arrayOfTiles.length == 0) throw new ReadJsonErrorException();

        for(int i = 0; i < arrayOfTiles.length ; i++){
            for(int j = 0; j < arrayOfTiles[i].getNumberOfCards(); j++){
                this.deck.push(new AmmoTile(createListFromType(arrayOfTiles[i]),
                        arrayOfTiles[i].hasPowerup()));
            }
        }
        shuffle();
    }

    public AmmoTile draw(){
        return this.deck.pop();
    }

    public List<AmmoCube> createListFromType(AmmoTileType type){
        List<AmmoCube> listToReturn = new ArrayList<>();

        for(int i = 0; i < type.getRedCubes(); i++) listToReturn.add(new AmmoCube(Color.RED));
        for(int i = 0; i < type.getBlueCubes(); i++) listToReturn.add(new AmmoCube(Color.BLUE));
        for(int i = 0; i < type.getYellowCubes(); i++) listToReturn.add(new AmmoCube(Color.YELLOW));
        return listToReturn;
    }

    public LinkedList<AmmoTile> getDeck() {
        return deck;
    }

    public void shuffle(){
        Collections.shuffle(this.deck);
    }

    @Override
    public String toString() {
        int sizeOfDeck = deck.size();
        String stringToReturn = "";
        for(int i = 0; i < sizeOfDeck; i++){
            stringToReturn += deck.get(i).toString() + '\n';
        }
        return stringToReturn;
    }
}
