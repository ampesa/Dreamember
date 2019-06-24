package com.apps.apene.bioclock_1.model;


public class Tip {

    // Atributos
    private String title, content, preview, image_url, uid;


    // Constructores
    public Tip (){

    }

    public Tip (String title, String preview, String uid) {
        this.title = title;
        this.preview = preview;
        this.uid = uid;
    }

    public Tip (String title, String content, String uid, String image_url){
        this.title = title;
        this.content = content;
        this.image_url = image_url;
        this.uid = uid;
    }

    public Tip (String title, String preview, String uid, String content, String image_url){
        this.title = title;
        this.preview = preview;
        this.uid = uid;
        this.content = content;
        this.image_url = image_url;
    }


    // Getters y Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    // Método toString()

    @Override
    public String toString() {
        return "Tip{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", preview='" + preview + '\'' +
                ", image_url='" + image_url + '\'' +
                ", uid='" + uid + '\'' +
                '}';
    }
}
