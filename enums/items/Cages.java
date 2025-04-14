package enums.items;

public enum Cages implements Items {
    // Cages:
    NormalCage(4),
    BigCage(8),
    DeluxeCage(12),

    // Stables:
    NormalStable(4),
    BigStable(8),
    DeluxeStable(12);

    private final int capacity;

    Cages(int capacity) {
        this.capacity = capacity;
    }

}

