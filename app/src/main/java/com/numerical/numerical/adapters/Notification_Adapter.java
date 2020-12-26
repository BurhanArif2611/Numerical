package com.numerical.numerical.adapters;

import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.numerical.numerical.Models.LatestFeed.Numeron;
import com.numerical.numerical.R;
import com.numerical.numerical.Utility.ErrorMessage;
import com.numerical.numerical.activity.LatestFeedDetailActivity;
import com.numerical.numerical.database.Notification_Model;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Notification_Adapter extends RecyclerView.Adapter<Notification_Adapter.ViewHolder> {
    Context context;
    List<Notification_Model> stringList;


    public Notification_Adapter(Context context, List<Notification_Model> numeronList) {
        this.stringList = numeronList;
        this.context = context;

    }

    @NonNull
    @Override
    public Notification_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_adapter, parent, false);
        Notification_Adapter.ViewHolder viewHolder = new Notification_Adapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public void onBindViewHolder(@NonNull final Notification_Adapter.ViewHolder holder, final int position) {
        Notification_Model notification_model = stringList.get(position);
        holder.title_tv.setText(notification_model.getTitle());
        holder.body_tv.setText(notification_model.getBody());
        Glide.with(context).load(notification_model.getImage_url()).into(holder.collection_img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("message",notification_model.getCollection_id());
                bundle.putString("Name", "Notification_Adapter");
                ErrorMessage.I(context,LatestFeedDetailActivity.class,bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title_tv, body_tv;
        CircleImageView collection_img;

        public ViewHolder(View itemView) {
            super(itemView);
            collection_img = (CircleImageView) itemView.findViewById(R.id.collection_img);
            body_tv = (TextView) itemView.findViewById(R.id.body_tv);
            title_tv = (TextView) itemView.findViewById(R.id.title_tv);
        }
    }

}
