package models.item;

import enums.items.*;
import java.util.*;
import java.util.function.BiFunction;

public class ItemFactory {
    // Map each Type class to its corresponding Item constructor
    private static final Map<Class<? extends ItemType>, BiFunction<ItemType, Integer, Item>> ITEM_CONSTRUCTORS =
            new HashMap<>();

    static {
        // Register constructors for all ItemTypes with quantity support
        ITEM_CONSTRUCTORS.put(CropType.class, (type, number) -> new Crop((CropType) type, number));
        ITEM_CONSTRUCTORS.put(ForagingSeedType.class, (type, number) -> new Seed((ForagingSeedType) type, number));
        ITEM_CONSTRUCTORS.put(MineralType.class, (type, number) -> new Mineral((MineralType) type, number));
        ITEM_CONSTRUCTORS.put(ToolType.class, (type, number) -> new Tool((ToolType) type, number));
        ITEM_CONSTRUCTORS.put(FishType.class, (type, number) -> new Fish((FishType) type, number));
        ITEM_CONSTRUCTORS.put(TrashCanType.class, (type, number) -> new TrashCan((TrashCanType) type, number));
    }

    // List all enum classes that implement Type
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
            TrashCanType.class
    );

    /**
     * Creates an Item by name with specified quantity (returns Optional)
     */
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

    /**
     * Finds an Type by name across all enums
     */
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

    /**
     * Creates an Item by name with specified quantity (throws exception if not found)
     */
    public static Item createItemOrThrow(String name, int number) {
        return createItem(name, number)
                .orElseThrow(() -> new IllegalArgumentException("No item found with name: " + name));
    }
}