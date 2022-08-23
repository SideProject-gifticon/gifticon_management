package com.example.gifticon_management;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.ContentInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    //private ArrayList<String> mData = null;

    private List<MainData> dataList;
    private Activity context;
    private RoomDB database;

    public RecyclerviewAdapter(Activity context, List<MainData> dataList){
        this.context=context;
        this.dataList=dataList;
        notifyDataSetChanged();
    }

    RecyclerviewAdapter(List<MainData> list){
        dataList = list;
    }

    // 터치 이벤트
    public interface OnItemClickListener{
        void onItemClicked(int position, String data);
    }

    private OnItemClickListener itemClickListener;

    public void setOnItemClickListener (OnItemClickListener listener){
        itemClickListener = listener;
    }

    @NonNull
    @Override
    public RecyclerviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.gifticon_ui, parent, false);
        RecyclerviewAdapter.ViewHolder vh = new RecyclerviewAdapter.ViewHolder(view);


        // 터치 이벤트

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data="";
                int pos = vh.getAdapterPosition();
                if(pos != RecyclerView.NO_POSITION){
                    data = vh.getTextView().getText().toString();
//                    Intent intent = new Intent(view.getContext(), gifticon_touch_activity.class);
//                    intent.putExtra("name",dataList.get(pos).getText());
//                    intent.putExtra("date",dataList.get(pos).getDate_text());
//                    intent.putExtra("image_gif",dataList.get(pos).getImage());
//                    view.getContext().startActivity(intent);
                }
                itemClickListener.onItemClicked(pos,data);
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
        holder.imageView.setImageBitmap(data.getImage());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
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
