package com.numerical.numerical.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.numerical.numerical.Models.LatestFeed.Numeron;
import com.numerical.numerical.R;
import com.numerical.numerical.Utility.ApiClient;
import com.numerical.numerical.Utility.Const;
import com.numerical.numerical.Utility.ErrorMessage;
import com.numerical.numerical.Utility.NetworkUtil;
import com.numerical.numerical.Utility.SavedData;
import com.numerical.numerical.activity.DashBoardActivity;
import com.numerical.numerical.activity.LatestFeedDetailActivity;
import com.numerical.numerical.database.UserProfileHelper;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.TimeZone;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.numerical.numerical.activity.LatestFeedDetailActivity.PublisherAlreadyFollow;

public class Standeredview_Adapter extends PagerAdapter {
    Context context;
    private List<Numeron> numeronList;
    private LayoutInflater inflater;
    // private TextView likecountTv;
    String ID, displayName, follow, Author_id, Views;

    public Standeredview_Adapter(Context context, List<Numeron> riskQuestionModelList, String id, String DisplayName, String Follow, String author_id, String views) {
        this.context = context;
        this.numeronList = riskQuestionModelList;
        this.ID = id;
        this.displayName = DisplayName;
        this.follow = Follow;
        this.Author_id = author_id;
        this.Views = views;
        inflater = LayoutInflater.from(context);
    }

    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return numeronList.size();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view.equals(o);
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View views = inflater.inflate(R.layout.standerdview_adapter, view, false);
        ImageView item_img = (ImageView) views.findViewById(R.id.item_img);
        TextView dateTv = (TextView) views.findViewById(R.id.date_tv);
        TextView likecountTv = (TextView) views.findViewById(R.id.likecount_tv);
        TextView viewTv = (TextView) views.findViewById(R.id.view_tv);
        TextView commentTv = (TextView) views.findViewById(R.id.comment_tv);
        ImageView action_menu_img = (ImageView) views.findViewById(R.id.action_menu_img);
        TextView sourceTv = (TextView) views.findViewById(R.id.source_tv);
        TextView placeofimage_tv = (TextView) views.findViewById(R.id.placeofimage_tv);
        TextView numeral_tv = (TextView) views.findViewById(R.id.numeral_tv);
        TextView numeral_detail_tv = (TextView) views.findViewById(R.id.numeral_detail_tv);
        TextView authore_tv = (TextView) views.findViewById(R.id.authore_tv);
        ImageButton menu_btn = (ImageButton) views.findViewById(R.id.menu_btn);
        LinearLayout profile = (LinearLayout) views.findViewById(R.id.profile);
        authore_tv.setText(displayName);

