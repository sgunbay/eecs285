package com.eecs285.siegegame;

public class ActionParser {
    
    private String input;
    
    ActionParser(String in) {
        input = in;
        //for(int i = 0; i < input.length(); i++)
        //    System.out.println("i = " + i + ": " + input.charAt(i));
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
        NAME_CHANGE
    }

    ActionType getActionType() {
        // returns the action type present in the input string
        // Army ActionTypes        
        
        if (input.contains("attacks") && input.contains("army"))
            return ActionType.ATTACK_ARMY;
        else if (input.contains("attacks") && input.contains("city"))
            return ActionType.ATTACK_CITY;
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
        else if(input.contains("changed their name to"))
            return ActionType.NAME_CHANGE;

        return null;
    }

    Coord getFirstCoordinate() {
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

    Coord getSecondCoordinate() {
        // returns the second set of coordinates from input, or null
        // if there is only 1 pair

        // eliminate the beginning part of the string that contains coordinates
        int endFirstCoord = input.indexOf(')');
        input = input.substring(endFirstCoord + 1);

        // use getFirstCoordinate function to get the remaining coordinates
        return getFirstCoordinate();
    }
    
    Player getFirstPlayer() {
        // This player's name is always at beginning of string
        String[] words = input.split(" ");
        String pName = words[0];
        
        // if string contains "Player's" instead of "Player", remove the 's
        int nameLength = words[0].length();
        if(words[0].charAt(nameLength - 2) == '\'') //need to escape ' mark
            words[0] = words[0].substring(0, nameLength - 2);
        
        
        System.out.println("in getFirstPlayer");
        for(int i = 0; i < Server.MAX_PLAYERS; i++) {
            if(Siege.players[i] != null && Siege.players[i].name == pName)
                 return Siege.players[i];
        }
        return null;
    }
}



/*
Narration mode
Start game
Announce player, turn #
Player trained unit in city coord (x, y)
Player moved army from coord (x1, y1) to (x2, y2)
Player merged army at coord (x1, y1) with (x2, y2)
Player's army at (x1, y1) attacks player's army/city at (x2, y2)
Player's army loses units
Player captures resource/city at coord (x, y)
Player's city at coord (x, y) is under siege/liberated
Player's resource at coord (x, y) is under conflict/recaptured
Player is defeated
Player wins
Player ends turn
*/
