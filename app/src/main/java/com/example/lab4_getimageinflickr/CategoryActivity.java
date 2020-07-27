package com.example.lab4_getimageinflickr;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.lab4_getimageinflickr.adapter.GalleryAdapter;
import com.example.lab4_getimageinflickr.adapter.RVAdapter;
import com.example.lab4_getimageinflickr.model.Gallery;
import com.example.lab4_getimageinflickr.model.Photo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView rvList;
    private ArrayList<Gallery> galleriesList;
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
                .addBodyParameter("method", "flickr.galleries.getList")
                .addBodyParameter("nojsoncallback", "1")
                .addBodyParameter("per_page", "1000")
                .addBodyParameter("page", "0").build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("response", response.toString() + "");
                        JSONObject galleries = null;
                        try {
                            galleries = response.getJSONObject("galleries");
                            Log.e("galleries", galleries.toString() + "");
                            JSONArray gallery = galleries.getJSONArray("gallery");
                            Log.e("gallery_length", gallery.length() + "");
                            galleriesList = new Gson().fromJson(gallery.toString(), new TypeToken<ArrayList<Gallery>>() {
                            }.getType());
                            for (int i = 0; i < galleriesList.size(); i++) {
                                Log.e("image[" + i + "]", galleriesList.get(i).getGalleryId());
                            }
                            rvList.setHasFixedSize(true);
                            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
                            rvList.setLayoutManager(staggeredGridLayoutManager);
                            GalleryAdapter mAdapter = new GalleryAdapter(galleriesList, CategoryActivity.this);
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