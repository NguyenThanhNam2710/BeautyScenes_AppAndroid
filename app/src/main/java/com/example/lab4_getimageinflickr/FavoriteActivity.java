package com.example.lab4_getimageinflickr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.lab4_getimageinflickr.adapter.RVAdapter;
import com.example.lab4_getimageinflickr.model.Photo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView rvList;
    private ArrayList<Photo> listImage;
    private SwipeRefreshLayout mSrlLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        rvList = (RecyclerView) findViewById(R.id.rvList);
        mSrlLayout = (SwipeRefreshLayout) findViewById(R.id.srlLayout);
        mSrlLayout.setOnRefreshListener(this);

        setUpData();
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setupAdapter();
                mSrlLayout.setRefreshing(false);
            }
        }, 2500);
    }

    private void setupAdapter() {
        setUpData();
    }

    private void setUpData() {
        AndroidNetworking.post("https://www.flickr.com/services/rest")
                .addBodyParameter("api_key", "71e2a9a70ac5d577d67e353e03938a96")
                .addBodyParameter("user_id", "187043301@N04")
                .addBodyParameter("extras", "views, media, path_alias, url_sq, url_t, url_s, url_q, url_m, url_n, url_z, url_c, url_l, url_o")
                .addBodyParameter("format", "json")
                .addBodyParameter("method", "flickr.favorites.getList")
                .addBodyParameter("nojsoncallback", "1")
                .addBodyParameter("per_page", "1000")
                .addBodyParameter("page", "0").build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("response", response.toString() + "");
                        try {
                            JSONObject photos = response.getJSONObject("photos");
                            Log.e("photos", photos.toString() + "");
                            JSONArray photo = photos.getJSONArray("photo");
                            Log.e("photo_length", photo.length() + "");
                            listImage = new Gson().fromJson(photo.toString(), new TypeToken<ArrayList<Photo>>() {
                            }.getType());
                            for (int i = 0; i < listImage.size(); i++) {
                                Log.e("image[" + i + "]", listImage.get(i).getUrlM());
                            }
                            rvList.setHasFixedSize(true);
                            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
                            rvList.setLayoutManager(staggeredGridLayoutManager);
                            RVAdapter mAdapter = new RVAdapter(listImage, FavoriteActivity.this);
                            rvList.setAdapter(mAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }
}