package com.example.gifticon_management;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class gifticon_touch_activity extends AppCompatActivity {

    ImageView imageView_gifticon_touch;
    TextView name_text_touch;
    TextView date_text_touch;
    TextView date_d_day;
    Button gifticon_input_button_touch; //사용
    Button gifticon_cancellation_button_touch; //취소

    Intent intent;

    String name;
    String date;
    Bitmap image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gifticon_touch);

        imageView_gifticon_touch = (ImageView) findViewById(R.id.imageView_gifticon_touch);
        name_text_touch = (TextView) findViewById(R.id.name_text_touch);
        date_text_touch = (TextView) findViewById(R.id.date_text_touch);
        date_d_day = (TextView) findViewById(R.id.date_d_day);
        gifticon_input_button_touch = (Button) findViewById(R.id.gifticon_input_button_touch);
        gifticon_cancellation_button_touch = (Button) findViewById(R.id.gifticon_cancellation_button_touch);

        //intent값 가져오기

        intent = getIntent();

        name = intent.getStringExtra("name");
        date = intent.getStringExtra("date");
        //image = intent.getParcelableExtra("image_gif");
        byte[] arr = getIntent().getByteArrayExtra("image_gif");
        image = BitmapFactory.decodeByteArray(arr, 0, arr.length);




        //데이터 세팅
        imageView_gifticon_touch.setImageBitmap(image);
        name_text_touch.setText(name);
        date_text_touch.setText(date);


        //버튼 셋팅
        //사용 버튼
        gifticon_input_button_touch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //사용완료하면 데이터 저장 따로 해야할듯;
            }
        });
        //취소하면 다시 메인으로 돌아가기
        gifticon_cancellation_button_touch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //취소
                finish();
            }
        });
    }
}