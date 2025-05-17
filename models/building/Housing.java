package models.building;

import enums.items.CageType;
import java.util.ArrayList;
import java.util.List;
import models.PurchasedAnimal;

public class Housing {
    private final int id;
    private final CageType type;
    private final int capacity;
    private final List<PurchasedAnimal> occupants;

    public Housing(int id, CageType type) {
        this.id = id;
        this.type = type;
        this.capacity = type.getCapacity();
        this.occupants = new ArrayList<>();
    }

    public int getId() { return id; }
    public CageType getType() { return type; }
    public int getCapacity() { return capacity; }
    public List<PurchasedAnimal> getOccupants() { return occupants; }

    public boolean addAnimal(PurchasedAnimal animal) {
        if (occupants.size() < capacity) {
            occupants.add(animal);
            return true;
        }
        return false;
    }
}
