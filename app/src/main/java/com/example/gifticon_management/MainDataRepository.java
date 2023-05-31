package com.example.gifticon_management;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;


import java.util.List;
import java.util.concurrent.ExecutionException;

/*
로컬 DB ROOM과 ViewModel간의 브릿지 역할
DAO 호출 AsyncTask
 */
public class MainDataRepository {

    private MainDao mainDao;
    private LiveData<List<MainData>> allMainData;

    public MainDataRepository(Application application){
        RoomDB roomDB = RoomDB.getInstance(application);
        mainDao = roomDB.mainDao();
        allMainData = mainDao.getAll();
    }

    public void insert(MainData mainData){
        new InsertMainDataAsyncTask(mainDao).execute(mainData);
    }

    public void update(MainData mainData){
        new UpdatetMainDataAsyncTask(mainDao).execute(mainData);
    }

    public void delete(MainData mainData){
        new DeleteMainDataAsyncTask(mainDao).execute(mainData);
    }

    public void getDataById(int id){
        new GetMainDataByIdAsyncTask(mainDao).execute(id);
    }
    

    public LiveData<List<MainData>> getAllMainData() {
        return allMainData;
    }

    private static class InsertMainDataAsyncTask extends AsyncTask<MainData, Void, Void>{
        private MainDao mainDao;

        private InsertMainDataAsyncTask(MainDao mainDao){
            this.mainDao = mainDao;
        }

        @Override
        protected Void doInBackground(MainData... mainDatas){
            mainDao.insert(mainDatas[0]);
            return null;
        }
    }

    private static class UpdatetMainDataAsyncTask extends AsyncTask<MainData, Void, Void>{
        private MainDao mainDao;

        private UpdatetMainDataAsyncTask(MainDao mainDao){
            this.mainDao = mainDao;
        }

        @Override
        protected Void doInBackground(MainData... mainDatas){
            mainDao.update(mainDatas[0]);
            return null;
        }
    }

    private static class DeleteMainDataAsyncTask extends AsyncTask<MainData, Void, Void>{
        private MainDao mainDao;

        private DeleteMainDataAsyncTask(MainDao mainDao){
            this.mainDao = mainDao;
        }

        @Override
        protected Void doInBackground(MainData... mainDatas){
            mainDao.delete(mainDatas[0]);
            return null;
        }

    }

    private static class GetMainDataByIdAsyncTask extends AsyncTask<Integer, Void, Void>{
        private MainDao mainDao;

        private GetMainDataByIdAsyncTask(MainDao mainDao){
            this.mainDao = mainDao;
        }

        @Override
        protected Void doInBackground(final Integer... id){
            mainDao.getDataById(id[0]);
            return null;
        }

    }



   
}
