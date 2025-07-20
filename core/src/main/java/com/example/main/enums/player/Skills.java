package com.example.main.enums.player;

public enum Skills {
    Farming("Farming skill"),
    Mining("Mining skill"),
    Foraging("Foraging skill"),
    Fishing("Fishing skill");

    private String skillDescription;
    private int level;
    private int experience;

    Skills(String skillName){
        this.skillDescription = skillName;
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

    public String getSkillDescription() {
        return skillDescription;
    }

    public void setSkillDescription(String skillDescription) {
        this.skillDescription = skillDescription;
    }

    private void checkLevelUp(){
        int expNeeded = getExpForNextLevel();
        if(this.experience >= expNeeded){
            this.level++;
            this.experience -= expNeeded;
        }
    }

    public int getExpForNextLevel() {
        return (100*(level+1) + 50);
    }

}
