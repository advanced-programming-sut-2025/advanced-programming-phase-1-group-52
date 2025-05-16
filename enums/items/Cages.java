package enums.items;

public enum Cages implements ItemType {
    // Cages:
    NormalCage("normal cage", 4),
    BigCage("big cage", 8),
    DeluxeCage("deluxe cage", 12),

    // Stables:
    NormalStable("normal stable", 4),
    BigStable("big stable", 8),
    DeluxeStable("deluxe stable", 12);

    private final String name;
    private final int capacity;

    Cages(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
    }

    @Override
    public boolean isTool() {
        return false;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public int getCapacity() { return this.capacity; }
}

