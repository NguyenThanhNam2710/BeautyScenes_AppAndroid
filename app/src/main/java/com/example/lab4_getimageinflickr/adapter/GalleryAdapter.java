package com.example.lab4_getimageinflickr.adapter;

import android.content.Context;
import android.os.Handler;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.lab4_getimageinflickr.CategoryActivity;
import com.example.lab4_getimageinflickr.R;
import com.example.lab4_getimageinflickr.model.Galleries_;
import com.example.lab4_getimageinflickr.model.Gallery;
import com.example.lab4_getimageinflickr.model.Photo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
    private List<Gallery> listData;
    private ArrayList<Photo> listImage;
    private Context context;

    public GalleryAdapter(List<Gallery> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.gallery_listview, parent, false);

        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Gallery item = listData.get(position);

        holder.btnGallery.setText(item.getTitle().getContent());
        holder.btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
                alertDialog.setView(R.layout.dialog_showdetailgallery);
                final AlertDialog dialog = alertDialog.show();
                final RecyclerView rvGallery = dialog.findViewById(R.id.rvGallery);
                final SwipeRefreshLayout mSrlLayoutG = dialog.findViewById(R.id.srlLayoutG);
                getImagesGalleries(item.getGalleryId(), rvGallery);
                mSrlLayoutG.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getImagesGalleries(item.getGalleryId(), rvGallery);
                                mSrlLayoutG.setRefreshing(false);
                            }
                        }, 2500);
                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private Button btnGallery;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnGallery = (Button) itemView.findViewById(R.id.btnGallery);
        }
    }

    private void getImagesGalleries(String id, RecyclerView rvGallery) {


        AndroidNetworking.post("https://www.flickr.com/services/rest")
                .addBodyParameter("api_key", "71e2a9a70ac5d577d67e353e03938a96")
                .addBodyParameter("gallery_id", id)
                .addBodyParameter("extras", "views, media, path_alias, url_sq, url_t, url_s, url_q, url_m, url_n, url_z, url_c, url_l, url_o")
                .addBodyParameter("format", "json")
                .addBodyParameter("method", "flickr.galleries.getPhotos")
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
                            rvGallery.setHasFixedSize(true);
                            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
                            rvGallery.setLayoutManager(staggeredGridLayoutManager);
                            RVAdapter mAdapter = new RVAdapter(listImage, context);
                            rvGallery.setAdapter(mAdapter);
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
