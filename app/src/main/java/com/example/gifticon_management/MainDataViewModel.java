package com.example.gifticon_management;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

/*
ui매칭 시킬 데이터 저장 및 처리하는 viewModel
Observe를 통해 데이터 셋 변화를 감지해서 ui에 적용하는 역할 클래스
 */
public class MainDataViewModel extends AndroidViewModel {

    private MainDataRepository repository;
    private LiveData<List<MainData>> allMainData;

    public MainDataViewModel(@NonNull Application application) {
        super(application);
        repository = new MainDataRepository(application);
        allMainData = repository.getAllMainData();
    }

    public void insert(MainData mainData){
        repository.insert(mainData);
    }

    public void update(MainData mainData){
        repository.update(mainData);
    }

    public void delete(MainData mainData){
        repository.delete(mainData);
    }

    public void getDataById(int id){
        repository.getDataById(id);
    }

    public LiveData<List<MainData>> getAllMainData(){
        return allMainData;
    }
}
