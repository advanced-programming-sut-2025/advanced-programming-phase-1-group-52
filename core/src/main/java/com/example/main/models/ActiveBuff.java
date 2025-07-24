package com.example.main.models;

import com.example.main.enums.player.Skills;

public class ActiveBuff {

    public enum BuffType {
        MAX_ENERGY,
        SKILL
    }

    private final BuffType type;
    private final Skills skill;
    private int duration;

    public ActiveBuff(BuffType type, Skills skill, int duration) {
        this.type = type;
        this.skill = skill;
        this.duration = duration;
    }

    public void decrementDuration() {
        this.duration--;
    }

    public boolean isExpired() {
        return duration <= 0;
    }

    public BuffType getType() {
        return type;
    }

    public Skills getSkill() {
        return skill;
    }

    public int getDuration() {
        return duration;
    }
}
