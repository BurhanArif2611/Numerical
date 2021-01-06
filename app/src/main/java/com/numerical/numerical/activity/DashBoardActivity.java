package com.numerical.numerical.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.numerical.numerical.Firebase.Config;
import com.numerical.numerical.R;
import com.numerical.numerical.Utility.ApiClient;
import com.numerical.numerical.Utility.Const;
import com.numerical.numerical.Utility.ErrorMessage;
import com.numerical.numerical.Utility.NetworkUtil;
import com.numerical.numerical.Utility.SavedData;
import com.numerical.numerical.adapters.Drawer_topics_Adapter;
import com.numerical.numerical.database.UserProfileHelper;
import com.numerical.numerical.fragments.DashboardFragment;
import com.numerical.numerical.fragments.LikedFragment;
import com.numerical.numerical.fragments.MostViewedFragment;
import com.numerical.numerical.fragments.NewNumeronsFragment;
import com.numerical.numerical.fragments.ProfileFragment;
import com.numerical.numerical.fragments.SearchFragment;
import com.numerical.numerical.fragments.TopisFragment;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashBoardActivity extends AppCompatActivity {

    @BindView(R.id.without_signin_home_tv)
    TextView withoutSigninHomeTv;
    @BindView(R.id.without_signin_helpsupport_tv)
    TextView withoutSigninHelpsupportTv;
    @BindView(R.id.signin_tv)
    TextView signin_tv;
    @BindView(R.id.signup_tv)
    TextView signupTv;
    @BindView(R.id.without_signin_menus_layout)
    LinearLayout withoutSigninMenusLayout;
    @BindView(R.id.home_tv)
    TextView homeTv;
    @BindView(R.id.mynumerical_tv)
    TextView mynumericalTv;
    @BindView(R.id.helpsupport_tv)
    TextView helpsupportTv;
    @BindView(R.id.signout_tv)
    TextView signoutTv;
    @BindView(R.id.with_signin_menus_layout)
    LinearLayout withSigninMenusLayout;
    @BindView(R.id.add_fab)
    FloatingActionButton addFab;
    @BindView(R.id.bottomhome_tv)
    ImageView bottom_homeTv;
    @BindView(R.id.bottom_cardview)
    CardView bottomCardview;
    @BindView(R.id.bottom_search_tv)
    ImageView bottomSearchTv;
    @BindView(R.id.searchView1)
    MaterialSearchBar searchView1;
    @BindView(R.id.notificans_btn)
    ImageButton notificansBtn;
    @BindView(R.id.badge_notification_1)
    TextView badgeNotification1;
    @BindView(R.id.facebook_btn)
    ImageButton facebookBtn;
    @BindView(R.id.twitter_btn)
    ImageButton twitterBtn;
    @BindView(R.id.topics_rcv)
    RecyclerView topicsRcv;
    @BindView(R.id.singin_bottom_view)
    BottomNavigationView singinBottomView;

    private View menuItemView;
    private FragmentTransaction mFragmentTransaction;
    FragmentManager detailsFragment1 = getSupportFragmentManager();
    private View action_settings, latest, most_viewed, topic_art, like_art;
    private String fragmentTitle = "";
    DrawerLayout drawer;
    private String Calling = "";
    private String Id = "";
    private String Name = "";
    String LastCall = "";
    private FirebaseAnalytics mFirebaseAnalytics;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.tab_home:

                    /*ErrorMessage.I_clear(DashboardActivity.this, MainActivity.class, null);*/
                    return true;
                case R.id.tab_category:
                  /*  status = false;
                    vpMain.setCurrentItem(1);
                    temp = temp.replace("-1", "") + "-1";
                    titleTv.setText("Category");
                    ivMenu.setVisibility(View.VISIBLE);
                    Intent intent = new Intent("favroite");
                    LocalBroadcastManager.getInstance(DashboardActivity.this).sendBroadcast(intent);*/
                    return true;
                case R.id.tab_faverite:
                  /*  status = false;
                    vpMain.setCurrentItem(2);
                    temp = temp.replace("-2", "") + "-2";
                    titleTv.setText("Favourite");
                    ivMenu.setVisibility(View.INVISIBLE);
                    Intent intent1 = new Intent("favroite_main");
                    LocalBroadcastManager.getInstance(DashboardActivity.this).sendBroadcast(intent1);*/
                    return true;
                case R.id.tab_order_history:
                  /*  status = false;
                    vpMain.setCurrentItem(3);
                    temp = temp.replace("-3", "") + "-3";
                    titleTv.setText("Order History");
                    ivMenu.setVisibility(View.INVISIBLE);*/

                    return true;

            }
            return false;
        }
    };
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
      /*  GetDeviceipWiFiData();
        GetDeviceipMobileData();*/
        ButterKnife.bind(this);
        singinBottomView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextAppearance(this, R.style.RobotoBoldTextAppearance);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
       /* ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.WHITE);
        }

        @SuppressLint("ResourceType") ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.drawable.ic_menu, R.drawable.ic_menu);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setHomeAsUpIndicator(R.drawable.ic_menu);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        View header = navigationView.getHeaderView(0);

        CircleImageView user_imageView = findViewById(R.id.user_imageView);
        TextView user_name_tv = findViewById(R.id.user_name_tv);
        TextView user_conatct_tv = findViewById(R.id.user_conatct_tv);
        ImageView next_img = findViewById(R.id.next_img);
        LinearLayout without_signin_layout = findViewById(R.id.without_signin_layout);
        LinearLayout signin_linlayout = findViewById(R.id.signin_linlayout);

        if (!SavedData.getAddToCart_Count().equals("0")) {
            badgeNotification1.setVisibility(View.VISIBLE);
            badgeNotification1.setText(SavedData.getAddToCart_Count());
        } else {
            badgeNotification1.setVisibility(View.GONE);
        }

        signoutTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogoutPopUP();
            }
        });
        addFab.setVisibility(View.GONE);


        if (UserProfileHelper.getInstance().getUserProfileModel().size() > 0) {
            signin_linlayout.setVisibility(View.VISIBLE);
            withSigninMenusLayout.setVisibility(View.VISIBLE);
            without_signin_layout.setVisibility(View.GONE);
            withoutSigninMenusLayout.setVisibility(View.GONE);
            addFab.setVisibility(View.VISIBLE);
            user_name_tv.setText(UserProfileHelper.getInstance().getUserProfileModel().get(0).getDisplayName());
            user_conatct_tv.setText(UserProfileHelper.getInstance().getUserProfileModel().get(0).getEmaiiId());
            singinBottomView.setVisibility(View.VISIBLE);
            singinBottomView.getMenu()
                    .findItem(R.id.center_tab)
                    .setVisible(true);
        } else {
            without_signin_layout.setVisibility(View.VISIBLE);
            withoutSigninMenusLayout.setVisibility(View.VISIBLE);
            signin_linlayout.setVisibility(View.GONE);
            withSigninMenusLayout.setVisibility(View.GONE);
            addFab.setVisibility(View.GONE);
            singinBottomView.getMenu()
                    .findItem(R.id.center_tab)
                    .setVisible(false);
        }
        signin_linlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                ProfileFragment presonalDetailsFragment1 = new ProfileFragment();
                mFragmentTransaction = detailsFragment1.beginTransaction();
                mFragmentTransaction.replace(R.id.fragmentsLayout, presonalDetailsFragment1);
                mFragmentTransaction.addToBackStack(null);
                mFragmentTransaction.commit();
            }
        });

        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer = findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            ErrorMessage.E("if is runing");
            if (!bundle.getString("Name").equals("")) {
                Calling = bundle.getString("Calling");
                Id = bundle.getString("Id");
                Name = bundle.getString("Name");
                if (Calling.equals("bypublisher")) {
                    ErrorMessage.E("id" + Id);
                    ByPublisher(Calling, Id, Name);
                } else if (Calling.equals("Tags")) {
                    ByTags(Calling, Id, Name);//5b9e6370cb4b49281e45a9c0
                    FirebaseAnalytics("Select Topic", "Select Topic", "Landing Page");
                }
            } else if (bundle.getString("Name").equals("")) {

                if (!bundle.getString("categorys").equals("")) {
                    ByTags("Tags", bundle.getString("categorys"), bundle.getString("categorys"));
                } else {
                    Topics("Topics", bundle.getString("topic_id"), bundle.getString("Topic_name"));
                }
            }
        } else {
            FirebaseAnalytics("Latest Numeron", "Latest Numerons", "Landing Page");
            DashboardFragment presonalDetailsFragment1 = new DashboardFragment();
            mFragmentTransaction = detailsFragment1.beginTransaction();
            mFragmentTransaction.replace(R.id.fragmentsLayout, presonalDetailsFragment1);
            Bundle bundle1 = new Bundle();
            bundle1.putString("Name", "Latest Numeron");
            bundle1.putString("Id", "");
            bundle1.putString("Calling", "");
            presonalDetailsFragment1.setArguments(bundle1);
            mFragmentTransaction.commitAllowingStateLoss();
        }
        bottomCardview.setVisibility(View.GONE);

       /* int searchSrcTextId = getResources().getIdentifier("android:id/search_src_text", null, null);
        EditText searchEditText = (EditText) searchView1.findViewById(searchSrcTextId);
        searchEditText.setTextColor(getResources().getColor(R.color.primary_text)); // set the text color
        searchEditText.setHintTextColor(getResources().getColor(R.color.secondary_text));
        searchEditText.setFocusable(true);*/
        searchView1.setSpeechMode(true);

        searchView1.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                ErrorMessage.E("onSearchStateChanged ");
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                ErrorMessage.E("onButtonClicked " + searchView1.getText());
                Intent intent = new Intent("Search");
                intent.putExtra("message", searchView1.getText());
                LocalBroadcastManager.getInstance(DashBoardActivity.this).sendBroadcast(intent);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
        notificansBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ErrorMessage.I(DashBoardActivity.this, NotificationActivity.class, null);
            }
        });
        LocalBroadcastManager.getInstance(this).registerReceiver(NewRequest, new IntentFilter(Config.Update));
        facebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/people/Env-Numerical/100010960274540"));
                    startActivity(browserIntent);
                } catch (Exception e) {
                }
            }
        });
        twitterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/numericalenv"));
                    startActivity(browserIntent);
                } catch (Exception e) {
                }
            }
        });
        // GetTopics();
    }


    /*@Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dash_board, menu);
        menuItemView = findViewById(R.id.form_setting);
        action_settings = findViewById(R.id.action_notification);
        latest = findViewById(R.id.latest);
        most_viewed = findViewById(R.id.most_viewed);
        topic_art = findViewById(R.id.topic_art);
        like_art = findViewById(R.id.like_art);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        action_settings = findViewById(R.id.action_notification);
        latest = findViewById(R.id.latest);
        most_viewed = findViewById(R.id.most_viewed);
        topic_art = findViewById(R.id.topic_art);
        like_art = findViewById(R.id.like_art);
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_notification) {
            ErrorMessage.I(DashBoardActivity.this, NotificationActivity.class, null);
            return true;
        }
        if (id == R.id.action_filter) {
            ErrorMessage.I(DashBoardActivity.this, FiterPageActivity.class, null);
            return true;
        } else if (id == R.id.form_setting) {
           /* menuItemView = findViewById(R.id.form_setting);
            showPopup(menuItemView);*/
            return true;
        } else if (id == R.id.most_viewed) {
            FirebaseAnalytics("Most Viewed", "Most Viewed", "Landing Page");
            MostViewedFragment presonalDetailsFragment1 = new MostViewedFragment();
            mFragmentTransaction = detailsFragment1.beginTransaction();
            mFragmentTransaction.replace(R.id.fragmentsLayout, presonalDetailsFragment1);
            mFragmentTransaction.addToBackStack(null);
            mFragmentTransaction.commit();
        } else if (id == R.id.topic_art) {
            FirebaseAnalytics("Select Topic", "Select Topic", "Landing Page");
            TopisFragment presonalDetailsFragment1 = new TopisFragment();
            mFragmentTransaction = detailsFragment1.beginTransaction();
            mFragmentTransaction.replace(R.id.fragmentsLayout, presonalDetailsFragment1);
            mFragmentTransaction.addToBackStack(null);
            mFragmentTransaction.commit();
        } else if (id == R.id.like_art) {
            FirebaseAnalytics("Liked", "Liked", "Landing Page");
            LikedFragment likedFragment = new LikedFragment();
            mFragmentTransaction = detailsFragment1.beginTransaction();
            mFragmentTransaction.replace(R.id.fragmentsLayout, likedFragment);
            mFragmentTransaction.addToBackStack(null);
            mFragmentTransaction.commit();
        } else if (id == R.id.latest) {
            FirebaseAnalytics("Latest Numeron", "Latest Numerons", "Landing Page");
            DashboardFragment presonalDetailsFragment1 = new DashboardFragment();
            mFragmentTransaction = detailsFragment1.beginTransaction();
            mFragmentTransaction.replace(R.id.fragmentsLayout, presonalDetailsFragment1);
            mFragmentTransaction.addToBackStack(null);
            Bundle bundle1 = new Bundle();
            bundle1.putString("Name", "Latest Numeron");
            bundle1.putString("Id", "");
            bundle1.putString("Calling", "");
            presonalDetailsFragment1.setArguments(bundle1);
            mFragmentTransaction.commit();
        }
        return super.onOptionsItemSelected(item);
    }


    public void launchFragmentTitle(String fragemntName) {
        fragmentTitle = fragemntName;
        if (fragemntName.equals("MostNumerons")) {
            getSupportActionBar().setTitle("Numerons");
        } else {
            getSupportActionBar().setTitle(fragemntName);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        invalidateOptionsMenu();
        if (fragmentTitle.equals("MostNumerons")) {
            //   menu.findItem(R.id.action_notification).setVisible(true);
            menu.findItem(R.id.form_setting).setVisible(true).setIcon(getResources().getDrawable(R.drawable.ic_most_viewed));
            menu.findItem(R.id.latest).setVisible(true);
            menu.findItem(R.id.most_viewed).setVisible(false);
            menu.findItem(R.id.topic_art).setVisible(true);
        } else if (fragmentTitle.equals("Topics")) {
            menu.findItem(R.id.form_setting).setVisible(true).setIcon(getResources().getDrawable(R.drawable.ic_topics));
            // menu.findItem(R.id.action_notification).setVisible(true);
            menu.findItem(R.id.latest).setVisible(true);
            menu.findItem(R.id.most_viewed).setVisible(true);
            menu.findItem(R.id.topic_art).setVisible(false);
        } else if (fragmentTitle.equals("Numerons")) {
            // menu.findItem(R.id.action_notification).setVisible(true);
            menu.findItem(R.id.form_setting).setVisible(true);
            menu.findItem(R.id.latest).setVisible(false);
            menu.findItem(R.id.most_viewed).setVisible(true);
            menu.findItem(R.id.topic_art).setVisible(true);
        } else if (fragmentTitle.equals("Profile")) {
            //  menu.findItem(R.id.action_notification).setVisible(true);
            menu.findItem(R.id.form_setting).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    public void LogoutPopUP() {
        final Dialog dialog = new Dialog(DashBoardActivity.this);
        dialog.setContentView(R.layout.logout_popup);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        final Button yes_btn = dialog.findViewById(R.id.yes_btn);
        final Button no_btn = dialog.findViewById(R.id.no_btn);
        yes_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
                UserProfileHelper.getInstance().delete();
                ErrorMessage.I_clear(DashBoardActivity.this, SplashActivity.class, null);

            }
        });
        no_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @SuppressLint("RestrictedApi")
    @OnClick({R.id.without_signin_home_tv, R.id.without_signin_helpsupport_tv, R.id.signin_tv, R.id.signup_tv, R.id.without_signin_menus_layout, R.id.mynumerical_tv, R.id.helpsupport_tv, R.id.signout_tv, R.id.with_signin_menus_layout, R.id.bottomhome_tv, R.id.home_tv, R.id.add_fab, R.id.bottom_search_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.without_signin_home_tv:
                drawer.closeDrawer(GravityCompat.START);
                DashboardFragment presonalDetailsFragment1 = new DashboardFragment();
                mFragmentTransaction = detailsFragment1.beginTransaction();
                mFragmentTransaction.replace(R.id.fragmentsLayout, presonalDetailsFragment1);
                //mFragmentTransaction.addToBackStack(null);
                Bundle bundle1 = new Bundle();
                bundle1.putString("Name", "Latest Numeron");
                bundle1.putString("Id", "");
                bundle1.putString("Calling", "");
                presonalDetailsFragment1.setArguments(bundle1);
                mFragmentTransaction.commit();
                break;
            case R.id.without_signin_helpsupport_tv:
                break;
            case R.id.signin_tv:
                ErrorMessage.I_clear(DashBoardActivity.this, LoginActivity.class, null);
                break;
            case R.id.signup_tv:
                ErrorMessage.I_clear(DashBoardActivity.this, SignUpActivity.class, null);
                break;
            case R.id.without_signin_menus_layout:
                break;
            case R.id.mynumerical_tv:
                break;
            case R.id.helpsupport_tv:
                break;
            case R.id.add_fab:
                bottomCardview.setVisibility(View.GONE);
                addFab.setVisibility(View.GONE);
                NewNumeronsFragment newNumeronsFragment = new NewNumeronsFragment();
                mFragmentTransaction = detailsFragment1.beginTransaction();
                mFragmentTransaction.replace(R.id.fragmentsLayout, newNumeronsFragment);
                mFragmentTransaction.addToBackStack(null);
                mFragmentTransaction.commit();
                break;
            case R.id.signout_tv:
                LogoutPopUP();
                break;
            case R.id.with_signin_menus_layout:
                break;
            case R.id.bottomhome_tv:
                searchView1.setVisibility(View.GONE);
                DashboardFragment dashboardFragment1 = new DashboardFragment();
                mFragmentTransaction = detailsFragment1.beginTransaction();
                mFragmentTransaction.replace(R.id.fragmentsLayout, dashboardFragment1);
                mFragmentTransaction.addToBackStack(null);
                Bundle bundle2 = new Bundle();
                bundle2.putString("Name", "Latest Numeron");
                bundle2.putString("Id", "");
                bundle2.putString("Calling", "");
                dashboardFragment1.setArguments(bundle2);
                mFragmentTransaction.commit();
                break;
            case R.id.home_tv:
                drawer.closeDrawer(GravityCompat.START);
                DashboardFragment dashboardFragment = new DashboardFragment();
                mFragmentTransaction = detailsFragment1.beginTransaction();
                mFragmentTransaction.replace(R.id.fragmentsLayout, dashboardFragment);
                mFragmentTransaction.addToBackStack(null);
                Bundle bundle3 = new Bundle();
                bundle3.putString("Name", "Latest Numeron");
                bundle3.putString("Id", "");
                bundle3.putString("Calling", "");
                dashboardFragment.setArguments(bundle3);
                mFragmentTransaction.commit();
                break;
            case R.id.bottom_search_tv:
                searchView1.setVisibility(View.VISIBLE);
                SearchFragment searchFragment = new SearchFragment();
                mFragmentTransaction = detailsFragment1.beginTransaction();
                mFragmentTransaction.replace(R.id.fragmentsLayout, searchFragment);
                mFragmentTransaction.addToBackStack(null);
                mFragmentTransaction.commit();
                break;

        }
    }

    public void Topics(String Calling, String Topics_Id, String Name) {
        try {
            drawer.closeDrawers();
        } catch (Exception e) {
        }
        FirebaseAnalytics(Topics_Id, "View Numerons By Topic", "Landing Page");
        DashboardFragment presonalDetailsFragment1 = new DashboardFragment();
        FragmentManager detailsFragment1 = getSupportFragmentManager();
        mFragmentTransaction = detailsFragment1.beginTransaction();
        mFragmentTransaction.replace(R.id.fragmentsLayout, presonalDetailsFragment1);
        mFragmentTransaction.addToBackStack(null);
        Bundle bundle = new Bundle();
        bundle.putString("Calling", Calling);
        bundle.putString("Id", Topics_Id);
        bundle.putString("Name", Name);
        presonalDetailsFragment1.setArguments(bundle);
        mFragmentTransaction.commit();
    }

    public void Bycategory(String Calling, String Topics_Id) {
        DashboardFragment presonalDetailsFragment1 = new DashboardFragment();
        FragmentManager detailsFragment1 = getSupportFragmentManager();
        mFragmentTransaction = detailsFragment1.beginTransaction();
        mFragmentTransaction.replace(R.id.fragmentsLayout, presonalDetailsFragment1);
        mFragmentTransaction.addToBackStack(null);
        Bundle bundle = new Bundle();
        bundle.putString("Calling", Calling);
        bundle.putString("Id", Topics_Id);
        bundle.putString("Name", Topics_Id);
        presonalDetailsFragment1.setArguments(bundle);
        mFragmentTransaction.commit();
    }

    public void ByPublisher(String Calling, String Topics_Id, String Name) {
        FirebaseAnalytics(Topics_Id, "View Numerons By Publisher", "Landing Page");
        DashboardFragment presonalDetailsFragment1 = new DashboardFragment();
        FragmentManager detailsFragment1 = getSupportFragmentManager();
        mFragmentTransaction = detailsFragment1.beginTransaction();
        mFragmentTransaction.replace(R.id.fragmentsLayout, presonalDetailsFragment1);
        mFragmentTransaction.addToBackStack(null);
        Bundle bundle = new Bundle();
        bundle.putString("Calling", Calling);
        bundle.putString("Id", Topics_Id);
        bundle.putString("Name", Name);
        presonalDetailsFragment1.setArguments(bundle);
        mFragmentTransaction.commit();
    }

    public void ByTags(String Calling, String Topics_Id, String Name) {
        FirebaseAnalytics(Topics_Id, "View Numerons By Category", "Landing Page");
        DashboardFragment presonalDetailsFragment1 = new DashboardFragment();
        FragmentManager detailsFragment1 = getSupportFragmentManager();
        mFragmentTransaction = detailsFragment1.beginTransaction();
        mFragmentTransaction.replace(R.id.fragmentsLayout, presonalDetailsFragment1);
        mFragmentTransaction.addToBackStack(null);
        Bundle bundle = new Bundle();
        bundle.putString("Calling", Calling);
        bundle.putString("Id", Topics_Id);
        bundle.putString("Name", Name);
        presonalDetailsFragment1.setArguments(bundle);
        mFragmentTransaction.commit();
    }

    /*@Override
    public void finish() {
        if (!LastCall.equals("")) {
            FinishPopUP();
        }
    }
*/
    public void FinishPopUP() {
        final Dialog dialog = new Dialog(DashBoardActivity.this);
        dialog.setContentView(R.layout.confirmation_popup);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        final TextView content_tv = dialog.findViewById(R.id.content_tv);
        final Button cancel_btn = dialog.findViewById(R.id.cancel_btn);
        final Button continue_video = dialog.findViewById(R.id.continue_video);
        content_tv.setText("Are you Sure ! you want to exit app .");
        continue_video.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
                moveTaskToBack(true);
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /*public void GetCurrentPosition(String Status) {
        LastCall = Status;
    }*/
    public void FirebaseAnalytics(String id, String action, String event) {
        ErrorMessage.E("FirebaseAnalytics" + action + ">>" + event);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, action);
        // bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, event);

        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, event);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        mFirebaseAnalytics.setCurrentScreen(DashBoardActivity.this, action, null);

    }

    @Override
    public void onBackPressed() {
        searchView1.setVisibility(View.GONE);
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        if (!SavedData.getAddToCart_Count().equals("0")) {
            badgeNotification1.setVisibility(View.VISIBLE);
            badgeNotification1.setText(SavedData.getAddToCart_Count());
        } else {
            badgeNotification1.setVisibility(View.GONE);
        }
        super.onResume();
    }

    private BroadcastReceiver NewRequest = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (!SavedData.getAddToCart_Count().equals("0")) {
                    badgeNotification1.setVisibility(View.VISIBLE);
                    badgeNotification1.setText(SavedData.getAddToCart_Count());
                } else {
                    badgeNotification1.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                ErrorMessage.E("Exception outer" + e.toString());
            }


        }
    };

    private void GetTopics() {
        if (NetworkUtil.isNetworkAvailable(DashBoardActivity.this)) {
            Call<ResponseBody> call = ApiClient.getLoadInterface().latest(Const.ENDPOINT.Topics);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            JSONArray jsonArray = new JSONArray(response.body().string());
                            //System.out.println("===response data Summery:" + jsonArray.toString());
                            ErrorMessage.E("response" + jsonArray.toString());
                            Drawer_topics_Adapter side_rv_adapter = new Drawer_topics_Adapter(DashBoardActivity.this, jsonArray, 1, "");
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DashBoardActivity.this);
                            topicsRcv.setLayoutManager(linearLayoutManager);
                            topicsRcv.setItemAnimator(new DefaultItemAnimator());
                            topicsRcv.setNestedScrollingEnabled(false);
                            topicsRcv.setAdapter(side_rv_adapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                            ErrorMessage.E("JSONException" + e.toString());
                            ErrorMessage.T(DashBoardActivity.this, "Server Error");
                        } catch (Exception e) {
                            e.printStackTrace();
                            ErrorMessage.E("Exception" + e.toString());
                            ErrorMessage.T(DashBoardActivity.this, "Server Error");
                        }


                    } else {
                        ErrorMessage.T(DashBoardActivity.this, "Response not successful");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(DashBoardActivity.this, "Response Fail");
                    System.out.println("============update profile fail  :" + t.toString());
                }
            });

        } else {
            ErrorMessage.T(DashBoardActivity.this, this.getString(R.string.no_internet));

        }

    }

    public String GetDeviceipMobileData() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
                 en.hasMoreElements(); ) {
                NetworkInterface networkinterface = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = networkinterface.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        Log.e("GetDeviceipMobileData>>", inetAddress.getHostAddress());
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (Exception ex) {
            Log.e("Current IP", ex.toString());
        }
        return null;
    }

    public String GetDeviceipWiFiData() {

        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);

        @SuppressWarnings("deprecation")

        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        Log.e("GetDeviceipWiFiData>>", ip);
        return ip;

    }
}
