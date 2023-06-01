package com.example.gifticon_management;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.github.chrisbanes.photoview.PhotoView;

public class ImgTouchActivity extends AppCompatActivity {
    PhotoView photoView;
    RoomDB database;
    Intent intent;
    Bitmap image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_touch);
        intent = getIntent();
        database = RoomDB.getInstance(this);

        int id = intent.getIntExtra("id", 0);
        image = database.mainDao().getDataById(id).getOriginImage();

        photoView = findViewById(R.id.gifticonPhotoView);
        photoView.setImageBitmap(image);
    }
}