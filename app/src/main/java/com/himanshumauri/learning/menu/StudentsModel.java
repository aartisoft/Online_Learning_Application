package com.himanshumauri.learning.menu;

public class StudentsModel {
    String student;
    int score;

    public StudentsModel() {
    }

    public StudentsModel(String student, int score) {
        this.student = student;
        this.score = score;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
