package com.example.gifticon_management;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerviewAdapter adapter;
    FloatingActionButton gifticon_add_button;

    List<MainData> dataList = new ArrayList<>();
    RoomDB database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = RoomDB.getInstance(this);
        dataList = database.mainDao().getAll();

        recyclerView = findViewById(R.id.recyclerView);
        gifticon_add_button = (FloatingActionButton) findViewById(R.id.gifticon_add_button);
        int numColumns = 3;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numColumns));

        adapter = new RecyclerviewAdapter(MainActivity.this, dataList);
        recyclerView.setAdapter(adapter);


        gifticon_add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, gifticon_input.class);
                startActivity(intent);
                adapter.notifyDataSetChanged();
            }
        });

        // 데이터 모두 삭제
        /*
        database.mainDao().reset(dataList);
        dataList.clear();
        dataList.addAll(database.mainDao().getAll());
        adapter.notifyDataSetChanged();

        // 데이터 임시 추가
        for(int i=0; i<3; i++){
            MainData temp = new MainData();
            temp.setText("스타벅스"+i);
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sample2);
            temp.setImage(bitmap);
            database.mainDao().insert(temp);

            dataList.clear();
            dataList.addAll(database.mainDao().getAll());
            adapter.notifyDataSetChanged();
        }

        // 데이터 임시 추가
        for(int i=0; i<3; i++){
            MainData temp = new MainData();
            temp.setText("스타벅스"+i+3);
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sample);
            temp.setImage(bitmap);
            database.mainDao().insert(temp);

            dataList.clear();
            dataList.addAll(database.mainDao().getAll());
            adapter.notifyDataSetChanged();
        }
    */
        dataList.clear();
        dataList.addAll(database.mainDao().getAll());
        adapter.notifyDataSetChanged();

    }
}