package com.example.log_in;

import android.content.Context;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

public class Film {
    private String name , path_img;

    Film(String info){
        String [] value = info.split("!");
        name = value[1];
        path_img ="https://image.tmdb.org/t/p/w500"+value[0];
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name=name;
    }
    public String getPath_img(){
        return path_img;
    }
    public void setPath_img(String path_img){
        this.path_img=path_img;
    }
}
