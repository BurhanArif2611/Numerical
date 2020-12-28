package com.numerical.numerical.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.numerical.numerical.Models.Topics.Example;
import com.numerical.numerical.R;
import com.numerical.numerical.Utility.ErrorMessage;
import com.numerical.numerical.activity.DashBoardActivity;
import com.numerical.numerical.activity.FiterPageActivity;

import org.json.JSONArray;
import org.json.JSONObject;

public class Drawer_topics_Adapter extends RecyclerView.Adapter<Drawer_topics_Adapter.ViewHolder> {
    Context context;
    JSONArray jsonArray;
    int Check;
    String First_Check;

    public Drawer_topics_Adapter(Context context, JSONArray jsonArray, int position,String first_check) {
        this.jsonArray = jsonArray;
        this.context = context;
        this.Check = position;
        this.First_Check = first_check;
        /* */
    }

    @NonNull
    @Override
    public Drawer_topics_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_topics_adapter, parent, false);
        Drawer_topics_Adapter.ViewHolder viewHolder = new Drawer_topics_Adapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final Drawer_topics_Adapter.ViewHolder holder, final int position) {
        Example example = null;

        JSONArray jsonArray1 = null;
        String id="";

        try {
            //JSONArray jsonArray=new JSONArray(jsonArray);
            JSONObject jsonObject = jsonArray.getJSONObject(position);
            Glide.with(context).load(jsonObject.getString("assetPath")).into(holder.item_img);
            holder.topics_name_tv.setText(jsonObject.getString("topic"));
            id=jsonObject.getString("_id");
            jsonArray1 = jsonObject.getJSONArray("categories");
            Gson gson = new Gson();
            String responseData = jsonObject.toString();
            example = gson.fromJson(responseData, Example.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONArray finalJsonArray = jsonArray1;
        String finalId = id;

        if (Check == position) {
            if (First_Check.equals("fresh")){
                ((FiterPageActivity) context).onfresh(position, finalJsonArray, finalId,holder.topics_name_tv.getText().toString());

            }
            holder.selected_img.setVisibility(View.VISIBLE);
        } else {
            holder.selected_img.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    holder.selected_img.setVisibility(View.VISIBLE);
                    //    ((DashBoardActivity)context).Topics("Topics",finalExample.getId(),holder.topics_name_tv.getText().toString());
                    ((FiterPageActivity) context).onRefresh(position, finalJsonArray, finalId,holder.topics_name_tv.getText().toString());
                } catch (Exception e) {
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView topics_name_tv;
        ImageView item_img, selected_img;


        public ViewHolder(View itemView) {
            super(itemView);
            item_img = itemView.findViewById(R.id.item_img);
            selected_img = itemView.findViewById(R.id.selected_img);
            topics_name_tv = itemView.findViewById(R.id.topics_name_tv);


        }
    }
}
