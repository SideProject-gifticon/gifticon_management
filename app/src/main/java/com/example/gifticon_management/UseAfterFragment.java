package com.example.gifticon_management;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


public class UseAfterFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    private RecyclerviewAdapter adapter;
    private RecyclerView recyclerView;

    private MainDataViewModel mainDataViewModel;

    public UseAfterFragment() {

    }

    public static UseAfterFragment newInstance(String param1, String param2) {
        UseAfterFragment fragment = new UseAfterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_use_after, container, false);

        recyclerView = view.findViewById(R.id.recyclerView_useAfter);
        adapter = new RecyclerviewAdapter();

        //database = RoomDB.getInstance(view.getContext());
        int numColumns = 3;
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), numColumns));

        recyclerView.setAdapter(adapter);

        mainDataViewModel = new ViewModelProvider(this).get(MainDataViewModel.class);
        mainDataViewModel.getAllMainData().observe(getViewLifecycleOwner(), new Observer<List<MainData>>() {
            @Override
            public void onChanged(List<MainData> dataList) {
                adapter.setMainData(dataList);
                adapter.notifyDataSetChanged();
            }
        });

        return view;
    }
}