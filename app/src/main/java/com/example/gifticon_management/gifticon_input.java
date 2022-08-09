package com.example.gifticon_management;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class gifticon_input extends AppCompatActivity {

    ImageView imageView_gifticon_input;
    EditText name_text_input;
    Button date_input_button;
    TextView date_input_text;
    Button gifticon_input_button;
    Button gifticon_cancellation_button;

    List<MainData> dataList = new ArrayList<>();
    RoomDB database;

    String date;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gifticon_input);


        database = RoomDB.getInstance(this);
        dataList = database.mainDao().getAll();

        imageView_gifticon_input = (ImageView) findViewById(R.id.imageView_gifticon_input);
        name_text_input= (EditText) findViewById(R.id.name_text_input);
        date_input_button = (Button) findViewById(R.id.date_input_button);
        date_input_text = (TextView) findViewById(R.id.date_input_text);
        gifticon_input_button = (Button) findViewById(R.id.gifticon_input_button);
        gifticon_cancellation_button = (Button) findViewById(R.id.gifticon_cancellation_button);

        Calendar cal = Calendar.getInstance();
        date_input_text.setText(cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DATE));



        //기프티콘 만료 날짜 입력 버튼
        date_input_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnClick_DatePick(view);

                //저장까지한다면 ->1
            }
        });

        //확인 버튼 눌렀을경우
       gifticon_input_button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               //입력받은거
               MainData temp = new MainData();
               temp.setText(name_text_input.toString());
               //1. 여기서 날짜 저장변수 따로 다시 입력 저장?
               temp.setDate_text(date);
               //갤러리에서 이미지 선택하기 변경 필요
               Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sample2);
               temp.setImage(bitmap);
               database.mainDao().insert(temp);

               finish();
           }
       });


        //취소 버튼을 눌렀을경우
        gifticon_cancellation_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        dataList.addAll(database.mainDao().getAll());

    }

    DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int yy, int mm, int dd) {
                    // Date Picker에서 선택한 날짜를 TextView에 설정
                    TextView date_input_text = findViewById(R.id.date_input_text);
                    date_input_text.setText(String.format("%d-%d-%d", yy,mm+1,dd));

                    date = String.format("%d-%d-%d", yy,mm+1,dd);
                }};


    public void mOnClick_DatePick(View v){
        Calendar cal = Calendar.getInstance();
        new DatePickerDialog(this, mDateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE)).show();
    }

}