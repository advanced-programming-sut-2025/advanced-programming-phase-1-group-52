package com.example.main.enums.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum TradeMenuCommands {
    Buy("trade -u (?<username>\\S+) -t (?<type>\\S+) -i (?<item>\\S+) -a (?<amount>\\S+) -p (?<price>\\S+)"),
    Trade("trade -u (?<username>\\S+) -t (?<type>\\S+) -i (?<item>\\S+) -a (?<amount>\\S+) -ti (?<targetItem>\\S+) -ta (?<targetAmount>\\S+)"),
    TradeList("trade list"),
    TradeResponse("trade response (?<response>accept|reject) -i (?<id>\\S+)"),
    TradeHistory("trade history"),
    Back("back");
    
    private final String pattern;

    TradeMenuCommands(String pattern) {
        this.pattern = pattern;
    }

    public Matcher getMatcher(String input) {
        Matcher matcher = Pattern.compile(this.pattern).matcher(input);
        return matcher.matches() ? matcher : null;
    }
}
