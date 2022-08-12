package com.example.gifticon_management;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Delayed;

public class gifticon_input extends AppCompatActivity {

    ImageView imageView_gifticon_input;
    EditText name_text_input;
    Button date_input_button;
    TextView date_input_text;
    Button gifticon_input_button;
    Button gifticon_cancellation_button;

    //RecyclerviewAdapter adapter;

    List<MainData> dataList = new ArrayList<>();
    RoomDB database;

    String date;



    static final int REQUEST_CODE = 1;
    Uri uri;

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

        //adapter = new RecyclerviewAdapter(this, dataList);


        //갤러리 나오게 선택
        imageView_gifticon_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickImageView(view);

            }
        });

        //기프티콘 만료 날짜 입력 버튼
        date_input_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnClick_DatePick(view);

            }
        });

        //확인 버튼 눌렀을경우
       gifticon_input_button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               //입력받은거
               MainData temp = new MainData();
               temp.setText(name_text_input.getText().toString());
               //1. 여기서 날짜 저장변수 따로 다시 입력 저장?
               temp.setDate_text(date);
               //갤러리 이미지 절대 경로 저장
               try {
                   InputStream in = getContentResolver().openInputStream(uri);
                   Bitmap bitmap = BitmapFactory.decodeStream(in);
                   temp.setImage(bitmap);
               } catch (FileNotFoundException e) {
                   e.printStackTrace();
               }
               database.mainDao().insert(temp);
               dataList.addAll(database.mainDao().getAll());
               //adapter.notifyDataSetChanged();
               setResult(9001); // 확인 버튼 누르면 resultcode 9001을 메인 액티비티로 보냄

               new Handler().postDelayed(new Runnable() {
                   @Override
                   public void run() {
                       finish();
                   }
               },400);

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


    //갤러리 소환
    public void onClickImageView(View v){
        Intent intent_img = new Intent(Intent.ACTION_PICK);
        intent_img.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent_img,REQUEST_CODE);
    }


    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == REQUEST_CODE) {
            uri = data.getData();
            setImage(uri);
        }
    }


    //이미지 뷰 보여주기
    private void setImage(Uri uri) {
        try{
            InputStream in = getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(in);
            imageView_gifticon_input.setImageBitmap(bitmap);
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

}