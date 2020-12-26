package com.numerical.numerical.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.numerical.numerical.R;
import com.numerical.numerical.Utility.SavedData;
import com.numerical.numerical.adapters.Notification_Adapter;
import com.numerical.numerical.database.NotificationHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationActivity extends BaseActivity {

    @BindView(R.id.notification_list_rv)
    RecyclerView notificationListRv;
    @BindView(R.id.no_data_found_tv)
    TextView noDataFoundTv;


    @Override
    protected int getContentResId() {
        return R.layout.activity_notification;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarWithBackButton("Notifications");
        ButterKnife.bind(this);
        SavedData.saveAddToCart_Count("0");
        if (NotificationHelper.getInstance().getNotification_Model().size() > 0) {
            noDataFoundTv.setVisibility(View.GONE);
            notificationListRv.setVisibility(View.VISIBLE);
            Notification_Adapter side_rv_adapter = new Notification_Adapter(NotificationActivity.this, NotificationHelper.getInstance().getNotification_Model());
            LinearLayoutManager gridLayoutManager = new LinearLayoutManager(NotificationActivity.this);
            notificationListRv.setLayoutManager(gridLayoutManager);
            notificationListRv.setItemAnimator(new DefaultItemAnimator());
            notificationListRv.setNestedScrollingEnabled(false);
            notificationListRv.setAdapter(side_rv_adapter);
        }else {
            noDataFoundTv.setVisibility(View.VISIBLE);
            notificationListRv.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        if (NotificationHelper.getInstance().getNotification_Model().size() > 0) {
            noDataFoundTv.setVisibility(View.GONE);
            notificationListRv.setVisibility(View.VISIBLE);
            Notification_Adapter side_rv_adapter = new Notification_Adapter(NotificationActivity.this, NotificationHelper.getInstance().getNotification_Model());
            LinearLayoutManager gridLayoutManager = new LinearLayoutManager(NotificationActivity.this);
            notificationListRv.setLayoutManager(gridLayoutManager);
            notificationListRv.setItemAnimator(new DefaultItemAnimator());
            notificationListRv.setNestedScrollingEnabled(false);
            notificationListRv.setAdapter(side_rv_adapter);
        }else {
            noDataFoundTv.setVisibility(View.VISIBLE);
            notificationListRv.setVisibility(View.GONE);
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (NotificationHelper.getInstance().getNotification_Model().size() > 0) {
            noDataFoundTv.setVisibility(View.GONE);
            notificationListRv.setVisibility(View.VISIBLE);
            Notification_Adapter side_rv_adapter = new Notification_Adapter(NotificationActivity.this, NotificationHelper.getInstance().getNotification_Model());
            LinearLayoutManager gridLayoutManager = new LinearLayoutManager(NotificationActivity.this);
            notificationListRv.setLayoutManager(gridLayoutManager);
            notificationListRv.setItemAnimator(new DefaultItemAnimator());
            notificationListRv.setNestedScrollingEnabled(false);
            notificationListRv.setAdapter(side_rv_adapter);
        }else {
            noDataFoundTv.setVisibility(View.VISIBLE);
            notificationListRv.setVisibility(View.GONE);
        }
        super.onPause();
    }
}
