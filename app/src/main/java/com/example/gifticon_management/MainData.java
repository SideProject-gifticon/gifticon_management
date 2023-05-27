package com.example.gifticon_management;

import android.graphics.Bitmap;


import androidx.annotation.Nullable;
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
    private Bitmap originImage; // 원본 이미지
    private Bitmap usedImage; // 사용 완료 이미지
    private String date_text;//날짜
    private int yy; //년도
    private int mm; //월
    private int dd; //날짜
    private boolean isUsed; // 사용 여부

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id=id;
    }

    public Bitmap getOriginImage() {
        return originImage;
    }

    public void setOriginImage(Bitmap image) {
        this.originImage = image;
    }

    public Bitmap getUsedImage() {
        return usedImage;
    }

    public void setUsedImage(Bitmap image) {
        this.usedImage = image;
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

    public boolean getIsUsed() { return isUsed; }

    public void setIsUsed(boolean isUsed) { this.isUsed = isUsed; }

}
