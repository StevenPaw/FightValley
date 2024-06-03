package com.stevenpaw.fightvalley.common.utils;

import com.stevenpaw.fightvalley.common.items.HealthApple;
import com.stevenpaw.fightvalley.common.items.IArenaItem;

public class Util_ArenaItemParser {

    public static IArenaItem getItemFromName (String name) {
        switch (name) {
            case "Health Apple":
                return new HealthApple();
            default:
                return null;
        }
    }
}
