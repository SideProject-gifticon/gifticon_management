package com.example.gifticon_management;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.room.TypeConverter;

import java.io.ByteArrayOutputStream;

public class Converters {

    // 비트맵 -> ByteArray
    @TypeConverter
    public byte[] toByteArray(Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        return outputStream.toByteArray();
    }

    // ByteArray -> 비트맵
    @TypeConverter
    public Bitmap toBitmap(byte[] byteArray){
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0 ,byteArray.length);
        return bitmap;
    }
}