        if (numeronList.get(position).getActions().size() > 0){
            action_menu_img.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_more_fill));
        }
        if (!PublisherAlreadyFollow.equals("")) {
            menu_btn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_follow_green));
        } else {
            menu_btn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_follow_color));
        }
        RecyclerView tagsRv = (RecyclerView) views.findViewById(R.id.tags_rv);
        ErrorMessage.E("assetpath" + numeronList.get(position).getAssetPath());
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("Calling", "bypublisher");
                bundle.putString("Id", Author_id);
                bundle.putString("Name",displayName);
                ErrorMessage.I(context, DashBoardActivity.class, bundle);
            }
        });
        menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PublisherAlreadyFollow.equals("")) {
                    if (NetworkUtil.isNetworkAvailable(context)) {
                        Call<ResponseBody> call = ApiClient.getLoadInterface().FollowNumerouns(Const.ENDPOINT.FollowCollections + "publisher/" + Author_id + "/" + SavedData.getTokan());
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                ErrorMessage.E("PublisherfollowOnServer response" + response.code());
                                if (response.isSuccessful()) {
                                    try {
                                        ErrorMessage.E("error code" + response.body().string());
                                        if (response.code() == 200) {
                                            menu_btn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_follow_green));
                                            ((LatestFeedDetailActivity) context).Track_Event("Follow", "Follow Publisher", Author_id, "numerical_event");
                                            PublisherAlreadyFollow = "Yes";
                                            ((LatestFeedDetailActivity) context).change_Status("Yes");
                                        } else {
                                        }
                                    } catch (Exception e) {
                                    }
                                } else {
                                    ErrorMessage.T(context, "Response not successful");
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                ErrorMessage.T(context, "Response Fail");
                                System.out.println("============update profile fail  :" + t.toString());

                            }
                        });

                    } else {
                        ErrorMessage.T(context, context.getString(R.string.no_internet));
                    }
                } else {

                    if (NetworkUtil.isNetworkAvailable(context)) {
                        Call<ResponseBody> call = ApiClient.getLoadInterface().FollowNumerouns(Const.ENDPOINT.UNFollowCollections + "publisher/" + Author_id + "/" + SavedData.getTokan());
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                ErrorMessage.E("followOnServer response" + response.code());
                                if (response.isSuccessful()) {
                                    try {
                                        ErrorMessage.E("error code" + response.body().string());
                                        if (response.code() == 200) {
                                            menu_btn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_follow_color));
                                            ((LatestFeedDetailActivity) context).Track_Event("Follow", "Unfollow Publisher", Author_id, "numerical_event");
                                            PublisherAlreadyFollow = "";
                                            ((LatestFeedDetailActivity) context).change_Status("");
                                        } else {
                                        }
                                    } catch (Exception e) {

                                    }
                                } else {
                                    ErrorMessage.T(context, "Response not successful");
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                ErrorMessage.T(context, "Response Fail");
                                System.out.println("============update profile fail  :" + t.toString());

                            }
                        });

                    } else {
                        ErrorMessage.T(context, context.getString(R.string.no_internet));
                    }
                }
            }
        });
        if (UserProfileHelper.getInstance().getUserProfileModel().size() > 0) {
            commentTv.setVisibility(View.VISIBLE);
        } else {
            commentTv.setVisibility(View.GONE);
        }
        try {
            if (numeronList.get(position).getAssetPath() == null) {
                placeofimage_tv.setVisibility(View.VISIBLE);
                numeral_tv.setVisibility(View.GONE);
                item_img.setVisibility(View.GONE);
                placeofimage_tv.setText(numeronList.get(position).getNumeral());
                ((LatestFeedDetailActivity) context).FirebaseAnalytics(ID + "-" + numeronList.get(position).getId(), numeronList.get(position).getTitle(), "View Numeron in Collection");

                if (numeronList.get(position).getSubType().equals("panel-angry")) {
                    placeofimage_tv.setBackgroundColor(context.getResources().getColor(R.color.panel_angry));
                } else if (numeronList.get(position).getSubType().equals("panel-formal")) {
                    placeofimage_tv.setBackgroundColor(context.getResources().getColor(R.color.panel_formal));
                } else if (numeronList.get(position).getSubType().equals("panel-sad")) {
                    placeofimage_tv.setBackgroundColor(context.getResources().getColor(R.color.panel_sad));
                } else if (numeronList.get(position).getSubType().equals("panel-calm")) {
                    placeofimage_tv.setBackgroundColor(context.getResources().getColor(R.color.panel_calm));
                } else if (numeronList.get(position).getSubType().equals("panel-positive")) {
                    placeofimage_tv.setBackgroundColor(context.getResources().getColor(R.color.panel_positive));
                } else if (numeronList.get(position).getSubType().equals("panel-royal")) {
                    placeofimage_tv.setBackgroundColor(context.getResources().getColor(R.color.panel_royal));
                } else if (numeronList.get(position).getSubType().equals("panel-happy")) {
                    placeofimage_tv.setBackgroundColor(context.getResources().getColor(R.color.panel_happy));
                }
            } else {
                placeofimage_tv.setVisibility(View.GONE);
                item_img.setVisibility(View.VISIBLE);
                if (numeronList.get(position).getAssetPath().contains("https://")) {
                    Glide.with(context).load(numeronList.get(position).getAssetPath()).into(item_img);
                } else if (numeronList.get(position).getAssetPath().contains("http://")) {
                    Glide.with(context).load(numeronList.get(position).getAssetPath()).into(item_img);
                } else {
                    Glide.with(context).load("https://numerical.co.in" + numeronList.get(position).getAssetPath()).into(item_img);
                }
            }
            try {
                if (Views.equals("null") || Views == null) {
                    viewTv.setText("0");
                } else {
                    viewTv.setText("" + Views);
                }
            } catch (Exception e) {
            }
            sourceTv.setText(numeronList.get(position).getSource());
            numeral_tv.setText(numeronList.get(position).getNumeral());
            numeral_detail_tv.setText(numeronList.get(position).getTitle());
            ErrorMessage.E("Likes" + numeronList.get(position).getLikes());
            if (numeronList.get(position).getLikes() == null) {
                likecountTv.setText("0");
            } else {
                likecountTv.setText("" + numeronList.get(position).getLikes());
            }
            commentTv.setText("0");

            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy - hh:mm a");
            Date d = null;
            try {
                input.setTimeZone(TimeZone.getTimeZone("IMP"));
                d = input.parse(numeronList.get(position).getLastModified());
            } catch (Exception e) {
                e.printStackTrace();
            }
            String formatted = output.format(d);
            dateTv.setText("Last Modified: " + formatted);

            Tags_Adapter tags_adapter = new Tags_Adapter(context, numeronList.get(position).getCategories());
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            tagsRv.setLayoutManager(linearLayoutManager);
            tagsRv.setItemAnimator(new DefaultItemAnimator());
            tagsRv.setAdapter(tags_adapter);
            tags_adapter.notifyDataSetChanged();
            tagsRv.smoothScrollToPosition(1);

            sourceTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (numeronList.get(position).getUrl() != null && !numeronList.get(position).getUrl().equals("")) {
                        ((LatestFeedDetailActivity) context).Track_Event("Source Link", "Click", numeronList.get(position).getUrl(), "numerical_event");
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                        browserIntent.setData(Uri.parse(numeronList.get(position).getUrl()));
                        context.startActivity(browserIntent);
                    } else {
                        ErrorMessage.T(context, "URL not available !");
                    }

                }
            });
            action_menu_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (numeronList.get(position).getActions().size() > 0) {
                        PopupMenu popup = new PopupMenu(context, v);
                        for (int i = 0; i < numeronList.get(position).getActions().size(); i++) {
                            popup.getMenu().add(Menu.NONE, i, i, numeronList.get(position).getActions().get(i).getName());
                        }

                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                int i = item.getItemId();
                              /*  if (i == 0) {
                                    try {
                                        Intent intent = new Intent(Intent.ACTION_DIAL);
                                        intent.setData(Uri.parse("tel:" + numeronList.get(position).getActions().get(0).getUrl()));
                                        context.startActivity(intent);
                                    } catch (Exception e) {
                                    }
                                    return true;
                                } else if (i == 1) {
                                    try {
                                        ErrorMessage.E("ACTION_DIAL" + numeronList.get(position).getActions().get(1).getUrl());
                       *//* Intent send = new Intent(Intent.ACTION_SENDTO);
                        send.setType("text/plain");
                        send.putExtra(Intent.EXTRA_EMAIL, numeronList.get(position).getActions().get(1).getUrl());
                        send.putExtra(Intent.EXTRA_SUBJECT, "Numerical");
                        startActivity(Intent.createChooser(send, "Send message"));*//*

                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        Uri data = Uri.parse("mailto:" + numeronList.get(position).getActions().get(1).getUrl() + "?subject=" + numeronList.get(position).getActions().get(1).getName());
                                        intent.setData(data);
                                        context.startActivity(intent);
                                    } catch (Exception e) {
                                        //e.toString();
                                    }
                                    return true;
                                } else if (i == 2) {
                                    try {
                                        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                                        browserIntent.setData(Uri.parse(numeronList.get(position).getActions().get(2).getUrl()));
                                        context.startActivity(browserIntent);
                                    } catch (Exception e) {
                                    }
                                } else if (i == 3) {
                                    try {
                                        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                                        browserIntent.setData(Uri.parse(numeronList.get(position).getActions().get(3).getUrl()));
                                        context.startActivity(browserIntent);
                                    } catch (Exception e) {
                                    }
                                } else if (i == 4) {
                                    try {
                                        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                                        browserIntent.setData(Uri.parse(numeronList.get(position).getActions().get(4).getUrl()));
                                        context.startActivity(browserIntent);
                                    } catch (Exception e) {
                                    }
                                } else if (i == 5) {
                                    try {
                                        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                                        browserIntent.setData(Uri.parse(numeronList.get(position).getActions().get(5).getUrl()));
                                        context.startActivity(browserIntent);
                                    } catch (Exception e) {
                                    }
                                }*/
                                if (i == 0) {
                                    try {
                                        if (numeronList.get(position).getActions().get(0).getActionInternal().equals(true)) {
                                            if (numeronList.get(position).getActions().get(0).getIdentifier().equals("CallNow")) {
                                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                                intent.setData(Uri.parse("tel:" + numeronList.get(position).getActions().get(0).getUrl()));
                                                context.startActivity(intent);
                                            } else if (numeronList.get(position).getActions().get(0).getIdentifier().equals("SendMessage")) {
                                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                                Uri data = Uri.parse("mailto:" + numeronList.get(position).getActions().get(0).getUrl() + "?subject=" + numeronList.get(position).getActions().get(0).getName());
                                                intent.setData(data);
                                                context.startActivity(intent);
                                            }
                                        } else if (numeronList.get(position).getActions().get(0).getActionInternal().equals(false)) {
                                            Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                                            browserIntent.setData(Uri.parse(numeronList.get(position).getActions().get(0).getUrl()));
                                            context.startActivity(browserIntent);
                                        }
                                    } catch (Exception e) {
                                    }
                                    return true;
                                } else if (i == 1) {
                                    try {
                                        if (numeronList.get(position).getActions().get(1).getActionInternal().equals(true)) {
                                            if (numeronList.get(position).getActions().get(1).getIdentifier().equals("CallNow")) {
                                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                                intent.setData(Uri.parse("tel:" + numeronList.get(position).getActions().get(1).getUrl()));
                                                context.startActivity(intent);
                                            } else if (numeronList.get(position).getActions().get(1).getIdentifier().equals("SendMessage")) {
                                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                                Uri data = Uri.parse("mailto:" + numeronList.get(position).getActions().get(1).getUrl() + "?subject=" + numeronList.get(position).getActions().get(1).getName());
                                                intent.setData(data);
                                                context.startActivity(intent);
                                            }
                                        } else if (numeronList.get(position).getActions().get(1).getActionInternal().equals(false)) {
                                            Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                                            browserIntent.setData(Uri.parse(numeronList.get(position).getActions().get(1).getUrl()));
                                            context.startActivity(browserIntent);
                                        }
                                    } catch (Exception e) {
                                        //e.toString();
                                    }
                                    return true;
                                } else if (i == 2) {
                                    try {
                                        if (numeronList.get(position).getActions().get(2).getActionInternal().equals(true)) {
                                            if (numeronList.get(position).getActions().get(2).getIdentifier().equals("CallNow")) {
                                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                                intent.setData(Uri.parse("tel:" + numeronList.get(position).getActions().get(2).getUrl()));
                                                context. startActivity(intent);
                                            } else if (numeronList.get(position).getActions().get(2).getIdentifier().equals("SendMessage")) {
                                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                                Uri data = Uri.parse("mailto:" + numeronList.get(position).getActions().get(2).getUrl() + "?subject=" + numeronList.get(position).getActions().get(2).getName());
                                                intent.setData(data);
                                                context.startActivity(intent);
                                            }
                                        } else if (numeronList.get(position).getActions().get(2).getActionInternal().equals(false)) {
                                            Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                                            browserIntent.setData(Uri.parse(numeronList.get(position).getActions().get(2).getUrl()));
                                            context.startActivity(browserIntent);
                                        }
                                    } catch (Exception e) {
                                    }
                                } else if (i == 3) {
                                    try {
                                        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                                        browserIntent.setData(Uri.parse(numeronList.get(position).getActions().get(3).getUrl()));
                                        context.startActivity(browserIntent);
                                    } catch (Exception e) {
                                    }
                                } else if (i == 4) {
                                    try {
                                        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                                        browserIntent.setData(Uri.parse(numeronList.get(position).getActions().get(4).getUrl()));
                                        context.startActivity(browserIntent);
                                    } catch (Exception e) {
                                    }
                                } else if (i == 5) {
                                    try {
                                        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                                        browserIntent.setData(Uri.parse(numeronList.get(position).getActions().get(5).getUrl()));
                                        context.startActivity(browserIntent);
                                    } catch (Exception e) {
                                    }
                                }
                                return false;
                            }
                        });
                        popup.show();
                    } else {
                        ErrorMessage.T(context, "Action not found!");
                    }
                }
            });

            if (numeronList.get(position).getLikedAnon().size() > 0) {
               /* while (((LatestFeedDetailActivity)context).ee.hasMoreElements()) {
                    NetworkInterface n = (NetworkInterface) (((LatestFeedDetailActivity)context).ee.nextElement());
                    Enumeration ee = n.getInetAddresses();
                    while (ee.hasMoreElements()) {
                        InetAddress ip = (InetAddress) ee.nextElement();
                        ErrorMessage.E("Standard getLocalIpAddress_third" + ip.getHostAddress());
                        for (int i = 0; i < numeronList.get(position).getLikedAnon().size(); i++) {
                            ErrorMessage.E("Like" + numeronList.get(position).getLikedAnon().get(i).getUserIP());
                            if (("::ffff:" + ip.getHostAddress()).contains(numeronList.get(position).getLikedAnon().get(i).getUserIP())) {
                                ErrorMessage.E("Standard getLocalIpAddress_third inner" + ip.getHostAddress() + ">>" + numeronList.get(position).getLikedAnon().get(i).getUserIP());
                                // likeImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_like_fill));
                                likecountTv.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.ic_like_fill), null, null, null);

                            }
                        }
                    }
                }*/
                likecountTv.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.ic_like_fill), null, null, null);
            }
            view.addView(views);
        } catch (Exception e) {
            ErrorMessage.E("Exception" + e.toString());
        }
        likecountTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtil.isNetworkAvailable(context)) {
                    Call<ResponseBody> call = ApiClient.getLoadInterface().LikeNumerouns(Const.ENDPOINT.LikeNumerouns + ID + "/numeron/" + numeronList.get(position).getId() + "/like");
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            ErrorMessage.E("error code" + response.code());
                            if (response.isSuccessful()) {
                                try {
                                    int Count = 0;
                                    if (response.code() == 200) {
                                        Count = Integer.parseInt(likecountTv.getText().toString());
                                        Count++;
                                        ErrorMessage.E("Count" + Count);
                                        likecountTv.setText("" + Count);
                                        likecountTv.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.ic_like_fill), null, null, null);
                                        ((LatestFeedDetailActivity) context).Track_Event("Like", "Like Numeron in Collection", ID + "-" + numeronList.get(position).getId(), "numerical_event");

                                    } else {

                                    }
                                } catch (Exception e) {
                                    ErrorMessage.E("Standeredview" + e.toString());
                                }
                            } else {
                                //ErrorMessage.T(context, "Response not successful");

                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            ErrorMessage.T(context, "Response Fail");
                            System.out.println("============update profile fail  :" + t.toString());

                        }
                    });

                } else {
                    ErrorMessage.T(context, context.getString(R.string.no_internet));
                }
            }
        });
        LocalBroadcastManager.getInstance(context).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, Intent intent) {
                String message = intent.getStringExtra("message");
                ErrorMessage.E("Standeredview_Adapter StandardAdapter>>>" + message);
                ErrorMessage.E("Standeredview_Adapter StandardAdapter>>>" + numeronList.get(position).getId());
                String Position = String.valueOf(position + 1);
                ErrorMessage.E("StandardAdapter" + intent.getStringExtra("position") + ">>>" + Position);
                if (intent.getStringExtra("position").equals(Position)) {
                    if (NetworkUtil.isNetworkAvailable(context)) {
                        Call<ResponseBody> call = ApiClient.getLoadInterface().LikeNumerouns(Const.ENDPOINT.LikeNumerouns + message + "/numeron/" + numeronList.get(position).getId() + "/like");
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                ErrorMessage.E("error code" + response.code());
                                if (response.isSuccessful()) {
                                    try {
                                        int Count = 0;
                                        if (response.code() == 200) {
                                            Count = Integer.parseInt(likecountTv.getText().toString());
                                            Count++;
                                            ErrorMessage.E("Count" + Count);
                                            likecountTv.setText("" + Count);
                                            likecountTv.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.ic_like_fill), null, null, null);
                                        } else {
                                        }
                                    } catch (Exception e) {
                                        ErrorMessage.E("Standeredview" + e.toString());
                                    }
                                } else {
                                    //ErrorMessage.T(context, "Response not successful");
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                ErrorMessage.T(context, "Response Fail");
                                System.out.println("============update profile fail  :" + t.toString());
                            }
                        });

                    } else {
                        ErrorMessage.T(context, context.getString(R.string.no_internet));
                    }
                }
            }
        }, new IntentFilter("StanderdView"));

        commentTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //((LatestFeedDetailActivity) context).CallCommentScreen(numeronList.get(position).getId());
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri data = Uri.parse("mailto:" + "" + "?subject=" + numeronList.get(position).getTitle());
                    intent.setData(data);
                    context.startActivity(intent);
                } catch (Exception e) {
                }
            }
        });
        return views;

    }

}
