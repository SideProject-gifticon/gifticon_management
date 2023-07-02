package com.example.gifticon_management;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.ContentInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.ViewHolder> {



    private List<MainData> dataList;
   private Activity context;
    private RoomDB database;




    public RecyclerviewAdapter(){
        dataList = new ArrayList<>();
    }

    public MainData getMainDataAt(int position){
        return dataList.get(position);
    }

    // 터치 이벤트
    //아이템 클릭시 해당 아이템 위치와 데이터값을 넘겨야한다.
    public interface OnItemClickListener{
        void onItemClicked(int position, MainData mainData);

    }


    public interface OnItemLongClickListener
    {
        void onItemLongClick(View v, int pos);
    }

    private OnItemClickListener itemClickListener;
    private OnItemLongClickListener mLongListener = null;




    public void setOnItemClickListener (OnItemClickListener listener){
        itemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener)
    {
        this.mLongListener = listener;
    }



    @NonNull
    @Override
    public RecyclerviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.gifticon_ui, parent, false);
        RecyclerviewAdapter.ViewHolder vh = new RecyclerviewAdapter.ViewHolder(view);


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = vh.getAdapterPosition();
                MainData mainData = dataList.get(pos);
                if(pos != RecyclerView.NO_POSITION){
                    itemClickListener.onItemClicked(pos,mainData);
                }
            }
        });

        //롱터치 이벤트
        view.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                int pos = vh.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION)
                {
                    mLongListener.onItemLongClick(v, pos);
                }
                return true;
            }
        });



        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //String text = mData.get(position);

        //holder.imageView.setImageResource(R.drawable.sample);
        //holder.textView.setText(text);

        Converters converters = new Converters();

        final MainData data = dataList.get(position);
        database = RoomDB.getInstance(context);
        holder.textView.setText(data.getText());
        if(data.getIsUsed())holder.imageView.setImageBitmap(data.getUsedImage()); // 사용 완료 이미지
        else holder.imageView.setImageBitmap(data.getOriginImage()); // 원본 이미지

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void setMainData(List<MainData> dataList){
        this.dataList = dataList;
        notifyDataSetChanged();
    }


    // 뷰홀더
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.gifticon_image);
            textView = itemView.findViewById((R.id.gifticon_name));

        }


        public TextView getTextView(){
            return textView;
        }
    }



}
