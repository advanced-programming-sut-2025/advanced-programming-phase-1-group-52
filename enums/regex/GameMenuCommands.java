package enums.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum GameMenuCommands {
    CheckFlagInNewGame("^game new -u [\\S\\s]+"),
    CreateNewGame("^game new -u (?<username1>[\\s\\S]*) (?<username2>[\\s\\S]*) (?<username3>[\\s\\S]*)$"),
    GameMap("user maps: -user1 (?<map1>[\\S]+) -user2 (?<map2>[\\S]+) -user3 (?<map3>[\\S]+) -user4 (?<map4>[\\S]+)"),
    ExitGame("exit game"),
    NextTurn("next turn"),
    Time("time"),
    Date("date"),
    DateTime("datetime"),
    DayOfTheWeek("day of the week"),
    CHEATAdvanceTime("cheat advance time (?<amount>\\d+) h"),
    CHEATAdvanceDate("cheat advance date (?<amount>\\d+) d"),
    Season("season"),
    CHEATLightning("cheat Thor -l (?<x>\\d+), (?<y>\\d+)"),
    Weather("weather"),
    WeatherForecast("weather forecast"),
    CHEATWeather("cheat weather set (?<weather>\\S+)"),
    GreenHouseBuilding("greenhouse build"),
    Walk("walk -l (?<x>\\d+), (?<y>\\d+)"),
    CheatWalk("cheat walk -l (?<x>\\d+), (?<y>\\d+)"),
    PrintMap("print map -l (?<x>\\d+), (?<y>\\d+) -s (?<size>\\d+)"),
    HelpMap("help reading map"),
    EnergyShow("energy show"),
    CHEATEnergySet("energy set -v (?<value>\\d+)"),
    CHEATEnergyUnlimited("energy unlimited"),
    ShowPlayerCoordinates("show player place"),
    InventoryShow("inventory show"),
    RemoveInventoryItems("^inventory trash -i (?<name>[\\S\\s]*)\\s*(?:-n (?<number>[\\d]+))?\\s*$"),
    ToolEquip("tools equip (?<name>[\\S\\s]+)"),
    ToolShowCurrent("tools show current"),
    ToolShowAvailable("tools show available"),
    ToolUse("tools use -d (?<direction>\\S+)"),
    CraftInfo("craftinfo -n (?<name>[\\S\\s]+)"),
    Plant("plant -s (?<seed>[\\S\\s]+) -d (?<direction>\\S+)"),
    ShowPlant("showplant -l (?<x>\\d+), (?<y>\\d+)"),
    Fertilize("fertilize -f (?<fertilizer>[\\S\\s]+) -d (?<direction>\\S+)"),
    HowMuchWater("howmuch water"),
    Eat("^(\\s*)eat(\\s+)(?<foodName>[\\S\\s]+)(\\s*)$"),
    PettingAnAnimal("^(\\s*)pet(\\s+)-n(\\s+)(?<name>[\\S\\s]+)(\\s*)$"),
    CHEATSetAnimalFriendship("cheat set friendship -n (?<animalName>[\\S\\s]+) -c (?<amount>\\d+)"),
    Animals("animals"),
    ShepherdAnimals("^(\\s*)shepherd(\\s+)animals(\\s+)-n(\\s+)(?<animalName>[\\S\\s+])(\\s+)" +
            "-l(\\s*)(?<x>\\d+),(\\s*)(?<y>\\d+)(\\s*)$"),
    FeedHay("^(\\s*)feed(\\s+)hay(\\s+)-n(\\s+)(?<animalName>[\\S\\s]+)(\\s*)$"),
    AnimalProduces("^(\\s*)produces(\\s*)$"),
    AnimalCollectProduce("^(\\s*)collect(\\s+)produce(\\s+)-n(\\s+)(?<name>[\\S\\s]+)(\\s*)$"),
    SellAnimal("^(\\s*)sell(\\s+)animal(\\s+)-n(\\s+)(?<name>[\\S\\s]+)(\\s*)$"),
    Fishing("^(\\s*)fishing(\\s+)-p(\\s+)(?<fishingPole>[\\S\\s]+)(\\s*)$"),
    ArtisanUse("^(\\s*)artisan(\\s+)use(\\s+)(?<artisanName>[\\S\\s]+)(\\s+)(?<itemName>[\\S\\s]+)(\\s*)$"),
    CHEATAddBalance("cheat add (?<amount>\\d+) dollars"),
    Sell("sell (?<productName>[\\S\\s]+) -n (?<count>\\d+)"),
    ArtisanGet("^(\\s*)artisan(\\s+)get(\\s+)(?<artisanName>[\\S\\s]+)(\\s*)$"),
    PlaceItem("place item -n (?<itemName>[\\S\\s]+) -d (?<direction>\\S+)"),
    CHEATAddItem("cheat add item -n (?<itemName>[\\S\\s]+) -c (?<count>\\d+)"),
    Friendships("friendships"),
    Talk("talk -u (?<username>[\\S\\s]+) -m (?<message>[\\S\\s]+)"),
    TalkHistory("talk history -u (?<username>[\\S\\s]+)"),
    Gift("gift -u (?<username>[\\S\\s]+) -i (?<item>[\\S\\s]+) -a (?<amount>\\d+)"),
    GiftList("gift list"),
    GiftRate("gift rate -i (?<id>\\d+) -r (?<rate>\\d+)"),
    GiftHistory("gift history -u (?<username>\\S+)"),
    Hug("hug -u (?<username>\\S+)"),
    Flower("flower -u (?<username>\\S+)"),
    AskMarriage("ask marriage -u (?<username>\\S+)"),
    MarriageResponse("respond -(?<response>accept|reject) -u (?<username>\\S+)"),
    StartTrade("start trade"),
    MeetNPC("meet NPC (?<name>[\\S\\s]+)"),
    GiftNPC("gift NPC (?<name>[\\S\\s]+) -i (<item>[\\S\\s]+)"),
    FriendshipNPCList("friendship NPC list"),
    QuestsList("quests list"),
    QuestsFinish("quests finish -i (?<id>\\d+)"),
    ShowCurrentMenu("show current menu"),
    MenuExit("menu exit"),
    GotoHomeMenu("home menu"),
    TreeInfo("tree info -n (?<name>[\\S\\s]+)"),
    GoToMainMenu("go to main menu"),
    ShowFishingSkill("show fishing skill"),
    ShowExtractionSkill("show extraction skill"),
    ShowForagingSkill("show foraging skill"),
    ShowFarmingSkill("show farming skill"),
    CheatAddRecipe("cheat add recipe -n (?<name>[\\S\\s]+)"),
    PickItem("pick item -d (?<direction>[\\S\\s]+)"),
    RefrigeratorWorking("cooking refrigerator (?<action>(put|pick)) (?<itemName>[\\S\\s]+)"),
    ShowCookingRecipes("cooking show recipes");

    private final String pattern;

    GameMenuCommands(String pattern) {
        this.pattern = pattern;
    }

    public Matcher getMatcher(String input) {
        Matcher matcher = Pattern.compile(this.pattern).matcher(input);
        return matcher.matches() ? matcher : null;
    }
}
