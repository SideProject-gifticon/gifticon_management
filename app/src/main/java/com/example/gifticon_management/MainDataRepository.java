package com.example.gifticon_management;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;


import java.util.Calendar;
import java.util.GregorianCalendar;
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
        if(!mainData.getIsUsed())
            registerAlarm(mainData);
        else
            cancelAlarm(mainData.getId());
    }

    public void delete(MainData mainData){
        new DeleteMainDataAsyncTask(mainDao).execute(mainData);
        cancelAlarm(mainData.getId());
    }

    public void getDataById(int id){
        new GetMainDataByIdAsyncTask(mainDao).execute(id);
    }
    

    public LiveData<List<MainData>> getAllMainData() {
        return allMainData;
    }

    private static class InsertMainDataAsyncTask extends AsyncTask<MainData, Void, Void>{
        private MainDao mainDao;
        private long id;

        private InsertMainDataAsyncTask(MainDao mainDao){
            this.mainDao = mainDao;
        }

        @Override
        protected Void doInBackground(MainData... mainDatas){
            id = mainDao.insert(mainDatas[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            MainData mainData = mainDao.getDataById((int)id);
            registerAlarm(mainData);

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

    // 기프티콘 한 개 알람 등록
    private static void registerAlarm(MainData mainData) {
        int day = 3; // 임시로 3일 전 알림

        int yy = mainData.getYy();
        int mm = mainData.getMm();
        int dd = mainData.getDd();

        // 만료일로부터 3일 전의 시간 계산
        Calendar alarmCalendar = new GregorianCalendar(yy, mm, dd);
        alarmCalendar.add(Calendar.DAY_OF_MONTH, -day); // 만료일에서 3일을 뺌
        alarmCalendar.set(Calendar.HOUR_OF_DAY, 9); // 아침 9시에 울리도록 설정

        Context context = MyApp.getInstance().getApplicationContext(); // 애플리케이션 컨텍스트 가져오기
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, GiftExpirationReceiver.class);
        intent.putExtra("name", mainData.getText());
        intent.putExtra("ex",alarmCalendar.getTimeInMillis());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, mainData.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(), pendingIntent);
    }

    // 알람 취소
    private static void cancelAlarm(int id) {
        Context context = MyApp.getInstance().getApplicationContext(); // 애플리케이션 컨텍스트 가져오기
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Log.d("Cancel:",""+ id);

        Intent intent = new Intent(context, GiftExpirationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();
    }



   
}
