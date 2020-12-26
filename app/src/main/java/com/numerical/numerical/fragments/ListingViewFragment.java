package com.numerical.numerical.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.numerical.numerical.Models.LatestFeed.Example;
import com.numerical.numerical.R;
import com.numerical.numerical.Utility.ApiClient;
import com.numerical.numerical.Utility.Const;
import com.numerical.numerical.Utility.ErrorMessage;
import com.numerical.numerical.Utility.NetworkUtil;
import com.numerical.numerical.Utility.SavedData;
import com.numerical.numerical.activity.LatestFeedDetailActivity;
import com.numerical.numerical.adapters.Latest_numeruns_Adapter;
import com.numerical.numerical.adapters.Tags_Adapter;
import com.numerical.numerical.database.UserProfileHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE;
import static com.numerical.numerical.activity.LatestFeedDetailActivity.PublisherAlreadyFollow;


public class ListingViewFragment extends Fragment {
    View view;
    @BindView(R.id.item_img)
    ImageView itemImg;
    @BindView(R.id.date_tv)
    TextView dateTv;
    @BindView(R.id.likecount_tv)
    TextView likecountTv;
    @BindView(R.id.view_tv)
    TextView viewTv;
    @BindView(R.id.comment_tv)
    TextView commentTv;
    @BindView(R.id.list_describtion_rv)
    RecyclerView listDescribtionRv;
    @BindView(R.id.tags_rv)
    RecyclerView tagsRv;
    @BindView(R.id.source_tv)
    TextView sourceTv;
    @BindView(R.id.last_modified_date_tv)
    TextView last_modified_date_tv;
    TextView placeofimage_tv, describtion_tv;
    Unbinder unbinder;
    @BindView(R.id.proffileImage)
    CircleImageView proffileImage;
    @BindView(R.id.authore_tv)
    TextView authoreTv;
    @BindView(R.id.menu_btn)
    ImageButton menuBtn;
    @BindView(R.id.second_date_tv)
    TextView secondDateTv;

