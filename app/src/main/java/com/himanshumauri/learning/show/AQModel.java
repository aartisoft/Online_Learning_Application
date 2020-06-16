package com.himanshumauri.learning.show;

public class AQModel {
    String answer,approved,question,uploader,verified;

    public AQModel() {
    }

    public AQModel(String answer, String approved, String question, String uploader, String verified) {
        this.answer = answer;
        this.approved = approved;
        this.question = question;
        this.uploader = uploader;
        this.verified = verified;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getApproved() {
        return approved;
    }

    public void setApproved(String approved) {
        this.approved = approved;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }
}
