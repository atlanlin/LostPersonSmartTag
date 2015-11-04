package com.yelling.lostpersonsmarttag;

/**
 * Created by Yelling on 30/10/15.
 */
public class Ward {
    protected int ward_id;
    protected String name, ward_tag, description, img_link;

    public Ward(int ward_id, String name, String ward_tag, String description, String img_link){
        this(name, ward_tag, description, img_link);
        this.ward_id = ward_id;
    }

    public Ward(String name, String ward_tag, String description, String img_link){
        this.name = name;
        this.description = description;
        this.img_link = img_link;
        this.ward_tag = ward_tag;

    }

    protected void setWardId(int ward_id){
        this.ward_id = ward_id;
    }

    protected Ward copyOf(){
        Ward ward = new Ward(this.ward_id, this.ward_tag, this.name.toString(), this.description.toString(),
                this.img_link.toString());
        return ward;
    }

    protected boolean validateInfo(){
        if(name.isEmpty() || description.isEmpty())
            return false;
        return true;
    }

    public String toString(){
        String str = "Ward [ward_id=" + this.ward_id + ", name=" + ", ward tag=" + this.ward_tag
                + this.name + ", description="
                + this.description + ", Image Link=" + this.img_link;
        return str;

    }
}
