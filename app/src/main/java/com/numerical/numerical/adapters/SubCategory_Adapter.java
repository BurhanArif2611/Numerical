package com.numerical.numerical.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.numerical.numerical.Models.Topics.Example;
import com.numerical.numerical.R;
import com.numerical.numerical.activity.FiterPageActivity;

import org.json.JSONArray;
import org.json.JSONObject;

public class SubCategory_Adapter extends RecyclerView.Adapter<SubCategory_Adapter.ViewHolder> {
    Context context;
    JSONArray jsonArray;
    int Check;

    public SubCategory_Adapter(Context context, JSONArray jsonArray, int position) {
        this.jsonArray = jsonArray;
        this.context = context;
        this.Check = position;
        /* */
    }

    @NonNull
    @Override
    public SubCategory_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.subcategory_layout, parent, false);
        SubCategory_Adapter.ViewHolder viewHolder = new SubCategory_Adapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final SubCategory_Adapter.ViewHolder holder, final int position) {

        try {
            holder.checkbox.setText(jsonArray.getString(position));
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    ((FiterPageActivity)context).AddCategory_onList(holder.checkbox.getText().toString());
                }
                else {
                    ((FiterPageActivity)context).unselectCategory(holder.checkbox.getText().toString());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        /*  TextView topics_name_tv;
          ImageView item_img, selected_img;*/
        CheckBox checkbox;

        public ViewHolder(View itemView) {
            super(itemView);
            checkbox = itemView.findViewById(R.id.checkbox);
           /* selected_img = (ImageView) itemView.findViewById(R.id.selected_img);
            topics_name_tv = (TextView) itemView.findViewById(R.id.topics_name_tv);*/


        }
    }
}
