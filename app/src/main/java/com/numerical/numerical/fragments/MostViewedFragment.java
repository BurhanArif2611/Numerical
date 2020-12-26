package com.numerical.numerical.fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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


public class MostViewedFragment extends Fragment {
    View view;
    @BindView(R.id.most_viewed_list_rv)
    RecyclerView mostViewedListRv;
    Unbinder unbinder;
    int count = 0;
    private boolean loading;
    ArrayList<String> stringArrayList=new ArrayList<>();
    private StaggeredGridLayoutManager gridLayoutManager;
    int pastVisiblesItems;
    int visibleItemCount;
    int totalItemCount;
    feed_Adapter side_rv_adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_most_viewed, container, false);
        unbinder = ButterKnife.bind(this, view);
        ((DashBoardActivity) getActivity()).launchFragmentTitle("MostNumerons");
        //((DashBoardActivity) getActivity()).GetCurrentPosition("");
        GetMostViewFeed(count);
        stringArrayList.clear();
        gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mostViewedListRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                            GetMostViewFeedOnScroll(count);
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

    private void GetMostViewFeed(int Count) {
        ErrorMessage.E("Count" + Count);
        if (NetworkUtil.isNetworkAvailable(getActivity())) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(getActivity());
            Call<ResponseBody> call = ApiClient.getLoadInterface().latest(Const.ENDPOINT.Mostviewed + "20/" + Count);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            materialDialog.dismiss();
                            JSONArray jsonArray = new JSONArray(response.body().string());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                stringArrayList.add(jsonObject.toString());
                            }
                            System.out.println("===response data Summery:" + stringArrayList.size());
                             side_rv_adapter = new feed_Adapter(getActivity(), jsonArray, stringArrayList,"Most Viewed");
                            mostViewedListRv.setLayoutManager(gridLayoutManager);
                            mostViewedListRv.setItemAnimator(new DefaultItemAnimator());
                            mostViewedListRv.setNestedScrollingEnabled(false);
                            mostViewedListRv.setAdapter(side_rv_adapter);
                            side_rv_adapter.notifyDataSetChanged();
                            loading = true;
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ErrorMessage.E("JSONException" + e.toString());
                            ErrorMessage.T(getActivity(), "Server Error");
                            materialDialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                            ErrorMessage.E("Exception" + e.toString());
                            ErrorMessage.T(getActivity(), "Server Error");
                            materialDialog.dismiss();
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
    private void GetMostViewFeedOnScroll(int Count) {
        ErrorMessage.E("Count" + Count);
        if (NetworkUtil.isNetworkAvailable(getActivity())) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(getActivity());
            Call<ResponseBody> call = ApiClient.getLoadInterface().latest(Const.ENDPOINT.Mostviewed + "20/" + Count);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            materialDialog.dismiss();
                            JSONArray jsonArray = new JSONArray(response.body().string());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                stringArrayList.add(jsonObject.toString());
                                side_rv_adapter.notifyDataSetChanged();
                            }
                            System.out.println("===response data Summery:" + stringArrayList.size());

                            loading = true;
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ErrorMessage.E("JSONException" + e.toString());
                            ErrorMessage.T(getActivity(), "Server Error");
                            materialDialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                            ErrorMessage.E("Exception" + e.toString());
                            ErrorMessage.T(getActivity(), "Server Error");
                            materialDialog.dismiss();
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
}
