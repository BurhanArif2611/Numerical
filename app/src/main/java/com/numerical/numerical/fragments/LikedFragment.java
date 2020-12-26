package com.numerical.numerical.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.numerical.numerical.R;
import com.numerical.numerical.Utility.ApiClient;
import com.numerical.numerical.Utility.Const;
import com.numerical.numerical.Utility.ErrorMessage;
import com.numerical.numerical.Utility.NetworkUtil;
import com.numerical.numerical.activity.DashBoardActivity;
import com.numerical.numerical.adapters.feed_Adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LikedFragment extends Fragment {
    View view;
    @BindView(R.id.liked_rv)
    RecyclerView likedRv;
    int count = 0;
    StaggeredGridLayoutManager gridLayoutManager;
    private JSONArray jsonArray;
    private boolean loading = true;
    int pastVisiblesItems;
    int visibleItemCount;
    int totalItemCount;
    ArrayList<String> stringArrayList = new ArrayList<>();
    Unbinder unbinder;
    private String Calling = "";
    private String Id = "";
    private String Name = "";
    private feed_Adapter side_rv_adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_liked, container, false);
        unbinder = ButterKnife.bind(this, view);
        ((DashBoardActivity) getActivity()).launchFragmentTitle("Liked");

        stringArrayList.clear();
        gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);


        GetLatestFeed(count);


        likedRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = gridLayoutManager.getChildCount();
                    totalItemCount = gridLayoutManager.getItemCount();
                    int[] firstVisibleItems = null;
                    firstVisibleItems = gridLayoutManager.findFirstVisibleItemPositions(firstVisibleItems);
                    ErrorMessage.E("firstVisibleItems firstVisibleItems" + firstVisibleItems.length);
                    if (firstVisibleItems != null && firstVisibleItems.length > 0) {
                        pastVisiblesItems = firstVisibleItems[0];
                    }
                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            Log.e("...", "Last Item Wow !");
                            //Do pagination.. i.e. fetch new data
                            count++;
                            count = count + 9;
                            GetLatestFeedWithPagination(count);
                        }
                    }
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

    private void GetLatestFeed(int Count) {
        ErrorMessage.E("Count" + Count);
        if (NetworkUtil.isNetworkAvailable(getActivity())) {
            Call<ResponseBody> call = null;
            final Dialog materialDialog = ErrorMessage.initProgressDialog(getActivity());
            ErrorMessage.E("GetLatestFeed"+Const.ENDPOINT.Check_FLiked + "10/" + Count);
            call = ApiClient.getLoadInterface().latest(Const.ENDPOINT.Check_FLiked + "10/" + Count);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    ErrorMessage.E("LikedFragment"+response.code());
                    if (response.isSuccessful()) {
                        try {
                            materialDialog.dismiss();
                            jsonArray = new JSONArray(response.body().string());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                stringArrayList.add(jsonObject.toString());
                            }
                            side_rv_adapter = new feed_Adapter(getActivity(), jsonArray, stringArrayList, Name);
                            likedRv.setLayoutManager(gridLayoutManager);
                            likedRv.setItemAnimator(new DefaultItemAnimator());
                            likedRv.setNestedScrollingEnabled(false);
                            likedRv.setAdapter(side_rv_adapter);
                            side_rv_adapter.notifyDataSetChanged();
                            ErrorMessage.E("firstVisibleItems GetLatestFeed" + stringArrayList.size());
                            loading = true;
                        }  catch (Exception e) {
                            e.printStackTrace();
                            try {
                                ErrorMessage.E("Exception" + e.toString());
                                ErrorMessage.T(getActivity(), "Server Error");
                                materialDialog.dismiss();
                            } catch (Exception e1) {
                            }
                        }


                    } else {
                        ErrorMessage.T(getActivity(), "Response not successful");
                        materialDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(getActivity(), "Response Fail");
                    System.out.println("============update profile fail  :" + t.toString());
                    materialDialog.dismiss();
                }
            });

        } else {
            ErrorMessage.T(getActivity(), this.getString(R.string.no_internet));

        }

    }

    private void GetLatestFeedWithPagination(int Count) {
        ErrorMessage.E("Count GetLatestFeedWithPagination" + Count);
        if (NetworkUtil.isNetworkAvailable(getActivity())) {
            // final Dialog materialDialog = ErrorMessage.initProgressDialog(getActivity());
            Call<ResponseBody> call = null;
            call = ApiClient.getLoadInterface().latest(Const.ENDPOINT.Check_FLiked + "10/" + Count);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            //  materialDialog.dismiss();
                            jsonArray = new JSONArray(response.body().string());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                stringArrayList.add(jsonObject.toString());
                                side_rv_adapter.notifyDataSetChanged();
                            }
                            ErrorMessage.E("firstVisibleItems" + stringArrayList.size());
                           /* feed_Adapter side_rv_adapter = new feed_Adapter(getActivity(), jsonArray, stringArrayList,Name);
                            gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                            vehicalRateListRv.setLayoutManager(gridLayoutManager);
                            vehicalRateListRv.setItemAnimator(new DefaultItemAnimator());
                            vehicalRateListRv.setNestedScrollingEnabled(false);
                            vehicalRateListRv.setAdapter(side_rv_adapter);
                            side_rv_adapter.notifyDataSetChanged();*/
                            loading = true;

                        } catch (JSONException e) {
                            e.printStackTrace();
                            ErrorMessage.E("JSONException" + e.toString());
                            ErrorMessage.T(getActivity(), "Server Error");
                            // materialDialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                            ErrorMessage.E("Exception" + e.toString());
                            ErrorMessage.T(getActivity(), "Server Error");
                            //materialDialog.dismiss();
                        }


                    } else {
                        ErrorMessage.T(getActivity(), "Response not successful");
                        // materialDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(getActivity(), "Response Fail");
                    System.out.println("============update profile fail  :" + t.toString());
                    //materialDialog.dismiss();
                }
            });

        } else {
            ErrorMessage.T(getActivity(), this.getString(R.string.no_internet));

        }

    }
}
