package com.example.gifticon_management;

import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.TextView;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "table_name")
public class MainData implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name="text")
    private String text;//이름
    private Bitmap image;//이미지
    private String date_text;//날짜
    private int yy; //년도
    private int mm; //월
    private int dd; //날짜

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id=id;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getText(){
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate_text() {
        return date_text;
    }

    public void setDate_text(String date_text) {
        this.date_text = date_text;
    }

    public int getDd() {
        return dd;
    }

    public void setDd(int dd) {
        this.dd = dd;
    }

    public int getMm() {
        return mm;
    }

    public void setMm(int mm) {
        this.mm = mm;
    }

    public int getYy() {
        return yy;
    }

    public void setYy(int yy) {
        this.yy = yy;
    }
}
