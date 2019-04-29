package Model.Cards;

import Constants.Color;
import Model.Items.AmmoCube;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class WeaponTest {

    @Test
    public void readCostFromFileTest(){
        Weapon testWeapon = new Weapon(19); //chose Cyberblade at random

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

    @Test
    public void testshoot(){
        Bullet bullet = new Bullet(0, 0, 2, 1, false);

        Weapon weap = new Weapon(0);
        assertEquals(weap.shoot(0,0,0).get(0).getX(), bullet.getX());
        assertEquals(weap.shoot(0,0,0).get(0).getY(), bullet.getY());
        assertEquals(weap.shoot(0,0,0).get(0).getDamage(), bullet.getDamage());
        assertEquals(weap.shoot(0,0,0).get(0).getMarks(), bullet.getMarks());
        assertEquals(weap.shoot(0,0,0).get(0).getTeleporterFlag(), bullet.getTeleporterFlag());

        bullet = new Bullet(0, 0, 1, 0, false);
        weap = new Weapon(1);
        assertEquals(weap.shoot(0,0,0).get(0).getX(), bullet.getX());
        assertEquals(weap.shoot(0,0,0).get(0).getY(), bullet.getY());
        assertEquals(weap.shoot(0,0,0).get(0).getDamage(), bullet.getDamage());
        assertEquals(weap.shoot(0,0,0).get(0).getMarks(), bullet.getMarks());
        assertEquals(weap.shoot(0,0,0).get(0).getTeleporterFlag(), bullet.getTeleporterFlag());

        bullet = new Bullet(0, 0, 2, 0, false);
        assertEquals(weap.shoot(1,0,0).get(0).getX(), bullet.getX());
        assertEquals(weap.shoot(1,0,0).get(0).getY(), bullet.getY());
        assertEquals(weap.shoot(1,0,0).get(0).getDamage(), bullet.getDamage());
        assertEquals(weap.shoot(1,0,0).get(0).getMarks(), bullet.getMarks());
        assertEquals(weap.shoot(1,0,0).get(0).getTeleporterFlag(), bullet.getTeleporterFlag());


    }
}