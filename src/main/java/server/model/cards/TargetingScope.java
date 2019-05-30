package server.model.cards;

import server.model.items.*;
import constants.*;

import java.io.Serializable;
import java.util.*;


public class TargetingScope extends Powerup implements Serializable {



    public TargetingScope() {
    }

    @Override
    public String getName() {
        return "Targeting scope";
    }
}