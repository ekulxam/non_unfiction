package survivalblock.non_unfiction;

import eu.midnightdust.lib.config.MidnightConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NonUnfictionConfig extends MidnightConfig {

    @Entry
    public static String targetPlayerName = "";

    @Entry
    public static List<String> powerUsernameList = new ArrayList<>();
}
