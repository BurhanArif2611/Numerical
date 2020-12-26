package com.numerical.numerical.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.numerical.numerical.R;
import com.numerical.numerical.Utility.ApiClient;
import com.numerical.numerical.Utility.Const;
import com.numerical.numerical.Utility.ErrorMessage;
import com.numerical.numerical.Utility.NetworkUtil;
import com.numerical.numerical.Utility.SavedData;
import com.numerical.numerical.adapters.Drawer_topics_Adapter;
import com.numerical.numerical.adapters.SubCategory_Adapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FiterPageActivity extends BaseActivity {

    @BindView(R.id.topics_rcv)
    RecyclerView topicsRcv;
    JSONArray jsonArray;
    @BindView(R.id.sub_topics_rcv)
    RecyclerView subTopicsRcv;
    @BindView(R.id.apply_btn)
    Button applyBtn;
    @BindView(R.id.nested_layout)
    NestedScrollView nestedLayout;
    ArrayList<String> categorylist = new ArrayList<>();
    String Cat_id = "",Topic_Id="",Topic_name="";

    @Override
    protected int getContentResId() {
        return R.layout.activity_fiter_page;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarWithBackButton("Filter");
        ButterKnife.bind(this);
        categorylist.clear();
        GetTopics();
    }

    private void GetTopics() {
        if (NetworkUtil.isNetworkAvailable(FiterPageActivity.this)) {
            Call<ResponseBody> call = ApiClient.getLoadInterface().latest(Const.ENDPOINT.Topics);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            jsonArray = new JSONArray(response.body().string());
                            //System.out.println("===response data Summery:" + jsonArray.toString());
                            ErrorMessage.E("response" + jsonArray.toString());
                            Drawer_topics_Adapter side_rv_adapter = new Drawer_topics_Adapter(FiterPageActivity.this, jsonArray, Integer.parseInt(SavedData.getTopic_position()));
                            LinearLayoutManager linearLayoutManager = new GridLayoutManager(FiterPageActivity.this, 3);
                            topicsRcv.setLayoutManager(linearLayoutManager);
                            topicsRcv.setItemAnimator(new DefaultItemAnimator());
                            topicsRcv.setNestedScrollingEnabled(false);
                            topicsRcv.setAdapter(side_rv_adapter);
                            side_rv_adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            ErrorMessage.E("JSONException" + e.toString());
                            ErrorMessage.T(FiterPageActivity.this, "Server Error");
                        } catch (Exception e) {
                            e.printStackTrace();
                            ErrorMessage.E("Exception" + e.toString());
                            ErrorMessage.T(FiterPageActivity.this, "Server Error");
                        }


                    } else {
                        ErrorMessage.T(FiterPageActivity.this, "Response not successful");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(FiterPageActivity.this, "Response Fail");
                    System.out.println("============update profile fail  :" + t.toString());
                }
            });

        } else {
            ErrorMessage.T(FiterPageActivity.this, this.getString(R.string.no_internet));

        }

    }

    public void onRefresh(int position, JSONArray jsonArray1, String cat_id,String topic_name) {
        categorylist.clear();
        Topic_Id=cat_id;
        Topic_name=topic_name;
        SavedData.saveTopic_position(""+position);
        Drawer_topics_Adapter side_rv_adapter = new Drawer_topics_Adapter(FiterPageActivity.this, jsonArray, position);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(FiterPageActivity.this, 3);
        topicsRcv.setLayoutManager(linearLayoutManager);
        topicsRcv.setItemAnimator(new DefaultItemAnimator());
        topicsRcv.setNestedScrollingEnabled(false);
        topicsRcv.setAdapter(side_rv_adapter);
        side_rv_adapter.notifyDataSetChanged();
        ErrorMessage.E("jsonArray1" + jsonArray1.toString());

        if (jsonArray1.length() > 0) {
            nestedLayout.fullScroll(View.FOCUS_DOWN);
            subTopicsRcv.setVisibility(View.VISIBLE);
            SubCategory_Adapter subCategory_adapter = new SubCategory_Adapter(FiterPageActivity.this, jsonArray1, position);
            LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(FiterPageActivity.this, LinearLayoutManager.VERTICAL, false);
            subTopicsRcv.setLayoutManager(linearLayoutManager1);
            subTopicsRcv.setItemAnimator(new DefaultItemAnimator());
            subTopicsRcv.setNestedScrollingEnabled(false);
            subTopicsRcv.setAdapter(subCategory_adapter);
            subCategory_adapter.notifyDataSetChanged();
        } else {
            subTopicsRcv.setVisibility(View.GONE);
        }

    }

    @OnClick(R.id.apply_btn)
    public void onClick() {
        if (!Topic_name.equals("")) {
            if (categorylist.size() > 0) {
                for (int i = 0; i < categorylist.size(); i++) {
                    if (i == 0) {
                        Cat_id = categorylist.get(0);
                    } else {
                        Cat_id = Cat_id + "," + categorylist.get(i);
                    }
                }
                ErrorMessage.E("Category_id>>" + Cat_id);
                SavedData.saveCat_id(""+Cat_id);
            }
            Bundle bundle = new Bundle();
            bundle.putString("topic_id", Topic_Id);
            bundle.putString("Topic_name", Topic_name);
            bundle.putString("categorys", Cat_id);
            bundle.putString("Name", "");
            ErrorMessage.I_clear(FiterPageActivity.this, DashBoardActivity.class, bundle);
        }else {
           // ErrorMessage.T(FiterPageActivity.this,"Please Select Any One !");
            Toast.makeText(FiterPageActivity.this, "Please Select Any One !", Toast.LENGTH_SHORT).show();
        }
    }

    public void AddCategory_onList(String Category) {
        categorylist.add(Category);
    }

    public void unselectCategory(String id) {
        categorylist.remove(id);
        ErrorMessage.E("unselectCategory" + categorylist.size());
    }
}