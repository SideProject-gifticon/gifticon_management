package com.example.gifticon_management;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class UseBeforeFragment extends Fragment {


    private RecyclerView recyclerView;
    private RecyclerviewAdapter recyclerviewAdapter;
    private FloatingActionButton gifticon_add_button_use;


    List<MainData> dataList = new ArrayList<>();
    private RoomDB database;
    private MainDataViewModel mainDataViewModel;

    private ActivityResultLauncher<Intent> activityResultLauncher;

    public UseBeforeFragment() {

    }


    public static UseBeforeFragment newInstance() {
        UseBeforeFragment fragment = new UseBeforeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_use_before, container, false);

       recyclerView = view.findViewById(R.id.recyclerView_useBefore);
       gifticon_add_button_use = view.findViewById(R.id.gifticon_add_button_use);
       database = RoomDB.getInstance(view.getContext());
       int numColumns = 3;
       recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), numColumns));


       recyclerviewAdapter = new RecyclerviewAdapter();



        mainDataViewModel = new ViewModelProvider(this).get(MainDataViewModel.class);
        mainDataViewModel.getAllMainData().observe(getViewLifecycleOwner(), new Observer<List<MainData>>() {
            @Override
            public void onChanged(List<MainData> mainData) {
                //변화감지
                dataList.clear();
                recyclerviewAdapter.setMainData(mainData);
            }
        });

        recyclerView.setAdapter(recyclerviewAdapter);

        gifticon_add_button_use.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), gifticon_input.class);
                activityResultLauncher.launch(intent); // 기프티콘 추가 액티비티 시작
                //startActivity(intent);
            }
        });

        activityResultLauncher = registerForActivityResult( // 기프티콘 추가 액티비티에서 확인 버튼 누르면 실행됨
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if(result.getResultCode()==9002){
                        recyclerviewAdapter.notifyDataSetChanged(); // 새로 추가한 데이터 갱신
                    }
                });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainDataViewModel = new ViewModelProvider(this).get(MainDataViewModel.class);
        mainDataViewModel.getAllMainData().observe(getViewLifecycleOwner(),new Observer<List<MainData>>() {
            @Override
            public void onChanged(List<MainData> mainData) {
                recyclerviewAdapter.setMainData(mainData);
            }
        });
    }

}