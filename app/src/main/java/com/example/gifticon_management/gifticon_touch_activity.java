package com.example.gifticon_management;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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


    Button gifticon_update_button_touch; // 수정버튼

    Intent intent;

    String name;
    String date;
    Bitmap originImage;
    Bitmap usedImage;

    //intent로 가져온 변수
    int id;
    int year;
    int month;
    int day;
    boolean isUsed;

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

    private MainDataViewModel mainDataViewModel;

    private int resultNumber=0;

    RoomDB database;

    Toolbar toolbar;

    ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gifticon_touch);

        imageView_gifticon_touch = (ImageView) findViewById(R.id.imageView_gifticon_touch);
        name_text_touch = (TextView) findViewById(R.id.name_text_touch);
        date_text_touch = (TextView) findViewById(R.id.date_text_touch);
        date_d_day = (TextView) findViewById(R.id.date_d_day);
        gifticon_input_button_touch = (Button) findViewById(R.id.gifticon_input_button_touch);
        gifticon_update_button_touch = (Button) findViewById(R.id.gifticon_update_button_touch);

        toolbar = findViewById(R.id.toolbar);

        mainDataViewModel =  new ViewModelProvider(this).get(MainDataViewModel.class);



        setSupportActionBar(toolbar);
        //뒤로가기 활성화
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //intent값 가져오기
        intent = getIntent();
        id = intent.getIntExtra("id",1);
        name = intent.getStringExtra("name");
        date = intent.getStringExtra("date");
        //image = intent.getParcelableExtra("image_gif");
        //byte[] arr1 = intent.getByteArrayExtra("originImage");
        //byte[] arr2 = intent.getByteArrayExtra("usedImage");
        //originImage = BitmapFactory.decodeByteArray(arr1, 0, arr1.length);
        //usedImage = BitmapFactory.decodeByteArray(arr2, 0, arr2.length);
        isUsed = intent.getBooleanExtra("isUsed", false);

        year = intent.getIntExtra("year",2022);
        month = intent.getIntExtra("month",5);
        day = intent.getIntExtra("day", 25);

        //데이터 세팅

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

        database = RoomDB.getInstance(this);
        usedImage = database.mainDao().getDataById(id).getUsedImage();
        originImage = database.mainDao().getDataById(id).getOriginImage();
        if(isUsed){
            gifticon_input_button_touch.setText("사용 취소");
            imageView_gifticon_touch.setImageBitmap(usedImage);
        }
        else{
            gifticon_input_button_touch.setText("사용");
            imageView_gifticon_touch.setImageBitmap(originImage);
        }


        //버튼 셋팅
        //사용 버튼
        gifticon_input_button_touch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isUsed = !isUsed;

                MainData data = new MainData();
                //MainData curData = database.mainDao().getDataById(id);
                if(isUsed){ // 사용
                    gifticon_input_button_touch.setText("사용 취소");
                    Bitmap overlayBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.used);
                    Bitmap usedImg = overlayImg(originImage, overlayBitmap); // 사용 완료 이미지
                    imageView_gifticon_touch.setImageBitmap(usedImg);
                    data.setUsedImage(usedImg);
                }
                else{ // 사용 취소
                    gifticon_input_button_touch.setText("사용");
                    imageView_gifticon_touch.setImageBitmap(originImage);
                    data.setUsedImage(originImage);
                }
                data.setId(id);
                data.setText(name);
                data.setDate_text(date);
                data.setYy(year);
                data.setMm(month);
                data.setDd(day);
                data.setIsUsed(isUsed);
                data.setOriginImage(originImage);
                //업데이트
                mainDataViewModel.update(data);
                //database.mainDao().update(data);


                setResult(9001); // 사용 버튼 누르면 resultcode 9001을 메인 액티비티로 보냄
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
               // byte[] arr = intent.getByteArrayExtra("image_gif");
                //intent.putExtra("image_gif",arr);

                //startActivity(intent);
                activityResultLauncher.launch(intent); // gifticon_touch 액티비티 시작


            }
        });

        activityResultLauncher = registerForActivityResult( // 기프티콘 추가 액티비티에서 확인 버튼 누르면 실행됨
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if(result.getResultCode()==9001){
                        // TODO
                        setResult(9001); 
                        finish();

                    }
                });
        imageView_gifticon_touch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(gifticon_touch_activity.this, ImgTouchActivity.class);
                //ByteArrayOutputStream stream = new ByteArrayOutputStream();
                //originImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                //byte[] arr = stream.toByteArray();
                intent.putExtra("id",id);

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(gifticon_touch_activity.this, view, "imgClickTrans");
                startActivity(intent, options.toBundle());
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



    private Bitmap overlayImg(Bitmap originalBitmap, Bitmap usedBitmap)
    {


        int imageViewWidth = imageView_gifticon_touch.getWidth();
        int imageViewHeight = imageView_gifticon_touch.getHeight();

        float scaleFactor = Math.min((float)imageViewWidth / usedBitmap.getWidth(), (float) imageViewHeight / usedBitmap.getHeight());
        int resizedUsedWidth = Math.round(usedBitmap.getWidth() * scaleFactor);
        int resizedUsedHeight = Math.round(usedBitmap.getHeight() * scaleFactor);

        int gifticonWidth = originalBitmap.getWidth();
        int gifticonHeight = originalBitmap.getHeight();

        int resizedGifticonWidth, resizedGifticonHeight;
        if (gifticonWidth > gifticonHeight) {
            // 기프티콘 이미지의 가로가 더 긴 경우
            resizedGifticonWidth = resizedUsedWidth;
            resizedGifticonHeight = (int) (((float) resizedUsedWidth / gifticonWidth) * gifticonHeight);
        } else {
            // 기프티콘 이미지의 세로가 더 긴 경우
            resizedGifticonHeight = resizedUsedHeight;
            resizedGifticonWidth = (int) (((float) resizedUsedHeight / gifticonHeight) * gifticonWidth);
        }

        Bitmap resizedOriginalBitmap = Bitmap.createScaledBitmap(originalBitmap, resizedGifticonWidth, resizedGifticonHeight, false);
        Bitmap resizeUsedBitmap = Bitmap.createScaledBitmap(usedBitmap, resizedUsedWidth, resizedUsedHeight, false);

        Bitmap combinedBitmap = Bitmap.createBitmap(resizedUsedWidth, resizedUsedHeight, resizedOriginalBitmap.getConfig());
        Canvas canvas = new Canvas(combinedBitmap);

        canvas.drawColor(Color.WHITE); // 배경을 흰색으로 설정

        int left = (resizedUsedWidth - resizedOriginalBitmap.getWidth()) / 2; // 가운데 정렬을 위해 left 좌표 계산
        int top = (resizedUsedHeight - resizedOriginalBitmap.getHeight()) / 2; // 가운데 정렬을 위해 top 좌표 계산

        canvas.drawBitmap(resizedOriginalBitmap, left, top, null);
        canvas.drawBitmap(resizeUsedBitmap, 0, 0, null);

        // 흑백으로 변경
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        Paint paint = new Paint();
        paint.setColorFilter(filter);
        canvas.drawBitmap(combinedBitmap, 0, 0, paint);

        return combinedBitmap;
    }

    //뒤로가기 토글 버튼 작동
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

}