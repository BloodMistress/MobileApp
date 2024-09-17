package com.example.kyrsach;

public class Note {
    private String date;
    private String calories;
    private String workoutType;
    private String workoutTime;

    // Конструктор без параметров нужен для Firebase
    public Note() {
    }

    // Конструктор с параметрами
    public Note(String date, String calories, String workoutType, String workoutTime) {
        this.date = date;
        this.calories = calories;
        this.workoutType = workoutType;
        this.workoutTime = workoutTime;
    }

    // Геттеры и сеттеры для всех полей
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getWorkoutType() {
        return workoutType;
    }

    public void setWorkoutType(String workoutType) {
        this.workoutType = workoutType;
    }

    public String getWorkoutTime() {
        return workoutTime;
    }

    public void setWorkoutTime(String workoutTime) {
        this.workoutTime = workoutTime;
    }
}

