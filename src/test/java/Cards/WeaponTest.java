package Cards;

import Constants.Color;
import Items.AmmoCube;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class WeaponTest {

    @Test
    public void readCostFromFileTest(){
        Weapon testWeapon = new Weapon(20); //chose Cyberblade at random

        List<AmmoCube> testList = new ArrayList<AmmoCube>();
        testList.add(new AmmoCube(Color.RED));
        testList.add(new AmmoCube(Color.YELLOW));
        assertEquals(testList.get(0).getColor(),testWeapon.getCost().get(0).getColor());
        assertEquals(testList.get(1).getColor(),testWeapon.getCost().get(1).getColor());

        assertEquals(testWeapon.getCost1(),Collections.emptyList());

        List<AmmoCube> testList2 = new ArrayList<AmmoCube>();
        testList2.add(new AmmoCube(Color.YELLOW));
        assertEquals(testList2.get(0).getColor(),testWeapon.getCost2().get(0).getColor());
    }

}