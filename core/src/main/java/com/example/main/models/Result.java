package com.example.main.models;

public record Result(Boolean isSuccessful, String Message) {
    public static Result success(String message) {
        return new Result(true, message);
    }

    public static Result failure(String message) {
        return new Result(false, message);
    }

    @Override
    public String toString() {
        return this.Message;
    }
}
