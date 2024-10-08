package com.example.spring_boot_mode.entity.mode;

public class Animation {

    private String id;
    private String name;
    private String address;
    private String notes;
    private String alias;
    private String hasend;
    private String pictureURL;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getHasend() {
        return hasend;
    }

    public void setHasend(String hasend) {
        this.hasend = hasend;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }

    @Override
    public String toString() {
        return "Animation{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", notes='" + notes + '\'' +
                ", alias='" + alias + '\'' +
                ", hasend='" + hasend + '\'' +
                ", pictureURL='" + pictureURL + '\'' +
                '}';
    }
}
