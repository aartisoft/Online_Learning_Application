package com.himanshumauri.learning.show;

public class NotesModel {
    String body,title,uploader,verified,video;

    public NotesModel() {
    }

    public NotesModel(String body, String title, String uploader, String verified, String video) {
        this.body = body;
        this.title = title;
        this.uploader = uploader;
        this.verified = verified;
        this.video = video;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }
}
