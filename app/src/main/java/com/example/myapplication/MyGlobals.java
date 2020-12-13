package com.example.myapplication;

import java.util.HashMap;
import java.util.Map;

import com.example.myapplication.model.userDTO;

public class MyGlobals {
    private String myNickname;
    private String myEmail;
    private String myImageUrl;
    Map<String, String> myClasses = new HashMap<>();

    public void setMyInfo(userDTO user)
    {
        this.myNickname = user.getNickname();
        this.myEmail = user.getEmail();
        this.myImageUrl = user.getImageUrl();
        this.myClasses = user.getClasses();
    }

    public String getMyNickname()
    {
        return myNickname;
    }
    public void setMyNickname(String nickname)
    {
        this.myNickname = nickname;
    }
    public String getMyEmail()
    {
        return myEmail;
    }
    public void setMyEmail(String email)
    {
        this.myEmail = email;
    }
    public String getMyImageUrl()
    {
        return myImageUrl;
    }
    public void setMyImageUrl(String imageUrl)
    {
        this.myImageUrl = imageUrl;
    }
    public Map<String, String> getMyClasses()
    {
        return myClasses;
    }
    public void setMyClasses(HashMap<String, String> classes)
    {
        this.myClasses = classes;
    }
    private static MyGlobals instance = null;

    public static synchronized MyGlobals getInstance(){
        if(null == instance){
            instance = new MyGlobals();
        }
        return instance;
    }
}