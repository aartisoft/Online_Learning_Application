package com.himanshumauri.learning;

public class TopicModel {
    String Title, name,Uid,approved;

    public TopicModel() {
    }

    public TopicModel(String title, String name, String uid, String approved) {
        Title = title;
        this.name = name;
        Uid = uid;
        this.approved = approved;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getApproved() {
        return approved;
    }

    public void setApproved(String approved) {
        this.approved = approved;
    }
}
