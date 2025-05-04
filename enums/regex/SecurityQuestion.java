package enums.regex;

public enum SecurityQuestion {
    PetName("What is your pet's name?"),
    MotherMaiden("What is your mother's maiden name?"),
    BirthCity("Which city were you born in?");

    private final String text;

    SecurityQuestion(String text) { this.text = text; }
    public String getQuestionText() { return text; }
}

