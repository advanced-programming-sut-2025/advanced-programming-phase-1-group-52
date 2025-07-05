package com.example.main.models.building;

import java.util.ArrayList;

import com.example.main.enums.items.CageType;
import com.example.main.models.item.PurchasedAnimal;

public class Housing {
    private final int id;
    private final CageType type;
    private final int capacity;
    private final ArrayList<PurchasedAnimal> occupants;
    private final int cornerX;
    private final int cornerY;

    public Housing(int id, CageType type, int x, int y) {
        this.id = id;
        this.type = type;
        this.cornerX = x;
        this.cornerY = y;
        this.capacity = type.getCapacity();
        this.occupants = new ArrayList<>();
    }

    public int getX() {
        return this.cornerX;
    }

    public int getY() {
        return this.cornerY;
    }

    public int getId() { return id; }
    public CageType getType() { return type; }
    public int getCapacity() { return capacity; }
    public ArrayList<PurchasedAnimal> getOccupants() { return occupants; }

    public boolean addAnimal(PurchasedAnimal animal) {
        if (occupants.size() < capacity) {
            occupants.add(animal);
            return true;
        }
        return false;
    }

    public void removeAnimal(PurchasedAnimal animal) {
        this.occupants.remove(animal);
    }
}
