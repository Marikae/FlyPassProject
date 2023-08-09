package com.example.lez5;

public class Service {
    private String name;
    private String imgSrc;
    private String description;
    public String getName(){
        return name;
    }
    public String getImgSrc(){
        return imgSrc;
    }
    public String getDescription(){
        return description;
    }
    public void setName(String newName){
        this.name = newName;
    }
    public void setImgSrc(String newImgSrc){
        this.imgSrc = newImgSrc;
    }
    public void setDescription(String newDescription){
        this.description = newDescription;
    }
}
