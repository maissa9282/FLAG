package com.example.flag;

public class Question {

    private int imageResId;
    private String[] options;
    private int correctAnswerIndex;

    public Question(int imageResId, String[] options, int correctAnswerIndex) {
        this.imageResId = imageResId;
        // Ensure we always have 3 options
        if (options == null || options.length != 3) {
            throw new IllegalArgumentException("Question must have exactly 3 options.");
        }
        this.options = options;

        if (correctAnswerIndex < 0 || correctAnswerIndex >= 3) {
            throw new IllegalArgumentException("Correct answer index must be 0, 1, or 2.");
        }
        this.correctAnswerIndex = correctAnswerIndex;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String[] getOptions() {
        return options;
    }

    public String getOption1() {
        return options[0];
    }

    public String getOption2() {
        return options[1];
    }

    public String getOption3() {
        return options[2];
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }
}
