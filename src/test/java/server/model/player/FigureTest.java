package server.model.player;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FigureTest {

    @Test
    public void fromTest(){
        for(Figure figure : Figure.values()){
            assertEquals(Figure.fromString(figure.toString()),figure);
        }
    }

}