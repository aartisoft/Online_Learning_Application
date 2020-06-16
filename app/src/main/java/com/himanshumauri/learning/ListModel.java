package com.himanshumauri.learning;

public class ListModel {
    String titleDesc,titleImage, titleName, titleTags;

    public ListModel() {
    }

    public ListModel(String titleDesc, String titleImage, String titleName, String titleTags) {
        this.titleDesc = titleDesc;
        this.titleImage = titleImage;
        this.titleName = titleName;
        this.titleTags = titleTags;
    }

    public String getTitleDesc() {
        return titleDesc;
    }

    public void setTitleDesc(String titleDesc) {
        this.titleDesc = titleDesc;
    }

    public String getTitleImage() {
        return titleImage;
    }

    public void setTitleImage(String titleImage) {
        this.titleImage = titleImage;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public String getTitleTags() {
        return titleTags;
    }

    public void setTitleTags(String titleTags) {
        this.titleTags = titleTags;
    }
}
