package models.item;

import enums.items.FishType;
import enums.items.MaterialType;
import enums.items.ToolType;
import enums.design.Season;
import enums.design.Weather;
import enums.player.Skills;
import models.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class  Fish extends Item {
    private double quality;
    private boolean legendary;

    public Fish(MaterialType material) {
        super(material.name(), material);
    }

    public double getQuality() {
        return quality;
    }

    public boolean isLegendary() {
        return legendary;
    }

    @Override
    protected int calculateEnergyConsumption() {
        return 0;
    }

    public static List<Fish> catchFish(Player player, Season season, Weather weather, ToolType pole) {
        int skill = player.getSkillLevel(Skills.Fishing);
        double R = 1.0;
        double M = switch (weather) {
            case Sunny -> 0.5;
            case Rainy -> 1.2;
            case Stormy -> 1.0;
            default -> 1.0;
        };
        int rawMax = (int) Math.floor(R * M * (skill + 2));
        int maxCount = Math.min(6, Math.max(1, rawMax));
        Random rnd = new Random();
        int count = rnd.nextInt(maxCount) + 1;
        double poleFactor = switch (pole) {
            case EducationalFishingPole -> 1.0;
            case BambooFishingPole -> 0.5;
            case FiberglassFishingPole -> 0.9;
            case IridiumFishingPole -> 1.2;
            default -> 1.0;
        };
        double qualityScore = R * (skill + 2) * poleFactor / (7.0 - M);
        boolean includeLegendary = skill == player.skills.get(Skills.Fishing).getLevel() && skill >= 10;
        List<FishType> possible = Arrays.stream(FishType.values())
                .filter(f -> f.getSeason() == season)
                .filter(f -> includeLegendary || f.getType().equals("Ordinary"))
                .collect(Collectors.toList());
        List<Fish> caught = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            FishType fishType = possible.get(rnd.nextInt(possible.size()));
            Fish fish = new Fish(MaterialType.valueOf(fishType.name()));
            fish.setNumber(1);
            fish.quality = qualityScore;
            fish.legendary = fishType.getType().equals("Legendary");
            caught.add(fish);
        }
        int energyCost = pole.getEnergyConsumption() * count;
        player.setEnergy(player.energy() - energyCost);
        player.addSkillExperience(Skills.Fishing, count * 10);
        return caught;
    }
}
