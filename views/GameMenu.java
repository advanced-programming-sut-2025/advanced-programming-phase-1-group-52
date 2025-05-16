package views;

import controllers.GameMenuController;
import enums.regex.GameMenuCommands;
import java.util.Scanner;
import java.util.regex.Matcher;

public class GameMenu implements AppMenu {
    GameMenuController controller = new GameMenuController();
    @Override
    public void checkInput(Scanner scanner) {
        String input = scanner.nextLine();
        Matcher matcher;

        if ((matcher = GameMenuCommands.CheckFlagInNewGame.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.ExtractUsernames.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.GameMap.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.ExitGame.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.NextTurn.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.Time.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.Date.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.DateTime.getMatcher(input)) != null) {
            
        }
        else if ((matcher = GameMenuCommands.DayOfTheWeek.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.CHEATAdvanceTime.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.CHEATAdvanceDate.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.Season.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.CHEATLightning.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.Weather.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.WeatherForecast.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.CHEATWeather.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.GreenHouseBuilding.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.Walk.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.PrintMap.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.HelpMap.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.EnergyShow.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.CHEATEnergySet.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.CHEATEnergyUnlimited.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.InventoryShow.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.RemoveInventoryItems.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.ToolEquip.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.ToolShowCurrent.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.ToolShowAvailable.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.ToolUse.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.CraftInfo.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.Plant.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.ShowPlant.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.Fertilize.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.HowMuchWater.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.Eat.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.PettingAnAnimal.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.CHEATSetAnimalFriendship.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.Animals.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.ShepherdAnimals.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.FeedHay.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.AnimalProduces.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.AnimalCollectProduce.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.SellAnimal.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.Fishing.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.ArtisanUse.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.CHEATAddBalance.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.Sell.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.ArtisanGet.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.PlaceItem.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.CHEATAddItem.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.Friendships.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.Talk.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.TalkHistory.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.Gift.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.GiftList.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.GiftRate.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.GiftHistory.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.Hug.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.Flower.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.AskMarriage.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.MarriageResponse.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.StartTrade.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.MeetNPC.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.GiftNPC.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.FriendshipNPCList.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.QuestsList.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.QuestsFinish.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.ShowCurrentMenu.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.MenuExit.getMatcher(input)) != null) {

        }

    }
}
