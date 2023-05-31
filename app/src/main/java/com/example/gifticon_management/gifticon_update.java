package com.example.gifticon_management;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class gifticon_update extends AppCompatActivity {

    ImageView imageView_gifticon_update;
    EditText name_text_update;
    Button date_update_button;
    TextView date_update_text;
    Button gifticon_update_button;
    Button gifticon_cancellation_button;

    //RecyclerviewAdapter adapter;

    Intent intentUpdate;

    List<MainData> dataList = new ArrayList<>();
    RoomDB database;

    private MainDataViewModel mainDataViewModel;


    //intent로 가져온 변수
    int id;
    int year;
    int month;
    int day;

    String name;
    String date;
    Bitmap image;




    static final int REQUEST_CODE = 1;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gifticon_update);


        database = RoomDB.getInstance(this);
        //dataList = database.mainDao().getAll();
        mainDataViewModel =  new ViewModelProvider(this).get(MainDataViewModel.class);

        imageView_gifticon_update = (ImageView) findViewById(R.id.imageView_gifticon_update);
        name_text_update= (EditText) findViewById(R.id.name_text_update);
        date_update_button = (Button) findViewById(R.id.date_update_button);
        date_update_text = (TextView) findViewById(R.id.date_update_text);
        gifticon_update_button = (Button) findViewById(R.id.gifticon_update_button);
        gifticon_cancellation_button = (Button) findViewById(R.id.gifticon_cancellation_button);



        //이전 액티비에서 수정버튼 클릭시 데이터 받아오기
        //intent값 가져오기

        intentUpdate = getIntent();

        //받아온 데이터들 각각에 맞게 세팅해주기
        id = intentUpdate.getIntExtra("id",1);
        name = intentUpdate.getStringExtra("name");
        date = intentUpdate.getStringExtra("date");
        //image = intent.getParcelableExtra("image_gif");
        byte[] arr = intentUpdate.getByteArrayExtra("image_gif");
        image = BitmapFactory.decodeByteArray(arr, 0, arr.length);


        year = intentUpdate.getIntExtra("year",2022);
        month = intentUpdate.getIntExtra("month",5);
        day = intentUpdate.getIntExtra("day", 25);

        //데이터 세팅
        imageView_gifticon_update.setImageBitmap(image);
        name_text_update.setText(name);
        date_update_text.setText("만료 날짜 : "+date);


        //나머지 각 버튼 기능들 활성화
        //갤러리 버튼
        imageView_gifticon_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //갤러리 불러오기
                onClickImageView(view);

            }
        });

        //기프티콘 만료 날짜 업데이트
        date_update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnClick_DatePick(view);

            }
        });

        //확인버튼 클릭시 현재 입력한 데이터값으로 roomdb 데이터 바꿔주기 + 종료하기
        gifticon_update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainData data = new MainData();
                data.setId(id);
                data.setText(name_text_update.getText().toString());
                //날짜
                data.setDate_text(date);
                data.setYy(year);
                data.setMm(month);
                data.setDd(day);
                //갤러리
                if(uri!=null){
                    try {
                        InputStream in = getContentResolver().openInputStream(uri);
                        Bitmap bitmap = BitmapFactory.decodeStream(in);
                        data.setOriginImage(bitmap);
                        data.setUsedImage(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    data.setOriginImage(image);
                    data.setUsedImage(image);
                }

                //설정된 데이터들을 저장하는데 이떄 해당하는 id 번호에 따라 바꿔주기
                //database.mainDao().update(data);

                //dataList.addAll(database.mainDao().getAll());
                //dataList.set(id,database.mainDao().getAll().get(id));
                //dataList.clear();
                //dataList.addAll(database.mainDao().getAll());
                mainDataViewModel.update(data);
                setResult(9001); // 확인 버튼 누르면 resultcode 9001을 메인 액티비티로 보냄

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                },400);
            }
        });
        //취소 클릭시 현재 액티비티 종료하기
        gifticon_cancellation_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //취소
                finish();
            }
        });


    }




    DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int yy, int mm, int dd) {
                    // Date Picker에서 선택한 날짜를 TextView에 설정
                    TextView date_update_text = findViewById(R.id.date_update_text);
                    date_update_text.setText(String.format("%d-%d-%d", yy,mm+1,dd));

                    date = String.format("%d-%d-%d", yy,mm+1,dd);
                    //날짜 변수 저장
                    year = yy;
                    month = mm;
                    day = dd;

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
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
            imageView_gifticon_update.setImageBitmap(bitmap);
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }


}