package com.numerical.numerical.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.numerical.numerical.Models.GetImages;
import com.numerical.numerical.Models.LatestFeed.Example;
import com.numerical.numerical.R;
import com.numerical.numerical.Utility.ApiClient;
import com.numerical.numerical.Utility.Const;
import com.numerical.numerical.Utility.ErrorMessage;
import com.numerical.numerical.Utility.NetworkUtil;
import com.numerical.numerical.Utility.SavedData;
import com.numerical.numerical.adapters.CommentAdapter;
import com.numerical.numerical.database.NotificationHelper;
import com.numerical.numerical.fragments.ListingViewFragment;
import com.numerical.numerical.fragments.StandredViewFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LatestFeedDetailActivity extends BaseActivity {
    private static File file;
    @BindView(R.id.authore_tv)
    TextView authoreTv;
    @BindView(R.id.tilte_tv)
    TextView tilteTv;
    @BindView(R.id.date_tv)
    TextView dateTv;
    @BindView(R.id.like_img)
    ImageView likeImg;
    @BindView(R.id.share_img)
    ImageView shareImg;
    @BindView(R.id.follow_byn_img)
    ImageView followBynImg;
    @BindView(R.id.action_menu_img)
    ImageView actionMenuImg;
    @BindView(R.id.back_btn_tv)
    TextView backBtnTv;
    @BindView(R.id.comment_rv)
    RecyclerView commentRv;
    @BindView(R.id.comment_relativelayout)
    RelativeLayout commentRelativelayout;
    @BindView(R.id.bottom_cardview)
    CardView bottomCardview;
    @BindView(R.id.write_comment_etv)
    EditText writeCommentEtv;
    @BindView(R.id.profile)
    LinearLayout profile;
    @BindView(R.id.menu_btn)
    ImageButton menuBtn;
    @BindView(R.id.convert_to_image_img)
    ImageView convertToImageImg;
    @BindView(R.id.top_relative_layout)
    RelativeLayout relativeLayout;
    ProgressDialog mProgressDialog;
    private FragmentTransaction mFragmentTransaction;
    FragmentManager detailsFragment1 = getSupportFragmentManager();
    private View view_settings;
    private String fragmentTitle = "";
    private Example example;
    private Intent intent;
    String Collection_id = "";
    String AlreadyFollow = "";
    public static String PublisherAlreadyFollow = "";
    private FirebaseAnalytics mFirebaseAnalytics;
    private String ip;
    public static Enumeration ee;

    //private Tracker mTracker;
    @Override
    protected int getContentResId() {
        return R.layout.activity_latest_feed_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarWithBackButton("");
        ButterKnife.bind(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
       /* MyApplication application = (MyApplication) getApplication();
        mTracker = application.getDefaultTracker();*/
        try {
            Intent mainIntent = getIntent();
            try {
                if (mainIntent != null && mainIntent.getData() != null && (mainIntent.getData().getScheme().equals("https"))) {
                    Uri data = mainIntent.getData();
                    String str = data.toString();
                    ErrorMessage.E("By Browser id>>" + str.substring(str.lastIndexOf("/") + 1));
                    FeedDetail(str.substring(str.lastIndexOf("/") + 1));
                }
            } catch (Exception e) {
            }

            if (getIntent().getStringExtra("Name").equals("")) {
                example = (Example) getIntent().getSerializableExtra("ItemData");
                tilteTv.setText(example.getTitle());
                authoreTv.setText(example.getUser().getDisplayName());
                Collection_id = example.getId();
                FeedDetail(Collection_id);

            } else if (getIntent().getStringExtra("Name").equals("Firebase")) {
                ErrorMessage.E("FeedDetail" + getIntent().getStringExtra("message"));
                Collection_id = getIntent().getStringExtra("message");
                Collection_id = Collection_id.substring(Collection_id.lastIndexOf("/")).replaceAll("/", "");
                ErrorMessage.E("FeedDetail" + Collection_id);
                FeedDetail(Collection_id);
            } else if (getIntent().getStringExtra("Name").equals("Notification_Adapter")) {
                Collection_id = getIntent().getStringExtra("message");
                try {
                    NotificationHelper.getInstance().deleteById(Collection_id);
                } catch (Exception e) {
                    ErrorMessage.E("Exception" + e.toString());
                }
                ErrorMessage.E("FeedDetail" + Collection_id);
                FeedDetail(Collection_id);

            } else {
                example = (Example) getIntent().getSerializableExtra("ItemData");
                tilteTv.setText(example.getTitle());
                authoreTv.setText(example.getUser().getDisplayName());
                setToolbarWithBackButton(getIntent().getStringExtra("Name"));
                if (example.getIsCollection()) {
                    tilteTv.setVisibility(View.VISIBLE);
                } else {
                    tilteTv.setVisibility(View.GONE);
                }
                Collection_id = example.getId();
                FeedDetail(Collection_id);
                //FirebaseAnalytics(Collection_id, example.getTitle(), "View Collection");

            }
            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy - hh:mm a");
            Date d = null;
            try {
                input.setTimeZone(TimeZone.getTimeZone("IMP"));
                d = input.parse(example.getLastModified());
            } catch (Exception e) {
                e.printStackTrace();
            }
            /*String formatted = output.format(d);
            dateTv.setText(formatted);
            ListingViewFragment presonalDetailsFragment1 = new ListingViewFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("ItemData", example);
            presonalDetailsFragment1.setArguments(bundle);
            mFragmentTransaction = detailsFragment1.beginTransaction();
            mFragmentTransaction.replace(R.id.fragmentsLayout, presonalDetailsFragment1);
            mFragmentTransaction.commit();*/

        } catch (Exception e) {
        }

        backBtnTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animSlideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
                commentRelativelayout.startAnimation(animSlideUp);
                commentRelativelayout.setVisibility(View.GONE);
                bottomCardview.setVisibility(View.VISIBLE);
            }
        });

       /* ErrorMessage.E("First IP"+getLocalIpAddress());
        ErrorMessage.E("Second IP"+getIPAddress(true));*/
        getLocalIpAddress_third();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        try {
            if (example.getIsCollection()) {
                getMenuInflater().inflate(R.menu.list_view, menu);
                view_settings = findViewById(R.id.view_settings);
            } else {
            }
        } catch (Exception e) {
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        view_settings = findViewById(R.id.view_settings);

        //noinspection SimplifiableIfStatement
        if (id == R.id.view_settings) {
            if (fragmentTitle.equals("Standerd")) {
                ListingViewFragment presonalDetailsFragment1 = new ListingViewFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("ItemData", example);
                presonalDetailsFragment1.setArguments(bundle);
                mFragmentTransaction = detailsFragment1.beginTransaction();
                mFragmentTransaction.replace(R.id.fragmentsLayout, presonalDetailsFragment1);
                mFragmentTransaction.commit();
                bottomCardview.setVisibility(View.VISIBLE);
            } else if (fragmentTitle.equals("Listview")) {
                ErrorMessage.E("PublisherAlreadyFollow" + PublisherAlreadyFollow);
                StandredViewFragment presonalDetailsFragment1 = new StandredViewFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("ItemData", example);
                bundle.putString("Publisherfollow", PublisherAlreadyFollow);
                presonalDetailsFragment1.setArguments(bundle);
                mFragmentTransaction = detailsFragment1.beginTransaction();
                mFragmentTransaction.replace(R.id.fragmentsLayout, presonalDetailsFragment1);
                mFragmentTransaction.commit();
                bottomCardview.setVisibility(View.GONE);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void launchFragmentTitle(String fragemntName) {
        fragmentTitle = fragemntName;

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        invalidateOptionsMenu();
        try {

            if (example.getIsCollection()) {
                if (fragmentTitle.equals("Listview")) {
                    menu.findItem(R.id.view_settings).setVisible(true).setIcon(getResources().getDrawable(R.drawable.ic_list_view));
                } else if (fragmentTitle.equals("Standerd")) {
                    menu.findItem(R.id.view_settings).setVisible(true).setIcon(getResources().getDrawable(R.drawable.ic_standard_view));

                }
            }

        } catch (Exception e) {
        }
        return super.onPrepareOptionsMenu(menu);
    }

    public void share() {
        String sAux = " Numerical App\n\n";
        try {
            if (example.getIsCollection()) {
                sAux = sAux + "https://numerical.co.in/numerons/collection/" + example.getId();
            } else {
                sAux = sAux + "https://numerical.co.in/numerons/numeron/" + example.getId();

            }

            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "Numerical");
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "choose one"));
        } catch (Exception e) {
            //e.toString();
        }
    }

    @OnClick({R.id.like_img, R.id.share_img, R.id.profile, R.id.follow_byn_img, R.id.action_menu_img, R.id.menu_btn, R.id.convert_to_image_img})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.like_img:
                sendMessage();
                break;
            case R.id.share_img:
                share();
                break;
            case R.id.follow_byn_img:
                try {
                    if (AlreadyFollow.equals("")) {
                        followOnServer();
                    } else {
                        UnfollowOnServer();
                    }
                } catch (Exception e) {
                    Log.e("else is working", ">>");
                }
                break;
            case R.id.action_menu_img:
                try {
                    if (example.getActions().size() > 0) {
                        showPopup(view);
                    } else {
                        // ErrorMessage.T(LatestFeedDetailActivity.this, "Action not found!");
                        Toast.makeText(LatestFeedDetailActivity.this, "Action not found!", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                }
                break;
            case R.id.convert_to_image_img:
                try {
                    // takeScreenshot();
                    //TakeImageOnServer();
                    String fname = "";
                    if (example.getTitle().length() > 50) {
                        fname = example.getTitle().substring(0, 50);
                    } else {
                        fname = example.getTitle();
                    }
                    try {
                        String last = fname.substring(fname.length() - 1);
                        ErrorMessage.E("SAMPLE_FILE" + last);
                        if (last.equals(".")) {
                            fname = fname.substring(0, fname.lastIndexOf(".")) + "";
                            ErrorMessage.E("SAMPLE_FILE if>>" + fname);
                            fname = fname + ".jpg";

                        } else {
                            ErrorMessage.E("SAMPLE_FILE" + fname);
                            fname = fname + ".jpg";
                        }


                        new GetImages(Const.BASEURL.URL + Const.ENDPOINT.Comment + example.getId() + "/getasset", fname, LatestFeedDetailActivity.this).execute();
                        // new DownloadImage().execute(Const.BASEURL.URL+Const.ENDPOINT.Comment +  example.getId() + "/getasset");
                       /* Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Const.BASEURL.URL+Const.ENDPOINT.Comment +  example.getId() + "/getasset" ));
                        startActivity(myIntent);*/
                        //  DownloadImageFromPath(Const.BASEURL.URL+Const.ENDPOINT.Comment +  example.getId() + "/getasset" );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                }

                break;
            case R.id.menu_btn:
                try {
                    // showPopup_menu(view);
                    if (PublisherAlreadyFollow.equals("")) {
                        PublisherfollowOnServer();
                    } else {
                        PublisherUnfollowOnServer();
                    }
                } catch (Exception e) {
                }
                break;
            case R.id.profile:
                Bundle bundle = new Bundle();
                bundle.putString("Calling", "bypublisher");
                bundle.putString("Id", example.getUser().getId());
                bundle.putString("Name", example.getUser().getDisplayName());
                ErrorMessage.I(LatestFeedDetailActivity.this, DashBoardActivity.class, bundle);
                break;
        }
    }

    private void sendMessage() {
        if (fragmentTitle.equals("Listview")) {
            intent = new Intent("Listview");
        } else {
            intent = new Intent("Standerd");
        }
        intent.putExtra("message", example.getId());
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void followOnServer() {
        if (NetworkUtil.isNetworkAvailable(LatestFeedDetailActivity.this)) {
            Call<ResponseBody> call = ApiClient.getLoadInterface().FollowNumerouns(Const.ENDPOINT.FollowCollections + "collection/" + example.getId() + "/" + SavedData.getTokan());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    ErrorMessage.E("followOnServer response" + response.code());
                    if (response.isSuccessful()) {
                        try {
                            ErrorMessage.E("error code" + response.body().string());
                            if (response.code() == 200) {
                                followBynImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_follow_green));
                                Track_Event("Follow", "Follow Collection", example.getId(), "numerical_event");

                                AlreadyFollow = "Yes";
                            } else {
                            }
                        } catch (Exception e) {

                        }
                    } else {
                        ErrorMessage.T(LatestFeedDetailActivity.this, "Response not successful");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(LatestFeedDetailActivity.this, "Response Fail");
                    System.out.println("============update profile fail  :" + t.toString());

                }
            });

        } else {
            ErrorMessage.T(LatestFeedDetailActivity.this, LatestFeedDetailActivity.this.getString(R.string.no_internet));
        }

    }

    private void UnfollowOnServer() {
        if (NetworkUtil.isNetworkAvailable(LatestFeedDetailActivity.this)) {
            Call<ResponseBody> call = ApiClient.getLoadInterface().FollowNumerouns(Const.ENDPOINT.UNFollowCollections + "collection/" + example.getId() + "/" + SavedData.getTokan());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    ErrorMessage.E("followOnServer response" + response.code());
                    if (response.isSuccessful()) {
                        try {
                            ErrorMessage.E("error code" + response.body().string());
                            if (response.code() == 200) {
                                followBynImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_follow_color));
                                Track_Event("Follow", "Unfollow Collection", example.getId(), "numerical_event");

                                AlreadyFollow = "";
                            } else {
                            }
                        } catch (Exception e) {

                        }
                    } else {
                        ErrorMessage.T(LatestFeedDetailActivity.this, "Response not successful");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(LatestFeedDetailActivity.this, "Response Fail");
                    System.out.println("============update profile fail  :" + t.toString());

                }
            });

        } else {
            ErrorMessage.T(LatestFeedDetailActivity.this, LatestFeedDetailActivity.this.getString(R.string.no_internet));
        }

    }

    private void getfollowOnServer(String Collection_id) {
        try {
            ErrorMessage.E("getfollowOnServer" + Const.ENDPOINT.Check_FollowCollections + Collection_id + "/0/" + SavedData.getTokan());
            if (NetworkUtil.isNetworkAvailable(LatestFeedDetailActivity.this)) {  ///apis/follow/:itemID/:userID?/:fcmtoken?'
                Call<ResponseBody> call = ApiClient.getLoadInterface().latest(Const.ENDPOINT.Check_FollowCollections + Collection_id + "/0/" + SavedData.getTokan());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        ErrorMessage.E("getfollowOnServererror code" + response.code());
                        if (response.isSuccessful()) {
                            try {
                                if (response.code() == 200) {
                                    Gson gson = new Gson();
                                    JSONArray jsonArray = new JSONArray(response.body().string());
                                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                                    ErrorMessage.E("getfollowOnServer" + jsonObject.toString());
                                    com.numerical.numerical.Models.GetFollow_Models.Example example = gson.fromJson(jsonObject.toString(), com.numerical.numerical.Models.GetFollow_Models.Example.class);
                                    if (example.getUsers().size() > 0) {
                                        followBynImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_follow_green));
                                        AlreadyFollow = "Yes";
                                    }
                                } else {
                                }
                            } catch (Exception e) {

                            }
                        } else {
                            // ErrorMessage.T(LatestFeedDetailActivity.this, "Response not successful");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        ErrorMessage.T(LatestFeedDetailActivity.this, "Response Fail");
                        System.out.println("============update profile fail  :" + t.toString());

                    }
                });

            } else {
                ErrorMessage.T(LatestFeedDetailActivity.this, LatestFeedDetailActivity.this.getString(R.string.no_internet));
            }
        } catch (Exception e) {
        }

    }

    private void showPopup(View v) {
        PopupMenu popup = new PopupMenu(LatestFeedDetailActivity.this, v);
        for (int i = 0; i < example.getActions().size(); i++) {
            popup.getMenu().add(Menu.NONE, i, i, example.getActions().get(i).getName());
        }

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int i = item.getItemId();
                if (i == 0) {
                    try {
                        if (example.getActions().get(0).getActionInternal().equals(true)) {
                            if (example.getActions().get(0).getIdentifier().equals("CallNow")) {
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse("tel:" + example.getActions().get(0).getUrl()));
                                startActivity(intent);
                            } else if (example.getActions().get(0).getIdentifier().equals("SendMessage")) {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                Uri data = Uri.parse("mailto:" + example.getActions().get(0).getUrl() + "?subject=" + example.getActions().get(0).getName());
                                intent.setData(data);
                                startActivity(intent);
                            }
                        } else if (example.getActions().get(0).getActionInternal().equals(false)) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                            browserIntent.setData(Uri.parse(example.getActions().get(0).getUrl()));
                            startActivity(browserIntent);
                        }
                    } catch (Exception e) {
                    }
                    return true;
                } else if (i == 1) {
                    try {
                        if (example.getActions().get(1).getActionInternal().equals(true)) {
                            if (example.getActions().get(1).getIdentifier().equals("CallNow")) {
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse("tel:" + example.getActions().get(1).getUrl()));
                                startActivity(intent);
                            } else if (example.getActions().get(1).getIdentifier().equals("SendMessage")) {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                Uri data = Uri.parse("mailto:" + example.getActions().get(1).getUrl() + "?subject=" + example.getActions().get(1).getName());
                                intent.setData(data);
                                startActivity(intent);
                            }
                        } else if (example.getActions().get(1).getActionInternal().equals(false)) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                            browserIntent.setData(Uri.parse(example.getActions().get(1).getUrl()));
                            startActivity(browserIntent);
                        }
                    } catch (Exception e) {
                        //e.toString();
                    }
                    return true;
                } else if (i == 2) {
                    try {
                        if (example.getActions().get(2).getActionInternal().equals(true)) {
                            if (example.getActions().get(2).getIdentifier().equals("CallNow")) {
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse("tel:" + example.getActions().get(2).getUrl()));
                                startActivity(intent);
                            } else if (example.getActions().get(2).getIdentifier().equals("SendMessage")) {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                Uri data = Uri.parse("mailto:" + example.getActions().get(2).getUrl() + "?subject=" + example.getActions().get(2).getName());
                                intent.setData(data);
                                startActivity(intent);
                            }
                        } else if (example.getActions().get(2).getActionInternal().equals(false)) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                            browserIntent.setData(Uri.parse(example.getActions().get(2).getUrl()));
                            startActivity(browserIntent);
                        }
                    } catch (Exception e) {
                    }
                } else if (i == 3) {
                    try {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                        browserIntent.setData(Uri.parse(example.getActions().get(3).getUrl()));
                        startActivity(browserIntent);
                    } catch (Exception e) {
                    }
                } else if (i == 4) {
                    try {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                        browserIntent.setData(Uri.parse(example.getActions().get(4).getUrl()));
                        startActivity(browserIntent);
                    } catch (Exception e) {
                    }
                } else if (i == 5) {
                    try {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                        browserIntent.setData(Uri.parse(example.getActions().get(5).getUrl()));
                        startActivity(browserIntent);
                    } catch (Exception e) {
                    }
                }

                return false;
            }
        });
        popup.show();
    }

    public void getCalllink(String url) {
        String[] bits = url.split("/");
        String lastOne = bits[bits.length - 1];
        Track_Event("Source Link", "Click", url, "numerical_event");
        GetLatestFeed(lastOne);
    }

    private void GetLatestFeed(String url) {
        if (NetworkUtil.isNetworkAvailable(LatestFeedDetailActivity.this)) {
            Call<ResponseBody> call = null;
            final Dialog materialDialog = ErrorMessage.initProgressDialog(LatestFeedDetailActivity.this);
            call = ApiClient.getLoadInterface().latest("https://numerical.co.in/apis/numerons/" + url);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            materialDialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            Gson gson = new Gson();
                            String responseData = jsonObject.toString();
                            example = gson.fromJson(responseData, Example.class);

                            try {
                                tilteTv.setText(example.getTitle());
                                authoreTv.setText(example.getUser().getDisplayName());
                                if (getIntent().getStringExtra("Name").equals("")) {
                                    if (example.getIsCollection()) {
                                        tilteTv.setVisibility(View.VISIBLE);
                                        setToolbarWithBackButton("Collection");
                                    } else {
                                        tilteTv.setVisibility(View.GONE);
                                        setToolbarWithBackButton("Numeron");
                                    }
                                } else {
                                    setToolbarWithBackButton(getIntent().getStringExtra("Name"));
                                    if (example.getIsCollection()) {
                                        tilteTv.setVisibility(View.VISIBLE);
                                    } else {
                                        tilteTv.setVisibility(View.GONE);
                                    }
                                }
                                SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy - hh:mm a");
                                Date d = null;
                                try {
                                    input.setTimeZone(TimeZone.getTimeZone("IMP"));
                                    d = input.parse(example.getLastModified());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                String formatted = output.format(d);
                                dateTv.setText(formatted);
                                ListingViewFragment presonalDetailsFragment1 = new ListingViewFragment();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("ItemData", example);
                                presonalDetailsFragment1.setArguments(bundle);
                                mFragmentTransaction = detailsFragment1.beginTransaction();
                                mFragmentTransaction.replace(R.id.fragmentsLayout, presonalDetailsFragment1);
                                mFragmentTransaction.commit();

                            } catch (Exception e) {
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            ErrorMessage.E("JSONException" + e.toString());
                            ErrorMessage.T(LatestFeedDetailActivity.this, "Server Error");
                            materialDialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                            ErrorMessage.E("Exception" + e.toString());
                            ErrorMessage.T(LatestFeedDetailActivity.this, "Server Error");
                            materialDialog.dismiss();
                        }


                    } else {
                        ErrorMessage.T(LatestFeedDetailActivity.this, "Response not successful");
                        materialDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(LatestFeedDetailActivity.this, "Response Fail");
                    System.out.println("============update profile fail  :" + t.toString());
                    materialDialog.dismiss();
                }
            });

        } else {
            ErrorMessage.T(LatestFeedDetailActivity.this, this.getString(R.string.no_internet));

        }

    }

    @OnClick(R.id.back_btn_tv)
    public void onClick() {
    }

    public void CallCommentScreen(String numeron_id) {
        ArrayList<String> stringArrayList = new ArrayList<>();
        stringArrayList.add("");
        stringArrayList.add("");
        stringArrayList.add("");
        stringArrayList.add("");
        stringArrayList.add("");
        stringArrayList.add("");
        stringArrayList.add("");
        stringArrayList.add("");
        stringArrayList.add("");
        stringArrayList.add("");
        stringArrayList.add("");
        stringArrayList.add("");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(LatestFeedDetailActivity.this);
        CommentAdapter side_rv_adapter = new CommentAdapter(LatestFeedDetailActivity.this, stringArrayList);
        commentRv.setLayoutManager(linearLayoutManager);
        commentRv.setItemAnimator(new DefaultItemAnimator());
        commentRv.setNestedScrollingEnabled(false);
        commentRv.setAdapter(side_rv_adapter);
        side_rv_adapter.notifyDataSetChanged();

        commentRelativelayout.setVisibility(View.VISIBLE);
        Animation animSlideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        commentRelativelayout.startAnimation(animSlideUp);
        bottomCardview.setVisibility(View.GONE);
        GetCommentsOnServer(numeron_id);
    }

    private void GetCommentsOnServer(String numeron_id) {
        if (NetworkUtil.isNetworkAvailable(LatestFeedDetailActivity.this)) {
            ErrorMessage.E("GetCommentsOnServer request" + Const.ENDPOINT.Comment + example.getId() + "/numeron/" + numeron_id + "/comment");
            Call<ResponseBody> call = ApiClient.getLoadInterface().Comments(Const.ENDPOINT.Comment + example.getId() + "/numeron/" + numeron_id + "/comment");
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    ErrorMessage.E("followOnServer response" + response.code());
                    if (response.isSuccessful()) {
                        try {
                            ErrorMessage.E("error code" + response.body().string());
                            if (response.code() == 200) {

                            } else {
                            }
                        } catch (Exception e) {

                        }
                    } else {
                        //ErrorMessage.T(LatestFeedDetailActivity.this, "Response not successful");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(LatestFeedDetailActivity.this, "Response Fail");
                    System.out.println("============update profile fail  :" + t.toString());

                }
            });

        } else {
            ErrorMessage.T(LatestFeedDetailActivity.this, LatestFeedDetailActivity.this.getString(R.string.no_internet));
        }

    }

    public void getLikeButton() {
        likeImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_like_fill));
    }

    /*  private void SubmitCommentsOnServer(String numeron_id) {
          if (NetworkUtil.isNetworkAvailable(LatestFeedDetailActivity.this)) {
              ErrorMessage.E("GetCommentsOnServer request" + Const.ENDPOINT.Comment + example.getId() + "/numeron/" + numeron_id + "/comment");
              final JsonObject jsonObject = new JsonObject();
              jsonObject.addProperty("comment", writeCommentEtv.getText().toString());
              Call<ResponseBody> call = ApiClient.getLoadInterface().SubmitComment(jsonObject);
              call.enqueue(new Callback<ResponseBody>() {
                  @Override
                  public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                      ErrorMessage.E("followOnServer response" + response.code());
                      if (response.isSuccessful()) {
                          try {
                              ErrorMessage.E("error code" + response.body().string());
                              if (response.code() == 200) {

                              } else {
                              }
                          } catch (Exception e) {

                          }
                      } else {
                          ErrorMessage.T(LatestFeedDetailActivity.this, "Response not successful");
                      }
                  }

                  @Override
                  public void onFailure(Call<ResponseBody> call, Throwable t) {
                      ErrorMessage.T(LatestFeedDetailActivity.this, "Response Fail");
                      System.out.println("============update profile fail  :" + t.toString());

                  }
              });

          } else {
              ErrorMessage.T(LatestFeedDetailActivity.this, LatestFeedDetailActivity.this.getString(R.string.no_internet));
          }

      }*/
    private void FeedDetail(String collection_id) {
        if (NetworkUtil.isNetworkAvailable(LatestFeedDetailActivity.this)) {
            Call<ResponseBody> call = null;
            final Dialog materialDialog = ErrorMessage.initProgressDialog(LatestFeedDetailActivity.this);
            call = ApiClient.getLoadInterface().latest(Const.ENDPOINT.Feed_Detail + collection_id);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            materialDialog.dismiss();
                            Gson gson = new Gson();
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            ErrorMessage.E("FeedDetail" + jsonObject.toString());
                            String responseData = jsonObject.toString();
                            example = gson.fromJson(responseData, Example.class);
                            tilteTv.setText(example.getTitle());
                            authoreTv.setText(example.getUser().getDisplayName());
                            if (example.getIsCollection()) {
                                FirebaseAnalytics(collection_id, example.getTitle(), "View Collection");
                            } else {
                                FirebaseAnalytics(collection_id, example.getTitle(), "View Numeron");
                            }
                            if (example.getIsCollection()) {
                                tilteTv.setVisibility(View.VISIBLE);
                                setToolbarWithBackButton("Collection");
                            } else {
                                tilteTv.setVisibility(View.GONE);
                                setToolbarWithBackButton("Numeron");
                            }
                            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                            SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy - hh:mm a");
                            Date d = null;
                            try {
                                input.setTimeZone(TimeZone.getTimeZone("IMP"));
                                d = input.parse(example.getLastModified());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                if (example.getLikedAnon().size() > 0) {
                                    while (ee.hasMoreElements()) {
                                        NetworkInterface n = (NetworkInterface) ee.nextElement();
                                        Enumeration ee = n.getInetAddresses();
                                        while (ee.hasMoreElements()) {
                                            InetAddress ip = (InetAddress) ee.nextElement();
                                            ErrorMessage.E("getLocalIpAddress_third" + ip.getHostAddress());
                                            for (int i = 0; i < example.getLikedAnon().size(); i++) {
                                                ErrorMessage.E("Like" + example.getLikedAnon().get(i).getUserIP());
                                                if (("::ffff:" + ip.getHostAddress()).contains(example.getLikedAnon().get(i).getUserIP())) {
                                                    ErrorMessage.E("getLocalIpAddress_third inner" + ip.getHostAddress() + ">>" + example.getLikedAnon().get(i).getUserIP());
                                                    likeImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_like_fill));
                                                } else if ((ip.getHostAddress()).contains(example.getLikedAnon().get(i).getUserIP())) {
                                                    ErrorMessage.E("getLocalIpAddress_third inner" + ip.getHostAddress() + ">>" + example.getLikedAnon().get(i).getUserIP());
                                                    likeImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_like_fill));
                                                }
                                            }
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                ErrorMessage.E("Exception likeAnone" + e.toString());
                            }
                                String formatted = output.format(d);
                                dateTv.setText(formatted);
                                ErrorMessage.E("ListingViewFragment" + example.getNumerons().size());
                                if (example.getType().equals("List")) {
                                    fragmentTitle = "Listview";
                                    ListingViewFragment presonalDetailsFragment1 = new ListingViewFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("ItemData", example);
                                    presonalDetailsFragment1.setArguments(bundle);
                                    mFragmentTransaction = detailsFragment1.beginTransaction();
                                    mFragmentTransaction.replace(R.id.fragmentsLayout, presonalDetailsFragment1);
                                    mFragmentTransaction.commit();

                                } else if (example.getType().equals("0")) {
                                    fragmentTitle = "Listview";
                                    ListingViewFragment presonalDetailsFragment1 = new ListingViewFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("ItemData", example);
                                    presonalDetailsFragment1.setArguments(bundle);
                                    mFragmentTransaction = detailsFragment1.beginTransaction();
                                    mFragmentTransaction.replace(R.id.fragmentsLayout, presonalDetailsFragment1);
                                    mFragmentTransaction.commit();

                                } else if (example.getType().equals("Standard")) {
                                    fragmentTitle = "Standerd";
                                    StandredViewFragment presonalDetailsFragment1 = new StandredViewFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("ItemData", example);
                                    bundle.putString("Publisherfollow", PublisherAlreadyFollow);
                                    presonalDetailsFragment1.setArguments(bundle);
                                    mFragmentTransaction = detailsFragment1.beginTransaction();
                                    mFragmentTransaction.replace(R.id.fragmentsLayout, presonalDetailsFragment1);
                                    mFragmentTransaction.commit();
                                    bottomCardview.setVisibility(View.GONE);
                                } else {
                                    fragmentTitle = "Listview";
                                    ListingViewFragment presonalDetailsFragment1 = new ListingViewFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("ItemData", example);
                                    presonalDetailsFragment1.setArguments(bundle);
                                    mFragmentTransaction = detailsFragment1.beginTransaction();
                                    mFragmentTransaction.replace(R.id.fragmentsLayout, presonalDetailsFragment1);
                                    mFragmentTransaction.commit();
                                }

                                getfollowOnServer(collection_id);
                                getPublisherfollowOnServer(collection_id, example.getUser().getId());

                                if (example.getActions().size() > 0) {
                                    actionMenuImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_more_fill));
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                ErrorMessage.E("Exception" + e.toString());
                                try {
                                    ErrorMessage.E("Exception" + e.toString());
                                    ErrorMessage.T(LatestFeedDetailActivity.this, "Server Error");
                                    materialDialog.dismiss();
                                } catch (Exception e1) {
                                    ErrorMessage.E("Exception 1" + e1.toString());
                                }
                            }


                        } else{
                            ErrorMessage.T(LatestFeedDetailActivity.this, "Response not successful");
                            materialDialog.dismiss();
                        }
                    }

                    @Override public void onFailure (Call < ResponseBody > call, Throwable t){
                        ErrorMessage.T(LatestFeedDetailActivity.this, "Response Fail");
                        System.out.println("============update profile fail  :" + t.toString());
                        materialDialog.dismiss();
                    }
                });

            } else{
                ErrorMessage.T(LatestFeedDetailActivity.this, this.getString(R.string.no_internet));

            }

        }

        public String getLocalIpAddress () {
            try {
                for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                    NetworkInterface intf = en.nextElement();
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            String ip = Formatter.formatIpAddress(inetAddress.hashCode());
                            Log.i("", "***** IP=" + ip);

                            return ip;
                        }
                    }
                }
            } catch (SocketException ex) {
                Log.e("", ex.toString());
            }
            return null;
        }

        public void getLocalIpAddress_third () {
            try {
                ee = NetworkInterface.getNetworkInterfaces();
            } catch (Exception ex) {
                Log.e("", ex.toString());
            }

        }

        private void showPopup_menu (View v){
            PopupMenu popup = new PopupMenu(LatestFeedDetailActivity.this, v);
// popup.setOnMenuItemClickListener(this);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.follow_menu, popup.getMenu());
            popup.show();

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.edit_view) {
                        if (PublisherAlreadyFollow.equals("")) {
                            PublisherfollowOnServer();
                        } else {
                            PublisherUnfollowOnServer();
                        }

                    }
                    return false;
                }
            });
        }

        private void PublisherfollowOnServer () {
            if (NetworkUtil.isNetworkAvailable(LatestFeedDetailActivity.this)) {
                Call<ResponseBody> call = ApiClient.getLoadInterface().FollowNumerouns(Const.ENDPOINT.FollowCollections + "publisher/" + example.getUser().getId() + "/" + SavedData.getTokan());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        ErrorMessage.E("PublisherfollowOnServer response" + response.code());
                        if (response.isSuccessful()) {
                            try {
                                ErrorMessage.E("error code" + response.body().string());
                                if (response.code() == 200) {
                                    menuBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_follow_green));
                                    PublisherAlreadyFollow = "Yes";
                                    Track_Event("Follow", "Follow Publisher", example.getUser().getId(), "numerical_event");

                                } else {
                                }
                            } catch (Exception e) {
                            }
                        } else {
                            ErrorMessage.T(LatestFeedDetailActivity.this, "Response not successful");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        ErrorMessage.T(LatestFeedDetailActivity.this, "Response Fail");
                        System.out.println("============update profile fail  :" + t.toString());

                    }
                });

            } else {
                ErrorMessage.T(LatestFeedDetailActivity.this, LatestFeedDetailActivity.this.getString(R.string.no_internet));
            }

        }

        private void PublisherUnfollowOnServer () {
            if (NetworkUtil.isNetworkAvailable(LatestFeedDetailActivity.this)) {
                Call<ResponseBody> call = ApiClient.getLoadInterface().FollowNumerouns(Const.ENDPOINT.UNFollowCollections + "publisher/" + example.getUser().getId() + "/" + SavedData.getTokan());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        ErrorMessage.E("followOnServer response" + response.code());
                        if (response.isSuccessful()) {
                            try {
                                ErrorMessage.E("error code" + response.body().string());
                                if (response.code() == 200) {
                                    menuBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_follow_color));
                                    PublisherAlreadyFollow = "";
                                    Track_Event("Follow", "Unfollow Publisher", example.getUser().getId(), "numerical_event");

                                } else {
                                }
                            } catch (Exception e) {

                            }
                        } else {
                            ErrorMessage.T(LatestFeedDetailActivity.this, "Response not successful");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        ErrorMessage.T(LatestFeedDetailActivity.this, "Response Fail");
                        System.out.println("============update profile fail  :" + t.toString());

                    }
                });

            } else {
                ErrorMessage.T(LatestFeedDetailActivity.this, LatestFeedDetailActivity.this.getString(R.string.no_internet));
            }

        }

        private void getPublisherfollowOnServer (String Collection_id, String Author_id){
            try {
                ErrorMessage.E("getPublisherfollowOnServer" + Const.ENDPOINT.Check_FollowCollections + Author_id + "/0/" + SavedData.getTokan());
                if (NetworkUtil.isNetworkAvailable(LatestFeedDetailActivity.this)) {  ///apis/follow/:itemID/:userID?/:fcmtoken?'
                    Call<ResponseBody> call = ApiClient.getLoadInterface().latest(Const.ENDPOINT.Check_FollowCollections + Author_id + "/0/" + SavedData.getTokan());
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            ErrorMessage.E("getPublisherfollowOnServer code" + response.code());

                            if (response.isSuccessful()) {
                                try {
                                    if (response.code() == 200) {
                                        Gson gson = new Gson();
                                        JSONArray jsonArray = new JSONArray(response.body().string());
                                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                                        ErrorMessage.E("getPublisherfollowOnServer" + jsonObject.toString());
                                        com.numerical.numerical.Models.GetFollow_Models.Example example = gson.fromJson(jsonObject.toString(), com.numerical.numerical.Models.GetFollow_Models.Example.class);
                                        if (example.getUsers().size() > 0) {
                                            menuBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_follow_green));
                                            PublisherAlreadyFollow = "Yes";
                                        }

                                    } else {
                                    }
                                } catch (Exception e) {

                                }
                            } else {
                                // ErrorMessage.T(LatestFeedDetailActivity.this, "Response not successful");
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            ErrorMessage.T(LatestFeedDetailActivity.this, "Response Fail");
                            System.out.println("============update profile fail  :" + t.toString());

                        }
                    });

                } else {
                    ErrorMessage.T(LatestFeedDetailActivity.this, LatestFeedDetailActivity.this.getString(R.string.no_internet));
                }
            } catch (Exception e) {
            }

        }

        public void FirebaseAnalytics (String id, String name, String content_type){
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, content_type);
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
            mFirebaseAnalytics.setCurrentScreen(LatestFeedDetailActivity.this, name, null);
        }

        public void Track_Event (String category, String action, String label, String event_name){
       /* mTracker.send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                .setLabel(label)
                .setValue(1)
                .build());*/
            if (category.equals("Like")) {
                Bundle params = new Bundle();
                if (example.getIsCollection()) {
                    params.putString("Category", category);
                    params.putString("Action", action);
                    params.putString("Label", label);
                    mFirebaseAnalytics.logEvent(event_name, params);
                } else {
                    params.putString("Category", category);
                    params.putString("Action", "Like Numeron");
                    params.putString("Label", example.getId() + "-" + label);
                    mFirebaseAnalytics.logEvent(event_name, params);
                }

            } else {
                Bundle params = new Bundle();
                params.putString("Category", category);
                params.putString("Action", action);
                params.putString("Label", label);
                mFirebaseAnalytics.logEvent(event_name, params);
            }
        }

        public static String getIPAddress ( boolean useIPv4){
            try {
                List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
                for (NetworkInterface intf : interfaces) {
                    List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                    for (InetAddress addr : addrs) {
                        if (!addr.isLoopbackAddress()) {
                            String sAddr = addr.getHostAddress();
                            //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                            boolean isIPv4 = sAddr.indexOf(':') < 0;

                            if (useIPv4) {
                                if (isIPv4) return sAddr;
                            } else {
                                if (!isIPv4) {
                                    int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                    return delim < 0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                                }
                            }
                        }
                    }
                }
            } catch (Exception ignored) {
            } // for now eat exceptions
            return "";
        }

        public void change_Status (String status){
            PublisherAlreadyFollow = status;
            StandredViewFragment presonalDetailsFragment1 = new StandredViewFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("ItemData", example);
            bundle.putString("Publisherfollow", PublisherAlreadyFollow);
            presonalDetailsFragment1.setArguments(bundle);
            mFragmentTransaction = detailsFragment1.beginTransaction();
            mFragmentTransaction.replace(R.id.fragmentsLayout, presonalDetailsFragment1);
            mFragmentTransaction.commit();
            bottomCardview.setVisibility(View.GONE);
        }

        private void takeScreenshot () {
            Date now = new Date();
            DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

            try {
                // image naming and path  to include sd card  appending name you choose for file

                File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Numerical");
                if (!f.isFile()) {
                    if (!(f.isDirectory())) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            try {
                                Files.createDirectory(Paths.get(f.getAbsolutePath()));
                            } catch (IOException e) {
                                e.printStackTrace();

                            }
                        } else {
                            f.mkdir();
                        }
                    }
                }

                // String mPath = Environment.getExternalStorageDirectory().getAbsolutePath().toString() + "/Numerical" + now + ".jpg";

                // create bitmap screen capture
                View v1 = getWindow().getDecorView().getRootView();
                v1.setDrawingCacheEnabled(true);
           /* View v1 = findViewById(R.id.top_relative_layout);;
            v1.setDrawingCacheEnabled(true);
            RelativeLayout z = (RelativeLayout) findViewById(R.id.top_relative_layout);
            int totalHeight = z.getChildAt(0).getHeight();
            int totalWidth = z.getChildAt(0).getWidth();
            v1.layout(0, 0, totalWidth, totalHeight);
            v1.buildDrawingCache(true);*/
                Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
                v1.setDrawingCacheEnabled(false);
                String fname = now + ".jpg";
                File imageFile = new File(f, fname);

                FileOutputStream outputStream = new FileOutputStream(imageFile);
                int quality = 100;
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
                outputStream.flush();
                outputStream.close();

                openScreenshot(imageFile);
            } catch (Exception e) {
                // Several error may come out with file handling or DOM
                e.printStackTrace();
            }
        }

        private void openScreenshot (File imageFile){
            try {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                Uri uri = Uri.fromFile(imageFile);
                intent.setDataAndType(uri, "image/*");
                startActivity(intent);
            } catch (Exception e) {
            }
        }


    }

