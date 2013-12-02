package com.eecs285.siegegame;

public class ActionParser {

    private String input;

    ActionParser(String in) {
        input = in;
    }

    public static enum ActionType {
        ATTACK_ARMY,
        ATTACK_CITY,
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
        END_TURN,
        NAME_CHANGE,
        WAITING_FOR_PLAYERS,
        STARTING_GAME
    }

    ActionType getActionType() {
        // returns the action type present in the input string
        // Army ActionTypes

        if (input.contains("attacks") && input.contains("army"))
            return ActionType.ATTACK_ARMY;
        else if (input.contains("attacks") && input.contains("city"))
            return ActionType.ATTACK_CITY;
        else if (input.contains("trains"))
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
        else if (input.contains("changed their name to"))
            return ActionType.NAME_CHANGE;
        else if (input.contains("Waiting for all players to be ready"))
            return ActionType.WAITING_FOR_PLAYERS;
        else if (input.contains("Starting the game"))
            return ActionType.STARTING_GAME;

        return null;
    }    
    

    Coord getFirstCoordinate() {
        // returns the first set of coordinates in the input string
        return getCoordinate(input);
    }

    Coord getSecondCoordinate() {
        // returns the second set of coordinates from input, or null
        // if there is only 1 pair

        // eliminate the beginning part of the string that contains coordinates
        int endFirstCoord = input.indexOf(')');
        String temp = input.substring(endFirstCoord + 1);

        // use getFirstCoordinate function to get the remaining coordinates
        return getCoordinate(temp);
    }

    private Coord getCoordinate(String in) {
        // if string doesn't contain parentheses, return null
        if (!(in.contains("(") && in.contains(")")))
            return null;

        // get substring of coordinates
        int beginCoords = in.indexOf('(');
        int endCoords = in.indexOf(')');
        String temp = in.substring(beginCoords + 1, endCoords);

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

    int getFirstPlayer() {
        // This player's name is always at beginning of string
        String[] words = input.split(" ");
        return getPlayerIndex(words[0]);
        
        /*
        String pName = words[0];

        // if string contains "Player's" instead of "Player", remove the 's
        int nameLength = words[0].length();
        if (words[0].charAt(nameLength - 2) == '\'') // need to escape ' mark
            words[0] = words[0].substring(0, nameLength - 2);

        for (int i = 0; i < Server.MAX_PLAYERS; i++) {
            if (Siege.players[i] != null && Siege.players[i].name == pName)
                return i;
        }
        return -1;*/
    }

    int getSecondPlayer() {
        // 2nd name is always after 'attacks' word
        int startIndex = input.indexOf("attacks") + "attacks".length() + 1;
        String temp = input.substring(startIndex, input.length());
        
        String[] words = temp.split(" ");
        return getPlayerIndex(words[0]);               
    }
    
    int getPlayerIndex(String pName) {
     // if string contains "Player's" instead of "Player", remove the 's
        int nameLength = pName.length();
        if (pName.charAt(nameLength - 2) == '\'') // need to escape ' mark
            pName = pName.substring(0, nameLength - 2);

        for (int i = 0; i < Server.MAX_PLAYERS; i++) {
            if (Siege.players[i] != null && Siege.players[i].name == pName)
                return i;
        }
        return -1;
    }
    
    String getTypeUnits() {
        String[] words = input.split(" ");
        return words[3];
    }
    
    int getNumUnits() {
        String[] words = input.split(" ");
        return Integer.valueOf(words[2]); 
    }
}

/*
 * Narration mode 
 * Start game 
 * Announce player, turn # 
 * Player trained unit in city coord (x, y) 
 * Player moved army from coord (x1, y1) to (x2, y2) 
 * Player merged army at coord (x1, y1) with (x2, y2) 
 * Player's army at (x1, y1) attacks player's army/city at (x2, y2)
 * Player's army loses units 
 * Player captures resource/city at coord (x, y) 
 * Player's city at coord (x, y) is under siege/liberated
 * Player's resource at coord (x, y) is under conflict/recaptured 
 * Player is defeated 
 * Player wins 
 * Player ends turn
 */
