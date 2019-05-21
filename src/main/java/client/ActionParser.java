package client;

import java.util.StringTokenizer;

public class ActionParser {

    public static Info createMoveEvent(int coordinatex, int coordinatey) {
        Info moveInfo = new MoveInfo(coordinatex, coordinatey);
        return null;
    }

    public static Info createShootEvent(){
        return null;
        //TODO
    }


    public static Info createCollectEvent(int collectDecision) {
        return null;
    }

    public static Info createPowerUpEvent(String PowerUp) {
        return null;
    }
}

        /* if (read.length() == 0) {
            throw new NotAValidInput("Empty input");
        }

        StringTokenizer tokenizer = new StringTokenizer(read);

        String action = tokenizer.nextToken().toUpperCase();

        if (action.equals("MOVE")) {
            if (!st.hasMoreTokens()) {
                throw new NotAValidInput("Nothing to say!");
            } else {
                String message = st.nextToken();
                while (st.hasMoreTokens()) {
                    message = message + " " + st.nextToken();
                }
                return new ActionChat(message);
            }

        } else if (action.equals("SHOOT")) {
            if (!st.hasMoreTokens()) {
                throw new NotAValidInput("Missing coordinate");
            }
            String coordinate = st.nextToken();
            if (!(coordinate.length() == 3)) {
                throw new NotAValidInput(NOT_VALID_COORD_TEXT);
            }
            Coordinate parsedCoordinate = parseCoordinate(coordinate);
            if (st.hasMoreTokens()) {
                throw new NotAValidInput(NOT_VALID_COMMAND_TEXT);
            }
            return new ActionMove(parsedCoordinate);

        } else if (action.equals("COLLECT")) {
            if (st.hasMoreTokens()) {
                throw new NotAValidInput(NOT_VALID_COMMAND_TEXT);
            }
            return new ActionAttack();

        } else if (action.equals("RECHARGE")) {
            if (!st.hasMoreTokens()) {
                throw new NotAValidInput("Missing card type");
            }
            String card = st.nextToken();
            if ("SPOTLIGHT".equals(card)) {
                if (!st.hasMoreTokens()) {
                    throw new NotAValidInput("Missing coordinate");
                } else {
                    Coordinate target = parseCoordinate(st.nextToken());
                    if (st.hasMoreTokens()) {
                        throw new NotAValidInput(NOT_VALID_COMMAND_TEXT);
                    } else {
                        return new ActionUseCard(new SpotlightCard(), target);
                    }
                }

            }
            if (st.hasMoreTokens()) {
                throw new NotAValidInput(NOT_VALID_COMMAND_TEXT);
            }
            if ("ATTACK".equals(card)) {
                return new ActionUseCard(new AttackCard());
            }
            if ("ADRENALINE".equals(card)) {
                return new ActionUseCard(new AdrenalineCard());
            }
            if ("DEFENSE".equals(card)) {
                throw new NotAValidInput("DEFENSE is a passive item card");
            }
            if ("SEDATIVES".equals(card)) {
                return new ActionUseCard(new SedativesCard());
            }
            if ("TELEPORT".equals(card)) {
                return new ActionUseCard(new TeleportCard());
            }
            throw new NotAValidInput("Not a valid card");

        }
       else if (action.equals("PLAY POWERUP")) {
            if (!st.hasMoreTokens()) {
                throw new NotAValidInput("Missing card type");
            }
            String card = st.nextToken();
            if ("SPOTLIGHT".equals(card)) {
                if (!st.hasMoreTokens()) {
                    throw new NotAValidInput("Missing coordinate");
                } else {
                    Coordinate target = parseCoordinate(st.nextToken());
                    if (st.hasMoreTokens()) {
                        throw new NotAValidInput(NOT_VALID_COMMAND_TEXT);
                    } else {
                        return new ActionUseCard(new SpotlightCard(), target);
                    }
                }

            }
            if (st.hasMoreTokens()) {
                throw new NotAValidInput(NOT_VALID_COMMAND_TEXT);
            }
            if ("ATTACK".equals(card)) {
                return new ActionUseCard(new AttackCard());
            }
            if ("ADRENALINE".equals(card)) {
                return new ActionUseCard(new AdrenalineCard());
            }
            if ("DEFENSE".equals(card)) {
                throw new NotAValidInput("DEFENSE is a passive item card");
            }
            if ("SEDATIVES".equals(card)) {
                return new ActionUseCard(new SedativesCard());
            }
            if ("TELEPORT".equals(card)) {
                return new ActionUseCard(new TeleportCard());
            }
            throw new NotAValidInput("Not a valid card");*/



