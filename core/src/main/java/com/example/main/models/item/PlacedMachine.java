package com.example.main.models.item;

import com.example.main.enums.items.ArtisanProductType;
import com.example.main.enums.items.CraftingMachineType;
import com.example.main.models.item.Good;
import com.example.main.models.item.Item;

public class PlacedMachine {
    private final CraftingMachineType machineType;
    private Item input;
    private Good output;
    private int progress = 0; // In 10-minute game intervals
    private ArtisanProductType currentRecipe;

    public PlacedMachine(CraftingMachineType machineType) {
        this.machineType = machineType;
    }

    public void startProcessing(Item input, ArtisanProductType recipe) {
        this.input = input;
        this.currentRecipe = recipe;
        this.progress = 0;
    }

    public void updateProgress() {
        if (isProcessing()) {
            progress++;
            if (progress >= currentRecipe.getProcessingTime()) {
                this.output = new Good(currentRecipe, 1);
                this.input = null; // Consume input
            }
        }
    }

    public void cancelProcessing() {
        this.input = null;
        this.currentRecipe = null;
        this.progress = 0;
    }

    public Good collectProduct() {
        if (isDone()) {
            Good collected = this.output;
            this.output = null;
            this.currentRecipe = null;
            this.progress = 0;
            return collected;
        }
        return null;
    }

    public boolean isProcessing() { return input != null && output == null; }
    public boolean isDone() { return output != null; }
    public boolean isIdle() { return input == null && output == null; }
    public CraftingMachineType getMachineType() { return machineType; }
    public Item getInput() { return input; }
    public Good getOutput() { return output; }
    public int getProgress() { return progress; }
    public ArtisanProductType getCurrentRecipe() { return currentRecipe; }
}
