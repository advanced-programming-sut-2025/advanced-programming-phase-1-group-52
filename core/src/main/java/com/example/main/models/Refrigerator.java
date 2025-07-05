package com.example.main.models;

import java.util.HashMap;
import java.util.Map;

import com.example.main.enums.items.MaterialType;

public class Refrigerator {
    private final Map<MaterialType, Integer> storage = new HashMap<>();

    public Refrigerator() {}

    public boolean putMaterial(MaterialType material, int quantity) {
        if (quantity <= 0) return false;
        storage.put(material, storage.getOrDefault(material, 0) + quantity);
        return true;
    }

    public boolean pickMaterial(MaterialType material, int quantity) {
        if (quantity <= 0) return false;
        int current = storage.getOrDefault(material, 0);
        if (current < quantity) return false;
        if (current == quantity) storage.remove(material);
        else storage.put(material, current - quantity);
        return true;
    }

    public boolean hasMaterial(MaterialType material, int quantity) {
        return quantity > 0 && storage.getOrDefault(material, 0) >= quantity;
    }

    public Map<MaterialType, Integer> getContents() {
        return new HashMap<>(storage);
    }
}
