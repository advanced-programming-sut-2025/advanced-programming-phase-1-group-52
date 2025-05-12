package enums.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum GameCommands {
    CookingRefrigerator("^(\\s*)cooking(\\s+)refrigerator(\\s+)(put|pick)(\\s+)(?<item>\\S+)(\\s*)$"),
    CookingShowRecipes("^(\\s*)cooking(\\s+)show(\\s+)recipes(\\s*)$"),
    CookingPrepare("^(\\s*)cooking(\\s+)prepare(\\s+)(?<recipe_name>\\S+)(\\s*)$"),
    Eat("^(\\s*)eat(\\s+)(?<food_name>\\S+)(\\s*)$"),
    BuildBarnOrCoop("^(\\s*)build(\\s+)-a(\\s+)(?<building_name>.+?)(\\s+)" +
            "-l(\\s*)(?<x>\\d+),(\\s*)(?<y>\\d+)(\\s*)$"),
    BuyAnimal("^(\\s*)buy(\\s+)animal(\\s+)-a(?<animal>[\\S\\s]+)(\\s+)-n(\\s+)(?<name>\\S+)(\\s*)$"),
    PettingAnAnimal("^(\\s*)pet(\\s+)-n(\\s+)(?<name>\\S+)(\\s*)$"),
    Animals("^(\\s*)animals(\\s*)$"),
    ShepherdAnimals("^(\\s*)shepherd(\\s+)animals(\\s+)-n(\\s+)(?<animal name>[\\S\\s+])(\\s+)" +
            "-l(\\s*)(?<x>\\d+),(\\s*)(?<y>\\d+)(\\s*)$"),
    FeedHay("^(\\s*)feed(\\s+)hay(\\s+)-n(\\s+)(?<animal name>\\S+)(\\s*)$"),
    AnimalProduces("^(\\s*)produces(\\s*)$"),
    AnimalCollectProduce("^(\\s*)collect(\\s+)produce(\\s+)-n(\\s+)(?<name>\\S+)(\\s*)$"),
    SellAnimal("^(\\s*)sell(\\s+)animal(\\s+)-n(\\s+)(?<name>\\S+)(\\s*)$"),
    Fishing("^(\\s*)fishing(\\s+)-p(\\s+)(?<fishing pole>\\S+)(\\s*)$"),
    ArtisanUse("^(\\s*)artisan(\\s+)use(\\s+)(?<artisan_name>\\S+)(\\s+)(?<item1_name>\\S+)(\\s*)$"),
    ArtisanGet("^(\\s*)artisan(\\s+)get(\\s+)(?<artisan_name>\\S+)(\\s*)$")
    ;

    private final String pattern;

    GameCommands(String pattern) {
        this.pattern = pattern;
    }

    public Matcher getMatcher(String input) {
        Matcher matcher = Pattern.compile(this.pattern).matcher(input);
        return matcher.matches() ? matcher : null;
    }
}
