package com.example.main.models.item;

import com.example.main.enums.items.*;
import java.util.*;
import java.util.function.BiFunction;

public class ItemFactory {
    private static final Map<Class<? extends ItemType>, BiFunction<ItemType, Integer, Item>> ITEM_CONSTRUCTORS =
            new HashMap<>();

    static {
        ITEM_CONSTRUCTORS.put(CropType.class, (type, number) -> new Crop((CropType) type, number));
        ITEM_CONSTRUCTORS.put(ForagingSeedType.class, (type, number) -> new Seed((ForagingSeedType) type, number));
        ITEM_CONSTRUCTORS.put(MineralType.class, (type, number) -> new Mineral((MineralType) type, number));
        ITEM_CONSTRUCTORS.put(ToolType.class, (type, number) -> new Tool((ToolType) type, number));
        ITEM_CONSTRUCTORS.put(FishType.class, (type, number) -> new Fish((FishType) type, number));
        ITEM_CONSTRUCTORS.put(TrashCanType.class, (type, number) -> new TrashCan((TrashCanType) type, number));
        ITEM_CONSTRUCTORS.put(CraftingRecipes.class, (type, number) -> new CraftingRecipe((CraftingRecipes) type, number));
        ITEM_CONSTRUCTORS.put(CraftingMachineType.class, (type, number) -> new CraftingMachine((CraftingMachineType) type, number));
        ITEM_CONSTRUCTORS.put(MaterialType.class, (type, number) -> new Material((MaterialType) type, number));
        ITEM_CONSTRUCTORS.put(ArtisanProductType.class, (type, number) -> new Good((ArtisanProductType) type, number));
        ITEM_CONSTRUCTORS.put(FoodType.class, (type, number) -> new Food((FoodType) type, number));
        ITEM_CONSTRUCTORS.put(AnimalProductType.class, (type, number) -> new AnimalProduct((AnimalProductType) type, number));
        ITEM_CONSTRUCTORS.put(FruitType.class, (type, number) -> new Fruit((FruitType) type, number));
        ITEM_CONSTRUCTORS.put(ToolType.class, (type, number) -> {
            ToolType toolType = (ToolType) type;
            if (toolType.name().contains("Watering_Can")) {
                return new WateringCan(toolType, number);
            }
            return new Tool(toolType, number);
        });
    }

    private static final List<Class<? extends Enum<?>>> ITEM_ENUMS = Arrays.asList(
        CropType.class,
        ForagingCropType.class,
        ForagingSeedType.class,
        MineralType.class,
        ToolType.class,
        TreeType.class,
        AnimalType.class,
        AnimalProductType.class,
        Backpacks.class,
        CageType.class,
        FishType.class,
        TrashCanType.class,
        CraftingRecipes.class,
        CraftingMachineType.class,
        MaterialType.class,
        ArtisanProductType.class,
        FoodType.class,
        FruitType.class
    );

    public static Optional<Item> createItem(String name, int number) {
        return findItemTypeByName(name)
                .map(type -> {
                    BiFunction<ItemType, Integer, Item> constructor = ITEM_CONSTRUCTORS.get(type.getClass());
                    if (constructor == null) {
                        throw new IllegalStateException("No constructor registered for: " + type.getClass());
                    }
                    return constructor.apply(type, number);
                });
    }

    private static Optional<ItemType> findItemTypeByName(String name) {
        for (Class<? extends Enum<?>> enumClass : ITEM_ENUMS) {
            if (ItemType.class.isAssignableFrom(enumClass)) {
                Optional<ItemType> result = Arrays.stream(enumClass.getEnumConstants())
                        .map(e -> (ItemType) e)
                        .filter(item -> item.getName().equalsIgnoreCase(name))
                        .findFirst();
                if (result.isPresent()) return result;
            }
        }
        return Optional.empty();
    }

    public static Item createItemOrThrow(String name, int number) {
        return createItem(name, number)
                .orElseThrow(() -> new IllegalArgumentException("No item found with name: " + name));
    }
}
