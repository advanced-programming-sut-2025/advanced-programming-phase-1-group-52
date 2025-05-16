package enums.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum GameMenuCommands {
    CheckFlagInNewGame("^game new -u\\b.*"),
    ExtractUsernames("(?:^game new -u|(?!^)\\G)\\s+(\\w+)"),
    GameMap("game map (?<map_number>[\\d]*)"),
    ExitGame("exit game"),
    NextTurn("next turn"),
    Time("time"),
    Date("date"),
    DateTime("datetime"),
    DayOfTheWeek("day of the week"),
    CHEATAdvanceTime("cheat advance time (?<amount>\\d+)h"),
    CHEATAdvanceDate("cheat advance date (?<amount>\\d+)d"),
    Season("season"),
    CHEATLightning("cheat Thor -l (?<x>\\d+), (?<y>\\d+)"),
    Weather("weather"),
    WeatherForecast("weather forecast"),
    CHEATWeather("cheat weather set (?<weather>\\S+)"),
    GreenHouseBuilding("greenhouse build"),
    Walk("walk -l (?<x>\\d+), (?<y>\\d+)"),
    PrintMap("print map -l (?<x>\\d+), (?<y>\\d+) -s (?<size>\\d+)"),
    HelpMap("help reading map"),
    EnergyShow("energy show"),
    CHEATEnergySet("energy set -v (?<value>\\d+)"),
    CHEATEnergyUnlimited("energy unlimited"),
    InventoryShow("inventory show"),
    RemoveInventoryItems("^inventory trash -i (?<name>[\\S]*)\\s*(?:-n (?<number>[\\d]+))?\\s*$"),
    ToolEquip("tools equip (?<name>\\S+)"),
    ToolShowCurrent("tools show current"),
    ToolShowAvailable("tools show available"),
    ToolUse("tools use -d (?<direction>\\S+)"),
    CraftInfo("craftinfo -n (?<name>\\S+)"),
    Plant("plant -s (?<seed>\\S+) -d (?<direction>\\S+)"),
    ShowPlant("showplant -l (?<x>\\d+), (?<y>\\d+)"),
    Fertilize("fertilize -f (?<fertilizer>\\S+) -d (?<direction>\\S+)"),
    HowMuchWater("howmuch water"),
    Eat("^(\\s*)eat(\\s+)(?<food_name>\\S+)(\\s*)$"),
    PettingAnAnimal("^(\\s*)pet(\\s+)-n(\\s+)(?<name>\\S+)(\\s*)$"),
    CHEATSetAnimalFriendship("cheat set friendship -n (?<animal name>\\S+) -c (?<amount>\\d+)"),
    Animals("animals"),
    ShepherdAnimals("^(\\s*)shepherd(\\s+)animals(\\s+)-n(\\s+)(?<animal name>[\\S\\s+])(\\s+)" +
            "-l(\\s*)(?<x>\\d+),(\\s*)(?<y>\\d+)(\\s*)$"),
    FeedHay("^(\\s*)feed(\\s+)hay(\\s+)-n(\\s+)(?<animal name>\\S+)(\\s*)$"),
    AnimalProduces("^(\\s*)produces(\\s*)$"),
    AnimalCollectProduce("^(\\s*)collect(\\s+)produce(\\s+)-n(\\s+)(?<name>\\S+)(\\s*)$"),
    SellAnimal("^(\\s*)sell(\\s+)animal(\\s+)-n(\\s+)(?<name>\\S+)(\\s*)$"),
    Fishing("^(\\s*)fishing(\\s+)-p(\\s+)(?<fishing pole>\\S+)(\\s*)$"),
    ArtisanUse("^(\\s*)artisan(\\s+)use(\\s+)(?<artisan_name>\\S+)(\\s+)(?<item1_name>\\S+)(\\s*)$"),
    CHEATAddBalance("cheat add (?<amount>\\d+) dollars"),
    Sell("sell (?<product_name>\\S+) -n (?<count>\\d+)"),
    ArtisanGet("^(\\s*)artisan(\\s+)get(\\s+)(?<artisan_name>\\S+)(\\s*)$"),
    PlaceItem("place item -n (?<item_name>\\S+) -d (?<direction>\\S+)"),
    CHEATAddItem("cheat add item -n (?<item_name>\\S+) -c (?<count>\\d+)"),
    Friendships("friendships"),
    Talk("talk -u (?<username>\\S+) -m (?<message>\\S+)"),
    TalkHistory("talk history -u (?<username>\\S+)"),
    Gift("gift -u (?<username>\\S+) -i (?<item>\\S+) -a (?<amount>\\d+)"),
    GiftList("gift list"),
    GiftRate("gift rate -i (?<id>\\S+) -r (?<rate>\\S+)"),
    GiftHistory("gift history -u (?<username>\\S+)"),
    Hug("hug -u (?<username>\\S+)"),
    Flower("flower -u (?<username>\\S+)"),
    AskMarriage("ask marriage -u (?<username>\\S+)"),
    MarriageResponse("respond -(?<response>accept|reject) -u (?<username>\\S+)"),
    StartTrade("start trade"),
    MeetNPC("meet NPC (?<name>\\S+)"),
    GiftNPC("gift NPC (?<name>\\S+) -i (<item>\\S+)"),
    FriendshipNPCList("friendship NPC list"),
    QuestsList("quests list"),
    QuestsFinish("quests finish -i (?<id>\\S+)"),
    ShowCurrentMenu(""),
    MenuExit("");

    private final String pattern;

    GameMenuCommands(String pattern) {
        this.pattern = pattern;
    }

    public Matcher getMatcher(String input) {
        Matcher matcher = Pattern.compile(this.pattern).matcher(input);
        return matcher.matches() ? matcher : null;
    }
}
