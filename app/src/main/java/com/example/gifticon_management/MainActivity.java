package com.example.gifticon_management;

import androidx.activity.result.ActivityResultLauncher;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;


import java.io.ByteArrayOutputStream;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerviewAdapter adapter;
    FloatingActionButton gifticon_add_button;
    //GificonDailog gificonDailog;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle barDrawerToggle;


    List<MainData> dataList = new ArrayList<>();
    RoomDB database;

    Toolbar toolbar;

    private MainDataViewModel mainDataViewModel;

    ActivityResultLauncher<Intent> activityResultLauncher; // 기프티콘 추가 액티비티 실행할 때 필요


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 네비게이션 뷰
        toolbar = (Toolbar) findViewById(R.id.toolbar_drawer);
        navigationView = findViewById(R.id.navigationView);
        //drawerLayout = findViewById(R.id.layout_drawer);
        // 누르면 Drawer 열리는 삼선 모양 버튼
        //barDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.app_name, R.string.app_name);
        setSupportActionBar(toolbar);

        //drawerLayout.addDrawerListener(barDrawerToggle);
        //barDrawerToggle.syncState();


        //database = RoomDB.getInstance(this);
        //dataList = database.mainDao().getAll();

        recyclerView = findViewById(R.id.recyclerView);
        gifticon_add_button = (FloatingActionButton) findViewById(R.id.gifticon_add_button);
        int numColumns = 3;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numColumns));

        adapter = new RecyclerviewAdapter();
        recyclerView.setAdapter(adapter);

        mainDataViewModel = new ViewModelProvider(this).get(MainDataViewModel.class);
        mainDataViewModel.getAllMainData().observe(this, new Observer<List<MainData>>() {
            @Override
            public void onChanged(List<MainData> mainData) {
                //변화감지
                dataList.clear();
                adapter.setMainData(mainData);
            }
        });
//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch(item.getItemId()){
//                    case R.id.menu_setting: // 설정 눌렀을 때
//                        Intent intent = new Intent(MainActivity.this, settingActivity.class);
//                        startActivity(intent); // 설정 창 실행
//                        break;
//                }
//                // Drawer 닫기
//                drawerLayout.closeDrawer(navigationView);
//                return false;
//            }
//        });



        // 기프티콘 터치

        adapter.setOnItemClickListener(new RecyclerviewAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(int position, MainData mainData) {
                //list가 아니라 클릭한 데이터에서 get데이터값을 전달해줘야한다.
                //여기에 intent 정보 전달
                Intent intent = new Intent(MainActivity.this, gifticon_touch_activity.class);
                intent.putExtra("id",mainData.getId());
                intent.putExtra("name",mainData.getText());
                intent.putExtra("date",mainData.getDate_text());
                intent.putExtra("isUsed",mainData.getIsUsed());
                //날짜입력
                intent.putExtra("year",mainData.getYy());
                intent.putExtra("month",mainData.getMm());
                intent.putExtra("day",mainData.getDd());
                //이미지 입력
                //Bitmap usedImage = mainData.getUsedImage();
                //Bitmap originImage = mainData.getOriginImage();
                //ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
                //ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
                //originImage.compress(Bitmap.CompressFormat.JPEG, 100, stream1);
                //byte[] byteArray1 = stream1.toByteArray();
                //usedImage.compress(Bitmap.CompressFormat.JPEG, 100, stream2);
                //byte[] byteArray2 = stream2.toByteArray();
                //intent.putExtra("originImage",byteArray1);
                //intent.putExtra("usedImage",byteArray2);

                //startActivity(intent);
                activityResultLauncher.launch(intent); // gifticon_touch 액티비티 시작
            }
        });

        recyclerView.setAdapter(adapter);

        //기프티콘 롱 터치 이벤트
        adapter.setOnItemLongClickListener(new RecyclerviewAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View v, int pos) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("삭제/수정");
                builder.setMessage("삭제 하실겁니까?");

                //다이얼로그 이벤트 처리
                //삭제 확인버튼
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //확인 누르면 삭제
                        //database.mainDao().delete(dataList.remove(pos));
                        mainDataViewModel.delete(adapter.getMainDataAt(pos));
                        adapter.notifyItemRemoved(pos);
                    }
                });


                //취소버튼
                builder.setNegativeButton("취소",null);

                //다이얼로그 표시
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        recyclerView.setAdapter(adapter);

        gifticon_add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, gifticon_input.class);
                activityResultLauncher.launch(intent); // 기프티콘 추가 액티비티 시작
                //startActivity(intent);
            }
        });

        activityResultLauncher = registerForActivityResult( // 기프티콘 추가 액티비티에서 확인 버튼 누르면 실행됨
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if(result.getResultCode()==9001){
                        //dataList.clear();
                        //adapter.setMainData(dataList);

                        adapter.notifyDataSetChanged(); // 새로 추가한 데이터 갱신
                    }
                });




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.drawer_menu, menu);
        return true;
    }

    @Override // 네비게이션 뷰의 메뉴를 터치했을 때 실행
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_setting:
                Intent intent = new Intent(MainActivity.this, settingActivity.class);
                startActivity(intent); // 설정 창 실행
                break;
        }
        //barDrawerToggle.onOptionsItemSelected(item);
        return super.onOptionsItemSelected(item);

    }
}