    private Example example;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_listing_view, container, false);
        unbinder = ButterKnife.bind(this, view);
        itemImg = (ImageView) view.findViewById(R.id.item_img);
        dateTv = (TextView) view.findViewById(R.id.date_tv);
        likecountTv = (TextView) view.findViewById(R.id.likecount_tv);
        viewTv = (TextView) view.findViewById(R.id.view_tv);
        commentTv = (TextView) view.findViewById(R.id.comment_tv);
        sourceTv = (TextView) view.findViewById(R.id.source_tv);
        placeofimage_tv = (TextView) view.findViewById(R.id.placeofimage_tv);
        describtion_tv = (TextView) view.findViewById(R.id.describtion_tv);
        listDescribtionRv = (RecyclerView) view.findViewById(R.id.list_describtion_rv);
        tagsRv = (RecyclerView) view.findViewById(R.id.tags_rv);

        ((LatestFeedDetailActivity) getActivity()).launchFragmentTitle("Listview");
        if (UserProfileHelper.getInstance().getUserProfileModel().size() > 0) {
            commentTv.setVisibility(View.VISIBLE);
        } else {
            commentTv.setVisibility(View.GONE);
        }
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            try {
                example = (Example) getArguments().getSerializable("ItemData");
                authoreTv.setText(example.getUser().getDisplayName());
                getPublisherfollowOnServer(example.getUser().getId());
               try {
                   if (example.getViews() == null || example.getViews().equals("null")) {
                       viewTv.setText("0");
                   } else {
                       viewTv.setText("" + example.getViews());
                   }
               }catch (Exception e){}

                sourceTv.setText(example.getSource());
                if (example.getLikes() == null) {
                    likecountTv.setText("0");
                } else {
                    likecountTv.setText("" + example.getLikes());
                }
                if (example.getIsCollection()) {
                    secondDateTv.setVisibility(View.GONE);
                    last_modified_date_tv.setVisibility(View.VISIBLE);
                    dateTv.setVisibility(View.GONE);
                    placeofimage_tv.setVisibility(View.GONE);
                    describtion_tv.setVisibility(View.GONE);
                    Glide.with(getActivity()).load(example.getAssetPath()).into(itemImg);
                } else {
                    secondDateTv.setVisibility(View.VISIBLE);
                    last_modified_date_tv.setVisibility(View.GONE);
                    dateTv.setVisibility(View.GONE);
                    sourceTv.setText(example.getNumerons().get(0).getSource());
                    itemImg.setVisibility(View.GONE);
                    placeofimage_tv.setVisibility(View.VISIBLE);
                    describtion_tv.setVisibility(View.VISIBLE);
                    placeofimage_tv.setText(example.getNumerons().get(0).getNumeral());

                    SpannableStringBuilder builder = new SpannableStringBuilder();

                    SpannableString str1 = new SpannableString(example.getNumerons().get(0).getNumeral());
                    str1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.primary_text)), 0, str1.length(), 0);
                    str1.setSpan(new AbsoluteSizeSpan(getResources().getDimensionPixelSize(R.dimen.text_size_20)), 0, str1.length(), SPAN_INCLUSIVE_INCLUSIVE); // set size
                    str1.setSpan(new StyleSpan(Typeface.BOLD), 0, str1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    builder.append(str1);

                    SpannableString str2 = new SpannableString(" " + example.getNumerons().get(0).getTitle());
                   /* Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "roboto_regular.ttf");
                    str2.setSpan (new CustomTypefaceSpan(font), 0, str2.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);*/
                    str2.setSpan(new AbsoluteSizeSpan(getResources().getDimensionPixelSize(R.dimen.text_size_16)), 0, str2.length(), SPAN_INCLUSIVE_INCLUSIVE); // set size
                    str2.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.primary_text)), 0, str2.length(), 0);
                    builder.append(str2);
                    //describtion_tv.setText(builder, TextView.BufferType.SPANNABLE);
                    describtion_tv.setText(example.getNumerons().get(0).getTitle());

                    ErrorMessage.E("ListView" + example.getNumerons().get(0).getSubType());
                    if (example.getNumerons().get(0).getSubType().equals("panel-angry")) {
                        placeofimage_tv.setBackgroundColor(getResources().getColor(R.color.panel_angry));
                    } else if (example.getNumerons().get(0).getSubType().equals("panel-formal")) {
                        placeofimage_tv.setBackgroundColor(getResources().getColor(R.color.panel_formal));
                    } else if (example.getNumerons().get(0).getSubType().equals("panel-sad")) {
                        placeofimage_tv.setBackgroundColor(getResources().getColor(R.color.panel_sad));
                    } else if (example.getNumerons().get(0).getSubType().equals("panel-calm")) {
                        placeofimage_tv.setBackgroundColor(getResources().getColor(R.color.panel_calm));
                    } else if (example.getNumerons().get(0).getSubType().equals("panel-positive")) {
                        placeofimage_tv.setBackgroundColor(getResources().getColor(R.color.panel_positive));
                    } else if (example.getNumerons().get(0).getSubType().equals("panel-royal")) {
                        placeofimage_tv.setBackgroundColor(getResources().getColor(R.color.panel_royal));
                    } else if (example.getNumerons().get(0).getSubType().equals("panel-happy")) {
                        placeofimage_tv.setBackgroundColor(getResources().getColor(R.color.panel_happy));
                    } else if (example.getNumerons().get(0).getSubType().equals("Standard-IMAGE")) {
                        itemImg.setVisibility(View.VISIBLE);
                        placeofimage_tv.setVisibility(View.GONE);
                        describtion_tv.setVisibility(View.VISIBLE);
                        placeofimage_tv.setText(example.getNumerons().get(0).getNumeral());
                        describtion_tv.setText(builder, TextView.BufferType.SPANNABLE);
                        //describtion_tv.setText(example.getNumerons().get(0).getTitle());
                        if (example.getNumerons().get(0).getAssetPath().contains("https://")) {
                            Glide.with(getActivity()).load(example.getNumerons().get(0).getAssetPath()).into(itemImg);
                        } else if (example.getNumerons().get(0).getAssetPath().contains("http://")) {
                            Glide.with(getActivity()).load(example.getNumerons().get(0).getAssetPath()).into(itemImg);
                        } else {
                            Glide.with(getActivity()).load("https://numerical.co.in" + example.getNumerons().get(0).getAssetPath()).into(itemImg);
                        }

                    } else if (example.getNumerons().get(0).getSubType().equals("IMAGE")) {
                        itemImg.setVisibility(View.VISIBLE);
                        placeofimage_tv.setVisibility(View.GONE);
                        describtion_tv.setVisibility(View.VISIBLE);
                        placeofimage_tv.setText(example.getNumerons().get(0).getNumeral());
                        //  describtion_tv.setText(builder, TextView.BufferType.SPANNABLE);
                        describtion_tv.setText(example.getNumerons().get(0).getTitle());
                        if (example.getNumerons().get(0).getAssetPath().contains("https://")) {
                            Glide.with(getActivity()).load(example.getNumerons().get(0).getAssetPath()).into(itemImg);
                        } else if (example.getNumerons().get(0).getAssetPath().contains("http://")) {
                            Glide.with(getActivity()).load(example.getNumerons().get(0).getAssetPath()).into(itemImg);
                        } else {
                            Glide.with(getActivity()).load("https://numerical.co.in" + example.getNumerons().get(0).getAssetPath()).into(itemImg);
                        }
                    }
                    listDescribtionRv.setVisibility(View.GONE);
                }
                ErrorMessage.E("Size" + example.getNumerons().size());
                Latest_numeruns_Adapter side_rv_adapter = new Latest_numeruns_Adapter(getActivity(), example.getNumerons());
                LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getActivity());
                listDescribtionRv.setLayoutManager(gridLayoutManager);
                listDescribtionRv.setItemAnimator(new DefaultItemAnimator());
                listDescribtionRv.setNestedScrollingEnabled(false);
                listDescribtionRv.setAdapter(side_rv_adapter);
                listDescribtionRv.setHasFixedSize(false);
                listDescribtionRv.smoothScrollToPosition(1);

                Tags_Adapter tags_adapter = new Tags_Adapter(getActivity(), example.getCategories());
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                tagsRv.setLayoutManager(linearLayoutManager);
                tagsRv.setItemAnimator(new DefaultItemAnimator());
                tagsRv.setAdapter(tags_adapter);
                tags_adapter.notifyDataSetChanged();
                tagsRv.smoothScrollToPosition(1);

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
                dateTv.setText("Last Modified: " + formatted);
                last_modified_date_tv.setText("Last Modified: " + formatted);
                secondDateTv.setText("Last Modified: " + formatted);
            } catch (Exception e) {
                ErrorMessage.E("ListingFragment Exception>>" + e.toString());
            }

        }
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver, new IntentFilter("Listview"));

        commentTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //((LatestFeedDetailActivity)getActivity()).CallCommentScreen(example.getNumerons().get(0).getId());
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri data = Uri.parse("mailto:" + "" + "?subject=" + example.getTitle());
                    intent.setData(data);
                    startActivity(intent);
                } catch (Exception e) {
                }
            }
        });
        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PublisherAlreadyFollow.equals("")) {
                    PublisherfollowOnServer();
                } else {
                    PublisherUnfollowOnServer();
                }
            }
        });
        return view;

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick({R.id.source_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.source_tv:
                try {
                    if (example.getUrl() != null && !example.getUrl().equals("")) {
                        ((LatestFeedDetailActivity) getActivity()).Track_Event("Source Link", "Click", example.getUrl(), "numerical_event");
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                        browserIntent.setData(Uri.parse(example.getUrl()));
                        startActivity(browserIntent);
                    } else {
                        ErrorMessage.T(getActivity(), "URL not available !");
                    }
                } catch (Exception e) {
                }
                break;
        }
    }

    private void LikeOnServer(String id) {
        try {
            if (NetworkUtil.isNetworkAvailable(getActivity())) {
                ErrorMessage.E("LikeOnServer" + example.getId());
                Call<ResponseBody> call = ApiClient.getLoadInterface().LikeNumerouns(Const.ENDPOINT.LikeNumerouns + id + "/like");
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
                                    ((LatestFeedDetailActivity) getActivity()).getLikeButton();
                                    ((LatestFeedDetailActivity) getActivity()).Track_Event("Like", "Like Collection", id, "numerical_event");

                                } else {
                                }
                            } catch (Exception e) {

                            }
                        } else {
                            try {
                                ErrorMessage.T(getActivity(), "Response not successful");
                            } catch (Exception w) {
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        System.out.println("============update profile fail  :" + t.toString());

                    }
                });

            } else {
                ErrorMessage.T(getActivity(), getActivity().getString(R.string.no_internet));
            }
        } catch (Exception e) {
        }
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            Log.e("receiverOnFragment", "Got message: " + message);
            LikeOnServer(message);

        }
    };

    private void PublisherfollowOnServer() {
        if (NetworkUtil.isNetworkAvailable(getActivity())) {
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
                                ((LatestFeedDetailActivity) getActivity()).Track_Event("Follow", "Follow Publisher", example.getUser().getId(), "numerical_event");
                                PublisherAlreadyFollow = "Yes";
                            } else {
                            }
                        } catch (Exception e) {
                        }
                    } else {
                        ErrorMessage.T(getActivity(), "Response not successful");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(getActivity(), "Response Fail");
                    System.out.println("============update profile fail  :" + t.toString());

                }
            });

        } else {
            ErrorMessage.T(getActivity(), getActivity().getString(R.string.no_internet));
        }

    }

    private void PublisherUnfollowOnServer() {
        if (NetworkUtil.isNetworkAvailable(getActivity())) {
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
                                ((LatestFeedDetailActivity) getActivity()).Track_Event("Follow", "Unfollow Publisher", example.getUser().getId(), "numerical_event");
                                PublisherAlreadyFollow = "";
                            } else {
                            }
                        } catch (Exception e) {

                        }
                    } else {
                        ErrorMessage.T(getActivity(), "Response not successful");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(getActivity(), "Response Fail");
                    System.out.println("============update profile fail  :" + t.toString());

                }
            });

        } else {
            ErrorMessage.T(getActivity(), getActivity().getString(R.string.no_internet));
        }

    }

    private void getPublisherfollowOnServer(String Author_id) {
        try {
            ErrorMessage.E("getPublisherfollowOnServer" + Const.ENDPOINT.Check_FollowCollections + Author_id + "/0/" + SavedData.getTokan());
            if (NetworkUtil.isNetworkAvailable(getActivity())) {  ///apis/follow/:itemID/:userID?/:fcmtoken?'
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
                            // ErrorMessage.T(getActivity(), "Response not successful");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        ErrorMessage.T(getActivity(), "Response Fail");
                        System.out.println("============update profile fail  :" + t.toString());

                    }
                });

            } else {
                ErrorMessage.T(getActivity(), getActivity().getString(R.string.no_internet));
            }
        } catch (Exception e) {
        }

    }
}
