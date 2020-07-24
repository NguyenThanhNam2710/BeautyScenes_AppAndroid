package com.example.lab4_getimageinflickr.adapter;

import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab4_getimageinflickr.R;
import com.example.lab4_getimageinflickr.model.Photo;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {
    private List<Photo> listData;
    private Context context;

    public RVAdapter(List<Photo> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.my_listview, parent, false);

        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Photo item = listData.get(position);
        Picasso.get().load(item.getUrlC()).into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setView(R.layout.dialog_showdetail);
                final AlertDialog dialog = alertDialog.show();
                ImageView imgFull = dialog.findViewById(R.id.imgFull);
                Picasso.get().load(item.getUrlC()).into(imgFull);

                TextView tvTitle = (TextView) dialog.findViewById(R.id.tvTitle);
                TextView tvWidth = (TextView) dialog.findViewById(R.id.tvWidth);
                TextView tvHeight = (TextView) dialog.findViewById(R.id.tvHeight);
                TextView tvView = (TextView) dialog.findViewById(R.id.tvView);
                TextView tvPathalias = (TextView) dialog.findViewById(R.id.tvPathalias);
                TextView tvMedia = (TextView) dialog.findViewById(R.id.tvMedia);
                TextView tvLink = (TextView) dialog.findViewById(R.id.tvLink);
                tvTitle.setText(item.getTitle());
                tvHeight.setText("Height: "+item.getHeightM() + "px");
                tvWidth.setText("Width: "+item.getWidthM() + "px");
                tvView.setText(item.getViews() + "");
                tvPathalias.setText(item.getPathalias() + "");
                tvMedia.setText(item.getMedia() + "");

                tvLink.setText(item.getUrlM());
                tvLink.setMovementMethod(LinkMovementMethod.getInstance());
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }
}
