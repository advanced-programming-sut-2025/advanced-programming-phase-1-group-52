package com.example.main.views;

import java.util.Scanner;
import java.util.regex.Matcher;

import com.example.main.controller.GameMenuController;
import com.example.main.enums.regex.GameMenuCommands;

public class GameMenu implements AppMenu {
    GameMenuController controller = new GameMenuController();
    @Override
    public void checkInput(Scanner scanner) {
        String input = scanner.nextLine();
        Matcher matcher;

        if ((matcher = GameMenuCommands.CreateNewGame.getMatcher(input)) != null) {
            System.out.println(controller.startNewGame(input).Message());
        }
        else if ((matcher = GameMenuCommands.GameMap.getMatcher(input)) != null) {
            System.out.println(controller.mapSelector(matcher.group("map1"),matcher.group("map2"),matcher.group("map3"),matcher.group("map4")).Message());
        }
        else if ((matcher = GameMenuCommands.ExitGame.getMatcher(input)) != null) {
            System.out.println(controller.exitGame().Message());
        }
        else if ((matcher = GameMenuCommands.NextTurn.getMatcher(input)) != null) {
            System.out.println(controller.switchTurn().Message());
        }
        else if ((matcher = GameMenuCommands.Time.getMatcher(input)) != null) {
            System.out.println(controller.showTime().Message());
        }
        else if ((matcher = GameMenuCommands.Date.getMatcher(input)) != null) {
            System.out.println(controller.showDate().Message());
        }
        else if ((matcher = GameMenuCommands.DateTime.getMatcher(input)) != null) {
            System.out.println(controller.showDateAndTime().Message());
        }
        else if ((matcher = GameMenuCommands.DayOfTheWeek.getMatcher(input)) != null) {
            System.out.println(controller.showDayOfWeek().Message());
        }
        else if ((matcher = GameMenuCommands.CHEATAdvanceTime.getMatcher(input)) != null) {
            System.out.println(controller.changeTime(matcher.group("amount")).Message());
        }
        else if ((matcher = GameMenuCommands.CHEATAdvanceDate.getMatcher(input)) != null) {
            System.out.println(controller.changeDate(matcher.group("amount")).Message());
        }
        else if ((matcher = GameMenuCommands.Season.getMatcher(input)) != null) {
            System.out.println(controller.showSeason().Message());
        }
        else if ((matcher = GameMenuCommands.CHEATLightning.getMatcher(input)) != null) {
            System.out.println(controller.cheatLightning(matcher.group("x"), matcher.group("y")).Message());
        }
        else if ((matcher = GameMenuCommands.Weather.getMatcher(input)) != null) {
            System.out.println(controller.showWeather().Message());
        }
        else if ((matcher = GameMenuCommands.WeatherForecast.getMatcher(input)) != null) {
            System.out.println(controller.showTomorrowWeather().Message());
        }
        else if ((matcher = GameMenuCommands.CHEATWeather.getMatcher(input)) != null) {
            System.out.println(controller.changeTomorrowWeather(matcher.group("weather")).Message());
        }
        else if ((matcher = GameMenuCommands.GreenHouseBuilding.getMatcher(input)) != null) {
            System.out.println(controller.buildGreenHouse().Message());
        }
        else if ((matcher = GameMenuCommands.Walk.getMatcher(input)) != null) {
            System.out.println(controller.walk(matcher.group("x"), matcher.group("y")).Message());
        }
        else if ((matcher = GameMenuCommands.PrintMap.getMatcher(input)) != null) {
            System.out.println(controller.printMap(matcher.group("x"),matcher.group("y"), matcher.group("size")).Message());
        }
        else if ((matcher = GameMenuCommands.HelpMap.getMatcher(input)) != null) {
            System.out.println(controller.mapInfo().Message());
        }
        else if ((matcher = GameMenuCommands.EnergyShow.getMatcher(input)) != null) {
            System.out.println(controller.energyShow().Message());
        }
        else if ((matcher = GameMenuCommands.CHEATEnergySet.getMatcher(input)) != null) {
            //System.out.println(controller.cheatSetEnergy(matcher.group("value")).Message());
        }
        else if ((matcher = GameMenuCommands.CHEATEnergyUnlimited.getMatcher(input)) != null) {
            System.out.println(controller.cheatUnlimitedEnergy().Message());
        }
        else if((matcher = GameMenuCommands.ShowPlayerCoordinates.getMatcher(input)) != null) {
            System.out.println(controller.showPlayerCoordinates().Message());
        }
        else if ((matcher = GameMenuCommands.InventoryShow.getMatcher(input)) != null) {
            System.out.println(controller.showInventoryItems().Message());
        }
//        else if ((matcher = GameMenuCommands.RemoveInventoryItems.getMatcher(input)) != null) {
//            System.out.println(controller.removeItemFromInventory(matcher.group("name"),matcher.group("number")).Message());
//        }
        else if ((matcher = GameMenuCommands.ToolEquip.getMatcher(input)) != null) {
            System.out.println(controller.equipTool(matcher.group("name")).Message());
        }
        else if ((matcher = GameMenuCommands.ToolShowCurrent.getMatcher(input)) != null) {
            System.out.println(controller.showCurrentTool().Message());
        }
        else if ((matcher = GameMenuCommands.ToolShowAvailable.getMatcher(input)) != null) {
            System.out.println(controller.showAllTools().Message());
        }
        else if ((matcher = GameMenuCommands.ToolUse.getMatcher(input)) != null) {
            System.out.println(controller.useTool(matcher.group("direction")).Message());
        }
        else if((matcher = GameMenuCommands.CheatWalk.getMatcher(input)) != null) {
            System.out.println(controller.cheatWalk(matcher.group("x"), matcher.group("y")).Message());
        }
        else if ((matcher = GameMenuCommands.CraftInfo.getMatcher(input)) != null) {
            System.out.println(controller.craftInfo(matcher.group("name")).Message());
        }
        else if ((matcher = GameMenuCommands.Plant.getMatcher(input)) != null) {
            System.out.println(controller.plant(matcher.group("seed"), matcher.group("direction")).Message());
        }
        else if ((matcher = GameMenuCommands.ShowPlant.getMatcher(input)) != null) {
            System.out.println(controller.showPlant(matcher.group("x"),matcher.group("y")).Message());
        }
        else if ((matcher = GameMenuCommands.Fertilize.getMatcher(input)) != null) {
            System.out.println(controller.fertilize(matcher.group("fertilizer"), matcher.group("direction")).Message());
        }
        else if ((matcher = GameMenuCommands.HowMuchWater.getMatcher(input)) != null) {
            System.out.println(controller.wateringCanFilled().Message());
        }
        else if ((matcher = GameMenuCommands.Eat.getMatcher(input)) != null) {
            System.out.println(controller.eat(matcher.group("foodName")).Message());
        }
        else if ((matcher = GameMenuCommands.PettingAnAnimal.getMatcher(input)) != null) {
            System.out.println(controller.petAnimal(matcher.group("name")));
        }
        else if ((matcher = GameMenuCommands.CHEATSetAnimalFriendship.getMatcher(input)) != null) {
            System.out.println(controller.cheatSetAnimalFriendship(matcher.group("animalName"), matcher.group("amount")));
        }
        else if ((matcher = GameMenuCommands.Animals.getMatcher(input)) != null) {
            System.out.println(controller.showAnimals());
        }
        else if ((matcher = GameMenuCommands.ShepherdAnimals.getMatcher(input)) != null) {
            System.out.println(controller.shepherdAnimal(matcher.group("animalName"), matcher.group("x"), matcher.group("y")));
        }
        else if ((matcher = GameMenuCommands.FeedHay.getMatcher(input)) != null) {
            System.out.println(controller.feedHay(matcher.group("animalName")));
        }
        else if ((matcher = GameMenuCommands.AnimalProduces.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.AnimalCollectProduce.getMatcher(input)) != null) {

        }
        else if ((matcher = GameMenuCommands.SellAnimal.getMatcher(input)) != null) {
            System.out.println(controller.sellAnimal(matcher.group("name")));
        }
        else if ((matcher = GameMenuCommands.Fishing.getMatcher(input)) != null) {
            System.out.println(controller.fishing(matcher.group("fishingPole")).Message());
        }
        else if ((matcher = GameMenuCommands.ArtisanUse.getMatcher(input)) != null) {
            System.out.println(controller.useArtisanMachine(matcher.group("artisanName"),matcher.group("itemName")).Message());
        }
        else if ((matcher = GameMenuCommands.CHEATAddBalance.getMatcher(input)) != null) {
            System.out.println(controller.cheatAddBalance(matcher.group("amount")).Message());
        }
        else if ((matcher = GameMenuCommands.Sell.getMatcher(input)) != null) {
            System.out.println(controller.sell(matcher.group("productName"), matcher.group("count")));
        }
        else if ((matcher = GameMenuCommands.ArtisanGet.getMatcher(input)) != null) {
            System.out.println(controller.getArtisanProduct(matcher.group("artisanName")).Message());
        }
        else if ((matcher = GameMenuCommands.PlaceItem.getMatcher(input)) != null) {
            System.out.println(controller.placeItem(matcher.group("itemName"), matcher.group("direction")).Message());
        }
        else if ((matcher = GameMenuCommands.CHEATAddItem.getMatcher(input)) != null) {
            System.out.println(controller.cheatAddItem(matcher.group("itemName"), matcher.group("count")).Message());
        }
        else if ((matcher = GameMenuCommands.Friendships.getMatcher(input)) != null) {
            System.out.println(controller.showAllFriendShips().Message());
        }
        else if ((matcher = GameMenuCommands.Talk.getMatcher(input)) != null) {
            System.out.println(controller.talk(matcher.group("username"), matcher.group("message")).Message());
        }
        else if ((matcher = GameMenuCommands.TalkHistory.getMatcher(input)) != null) {
            System.out.println(controller.talkHistory(matcher.group("username")).Message());
        }
        else if ((matcher = GameMenuCommands.Gift.getMatcher(input)) != null) {
            System.out.println(controller.giftPlayer(matcher.group("username"), matcher.group("item"), matcher.group("amount")).Message());
        }
        else if ((matcher = GameMenuCommands.GiftList.getMatcher(input)) != null) {
            System.out.println(controller.showGiftsList().Message());
        }
        else if ((matcher = GameMenuCommands.GiftRate.getMatcher(input)) != null) {
            System.out.println(controller.rateGift(matcher.group("id"), matcher.group("rate")).Message());
        }
        else if ((matcher = GameMenuCommands.GiftHistory.getMatcher(input)) != null) {
            System.out.println(controller.showGiftHistoryWith("username").Message());
        }
        else if ((matcher = GameMenuCommands.Hug.getMatcher(input)) != null) {
            System.out.println(controller.hug(matcher.group("username")).Message());
        }
        else if ((matcher = GameMenuCommands.Flower.getMatcher(input)) != null) {
            System.out.println(controller.flowerSomeone(matcher.group("username")).Message());
        }
        else if ((matcher = GameMenuCommands.AskMarriage.getMatcher(input)) != null) {
            System.out.println(controller.askMarriage(matcher.group("username")).Message());
        }
        else if ((matcher = GameMenuCommands.MarriageResponse.getMatcher(input)) != null) {
            System.out.println(controller.respondToMarriage(matcher.group("response"), matcher.group("username")).Message());
        }
        else if ((matcher = GameMenuCommands.StartTrade.getMatcher(input)) != null) {
            System.out.println(controller.goToTradeMenu().Message());
        }
        else if ((matcher = GameMenuCommands.MeetNPC.getMatcher(input)) != null) {
            System.out.println(controller.meetNPC(matcher.group("name")).Message());
        }
        else if ((matcher = GameMenuCommands.GiftNPC.getMatcher(input)) != null) {
            System.out.println(controller.giftNPC(matcher.group("name"), matcher.group("item")).Message());
        }
        else if ((matcher = GameMenuCommands.FriendshipNPCList.getMatcher(input)) != null) {
            System.out.println(controller.showNPCFriendships().Message());
        }
        else if ((matcher = GameMenuCommands.QuestsList.getMatcher(input)) != null) {
            System.out.println(controller.showQuests().Message());
        }
        else if ((matcher = GameMenuCommands.QuestsFinish.getMatcher(input)) != null) {
            System.out.println(controller.finishQuest(matcher.group("id")).Message());
        }
        else if ((matcher = GameMenuCommands.ShowCurrentMenu.getMatcher(input)) != null) {
            System.out.println(controller.showCurrentMenu().Message());
        }
        else if ((matcher = GameMenuCommands.MenuExit.getMatcher(input)) != null) {
            controller.menuExit();
        }
        else if((matcher = GameMenuCommands.GotoHomeMenu.getMatcher(input)) != null) {
            System.out.println(controller.gotoHomeMenu().Message());
        }
        else if((matcher = GameMenuCommands.TreeInfo.getMatcher(input)) != null) {
            System.out.println(controller.treeInfo(matcher.group("name")).Message());
        }
        else if((matcher = GameMenuCommands.ExitGame.getMatcher(input)) != null) {
            System.out.println(controller.exitGame().Message());
        }
        else if((matcher = GameMenuCommands.ShowExtractionSkill.getMatcher(input)) != null) {
            System.out.println(controller.showExtractionSkill().Message());
        }
        else if((matcher = GameMenuCommands.ShowForagingSkill.getMatcher(input)) != null) {
            System.out.println(controller.showForagingSkill().Message());
        }
        else if((matcher = GameMenuCommands.ShowFarmingSkill.getMatcher(input)) != null) {
            System.out.println(controller.showFarmingSkill().Message());
        }
        else if ((matcher = GameMenuCommands.ShowFishingSkill.getMatcher(input)) != null) {
            System.out.println(controller.showFishingSkill().Message());
        }
        else if((matcher = GameMenuCommands.PickItem.getMatcher(input)) != null) {
            System.out.println(controller.pickItem(matcher.group("direction")).Message());
        }
        else if((matcher = GameMenuCommands.Plant.getMatcher(input)) != null) {
            System.out.println(controller.plant(matcher.group("seed"), matcher.group("direction")).Message());
        }
        else if((matcher = GameMenuCommands.CheatShowTargetTileType.getMatcher(input)) != null) {
            System.out.println(controller.cheatTileType(matcher.group("direction")).Message());
        }
        else if ((matcher = GameMenuCommands.InventoryCheck.getMatcher(input)) != null) {
            System.out.println(controller.checkInventoryIntegrity().Message());
        }
        else if ((matcher = GameMenuCommands.QuestCheck.getMatcher(input)) != null) {
            System.out.println(controller.checkQuestIntegrity().Message());
        }
        else if ((matcher = GameMenuCommands.DebugQuest.getMatcher(input)) != null) {
            System.out.println(controller.debugQuest(matcher.group("id")).Message());
        }
        else if ((matcher = GameMenuCommands.DebugQuestVisibility.getMatcher(input)) != null) {
            System.out.println(controller.debugQuestVisibility().Message());
        }
        else if ((matcher = GameMenuCommands.DebugTalkHistory.getMatcher(input)) != null) {
            System.out.println(controller.debugTalkHistory().Message());
        }
        else if ((matcher = GameMenuCommands.ShowBalance.getMatcher(input)) != null) {
            System.out.println(controller.showBalance().Message());
        }
        else if ((matcher = GameMenuCommands.GoToStoreMenu.getMatcher(input)) != null) {
            System.out.println(controller.goToStoreMenu().Message());
        }
        else {
            System.out.println("Invalid command");
        }

    }
}
