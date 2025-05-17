package models;

public class SkillData {
    private int level;
    private int experience;

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
}
