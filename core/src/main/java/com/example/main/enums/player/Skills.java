package com.example.main.enums.player;

public enum Skills {
    Farming("Farming"),
    Extraction("Extraction"),
    Foraging("Foraging"),
    Fishing("Fishing");

    private String skillName;
    private int level;
    private int experience;

    Skills(String skillName){
        this.skillName = skillName;
        this.level = 0;
        this.experience = 0;
    }

    public void addExperience(int experience){
        this.experience += experience;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    private void checkLevelUp(){
        int expNeeded = getExpForNextLevel();
        if(this.experience >= expNeeded){
            this.level++;
            this.experience -= expNeeded;
        }
    }

    private int getExpForNextLevel() {
        return (100*(level+1) + 50);
    }

}
