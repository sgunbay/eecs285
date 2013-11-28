package com.eecs285.siegegame;

public class ActionParser {

    public static enum ActionType {
        ATTACK,
        RECRUIT,
        LOSE_UNITS,
        MOVE_ARMY,
        MERGE_ARMY,
        CAPTURE_RESOURCE,
        CAPTURE_CITY,
        CITY_UNDER_SIEGE,
        CITY_LIBERATED,
        RESOURCE_UNDER_CONFLICT,
        RESOURCE_RECAPTURED,
        PLAYER_DEFEATED,
        PLAYER_WINS,
        END_TURN
    }

    static ActionType getActionType(String input) {
        // returns the action type present in the input string

        // Army ActionTypes
        if (input.contains("attacks"))
            return ActionType.ATTACK;
        else if (input.contains("trained"))
            return ActionType.RECRUIT;
        else if (input.contains("loses units"))
            return ActionType.LOSE_UNITS;
        else if (input.contains("moved army"))
            return ActionType.MOVE_ARMY;
        else if (input.contains("merged army"))
            return ActionType.MERGE_ARMY;

        // Resource ActionTypes
        else if (input.contains("captures resource"))
            return ActionType.CAPTURE_RESOURCE;
        else if (input.contains("resource") && input.contains("under conflict"))
            return ActionType.RESOURCE_UNDER_CONFLICT;
        else if (input.contains("resource") && input.contains("recaptured"))
            return ActionType.RESOURCE_RECAPTURED;

        // City ActionTypes
        else if (input.contains("captures city"))
            return ActionType.CAPTURE_CITY;
        else if (input.contains("city") && input.contains("under siege"))
            return ActionType.CITY_UNDER_SIEGE;
        else if (input.contains("city") && input.contains("liberated"))
            return ActionType.CITY_LIBERATED;

        // Game ActionType
        else if (input.contains("wins"))
            return ActionType.PLAYER_WINS;
        else if (input.contains("defeated"))
            return ActionType.PLAYER_DEFEATED;
        else if (input.contains("ends turn"))
            return ActionType.END_TURN;

        return null;
    }

    static Coord getFirstCoordinate(String input) {
        // returns the first set of coordinates in the input string

        // if string doesn't contain parentheses, return null
        if (!(input.contains("(") && input.contains(")")))
            return null;

        // get substring of coordinates
        int beginCoords = input.indexOf('(');
        int endCoords = input.indexOf(')');
        String temp = input.substring(beginCoords + 1, endCoords);

        // split at comma to separate row and column values
        String[] coords = temp.split(",");
        if (coords.length > 2) {
            System.out.println("ERROR in coordinate format");
            System.out.println("Should be (x, y)");
            System.exit(-1);
        }

        // if space precedes column value, remove it
        if (coords[1].charAt(0) == ' ')
            coords[1] = coords[1].substring(1);

        int row = Integer.parseInt(coords[0]);
        int col = Integer.parseInt(coords[1]);

        return (new Coord(row, col));
    }

    static Coord getSecondCoordinate(String input) {
        // returns the second set of coordinates from input, or null
        // if there is only 1 pair

        // eliminate the beginning part of the string that contains coordinates
        int endFirstCoord = input.indexOf(')');
        input = input.substring(endFirstCoord + 1);

        // use getFirstCoordinate function to get the remaining coordinates
        return getFirstCoordinate(input);
    }
}
