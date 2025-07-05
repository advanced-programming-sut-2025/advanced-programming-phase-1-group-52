package com.example.main.enums.regex;

public enum SecurityQuestion {
    PetName("1. What is your pet's name?", 1),
    MotherMaiden("2. What is your mother's maiden name?", 2),
    BirthCity("3. Which city were you born in?", 3);

    private final String text;
    private final int id;

    SecurityQuestion(String text, int id) {
        this.text = text;
        this.id = id;
    }
    public String getQuestionText() { return text; }
    public int getId() { return id; }
}

