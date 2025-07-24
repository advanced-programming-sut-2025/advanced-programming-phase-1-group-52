package com.example.main.models;

public class SkillData {
    private int level;
    private int experience;
    private int originalLevel = -1;
    private boolean isBuffed = false;

    public SkillData() {
        this.experience = 0;
        this.level = 1;
    }

    public void addExperience(int amount) {
        this.experience += amount;
    }

    public void levelUp() {
        this.level++;
    }

    public void deductExperience(int amount) {
        this.experience -= amount;
    }

    public int getLevel() { return level; }

    public int getExperience() { return experience; }

    public boolean isBuffed() {
        return isBuffed;
    }

    public void applyBuff(int tempLevel) {
        if (!isBuffed) {
            this.originalLevel = this.level;
            this.isBuffed = true;
        }
        this.level = tempLevel; // Set to the new buffed level
    }

    public void removeBuff() {
        if (isBuffed) {
            this.level = this.originalLevel;
            this.originalLevel = -1;
            this.isBuffed = false;
        }
    }
}
