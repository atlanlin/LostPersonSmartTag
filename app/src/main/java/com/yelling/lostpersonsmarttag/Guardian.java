package com.yelling.lostpersonsmarttag;

import java.util.HashMap;

/**
 * Created by Yelling on 30/10/15.
 */
public class Guardian {
    protected int guardian_id;
    protected String login_id, login_pw, contact_number, name, description, address,
        image_link, gcm_code;

    public Guardian(int guardian_id, String login_id, String login_pw, String contact_number,
                    String name, String description, String address, String image_link, String gcm_code){
        this(login_id,login_pw,contact_number,name,description,address,image_link,gcm_code);
        this.guardian_id= guardian_id;

    }

    public Guardian(String login_id, String login_pw, String contact_number,
        String name, String description, String address, String image_link, String gcm_code){
        this.login_id = login_id;
        this.login_pw = login_pw;
        this.contact_number = contact_number;
        this.name = name;
        this.description = description;
        this.address = address;
        this.image_link = image_link;
        this.gcm_code = gcm_code;

    }

    protected void setGuardianId(int guardian_id){
        this.guardian_id = guardian_id;
    }

    protected Guardian copyOf(){
        Guardian newGuardian = new Guardian(guardian_id,login_id.toString(),login_pw.toString(),
                contact_number.toString(),name.toString(), description.toString(),
                address.toString(),image_link.toString(),gcm_code.toString());
        return newGuardian;
    }

    protected boolean validateInfo(){
        if(login_id.isEmpty())
            return false;
        if(login_pw.isEmpty())
            return false;
        if(contact_number.isEmpty())
            return false;
        if(name.isEmpty())
            return false;
        if(description.isEmpty())
            return false;
        if(address.isEmpty())
            return false;
        return true;
    }

    public String toString(){
        String str = "Guardian[guardian id:" + guardian_id + ", login id:" + login_id + ", name:" +
                name + ", contact no:" + contact_number + ", address:" + address +
                ", description:" + description;
        return str;

    }
    public HashMap<String, String> toHashmap(){
        HashMap<String, String>hashMap = new HashMap<String, String>();
        hashMap.put("__type", "Guardian:#");
        hashMap.put("login_id", login_id);
        hashMap.put("login_pw", login_pw);
        hashMap.put("contact_number", contact_number);
        hashMap.put("name", name);
        hashMap.put("description", description);
        hashMap.put("address", address);
        return hashMap;
    }

}
