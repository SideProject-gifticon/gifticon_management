package com.example.gifticon_management;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

public class gifticon_touch_activity extends AppCompatActivity {

    ImageView imageView_gifticon_touch;
    TextView name_text_touch;
    TextView date_text_touch;
    TextView date_d_day;
    Button gifticon_input_button_touch; //사용
    Button gifticon_cancellation_button_touch; //취소

    Button gifticon_update_button_touch; // 수정버튼

    Intent intent;

    String name;
    String date;
    Bitmap image;

    //intent로 가져온 변수
    int id;
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
        gifticon_update_button_touch = (Button) findViewById(R.id.gifticon_update_button_touch);

        //intent값 가져오기

        intent = getIntent();

        id = intent.getIntExtra("id",1);
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

                // 이미지 흑백으로 변환
                ColorMatrix matrix = new ColorMatrix();
                matrix.setSaturation(0);
                ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                imageView_gifticon_touch.setColorFilter(filter);

                //Bitmap img2;
                Bitmap overlayBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.used);
                //overlayImg(image, overlayBitmap);
                imageView_gifticon_touch.setImageBitmap(overlayImg(image, overlayBitmap));

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

        //기프티콘 수정 버튼 클릭 이벤트
        gifticon_update_button_touch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //클릭시 수정하는 화면으로 이동해야함.
                Intent intent = new Intent(gifticon_touch_activity.this, gifticon_update.class);
                //이동시 아이디, 이미지, 날짜, 이름 데이터 넘겨줘야합니다.
                //수정시 필요한 아이디 넘기기
                intent.putExtra("id",id);
                intent.putExtra("name",name);
                intent.putExtra("date",date);
                //날짜 입력
                intent.putExtra("year", year);
                intent.putExtra("month",month);
                intent.putExtra("day", day);
                //이미지 입력
                byte[] arr = intent.getByteArrayExtra("image_gif");
                intent.putExtra("image_gif",arr);

                startActivity(intent);


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

    private Bitmap overlayImg(Bitmap originalBitmap, Bitmap overlayBitmap)
    {
        int width = Math.min(originalBitmap.getWidth(), overlayBitmap.getWidth());
        int height = Math.min(originalBitmap.getHeight(), overlayBitmap.getHeight());

        Bitmap resizedOriginalBitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, false);
        Bitmap resizedOverlayBitmap = Bitmap.createScaledBitmap(overlayBitmap, width, height, false);

        Bitmap combinedBitmap = Bitmap.createBitmap(width, height, resizedOriginalBitmap.getConfig());
        Canvas canvas = new Canvas(combinedBitmap);
        canvas.drawBitmap(resizedOriginalBitmap, 0, 0, null);
        canvas.drawBitmap(resizedOverlayBitmap, 0, 0, null);
        
        return combinedBitmap;
    }
}