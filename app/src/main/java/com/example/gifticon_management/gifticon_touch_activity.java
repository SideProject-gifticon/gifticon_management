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

import java.util.Calendar;

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

    //intent로 가져온 변수
    int year;
    int month;
    int day;

    //오늘 날짜 변수
    int yy;
    int mm;
    int dd;

    //D-day 변수
    private  int dYear=1;
    private  int dMonth=1;
    private  int dDay=1;

    private  long d;
    private  long t;
    private  long r;

    private int resultNumber=0;

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
        byte[] arr = intent.getByteArrayExtra("image_gif");
        image = BitmapFactory.decodeByteArray(arr, 0, arr.length);


        year = intent.getIntExtra("year",2022);
        month = intent.getIntExtra("month",5);
        day = intent.getIntExtra("day", 25);

        //데이터 세팅
        imageView_gifticon_touch.setImageBitmap(image);
        name_text_touch.setText(name);
        date_text_touch.setText("만료 날짜 : "+date);

        //d-day 계산산

        Calendar calendar = Calendar.getInstance(); //현재 날짜 불러옴
        yy = calendar.get(Calendar.YEAR);
        mm = calendar.get(Calendar.MONTH);
        dd = calendar.get(Calendar.DAY_OF_MONTH);

        Calendar dCalendar = Calendar.getInstance();
        dCalendar.set(year,month,day);

        //오늘 날짜를 밀리타임으로 변경
        t = calendar.getTimeInMillis();
        d = dCalendar.getTimeInMillis();
        r = (d-t)/(24*60*60*1000);

        resultNumber = (int)r;
        updateDisplay();

       // date_d_day.


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

    private void updateDisplay(){

        if(resultNumber>=0){
            date_d_day.setText(String.format("D - %d",resultNumber));
        }else{
            int absR = Math.abs(resultNumber);
            date_d_day.setText(String.format("D + %d",absR));
        }

    }
}