package com.example.newsapiapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.newsapiapplication.Models.Articles;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    ArrayList<Articles> articlesArrayList;
    Context context;
    long postedTime;

    public NewsAdapter(ArrayList<Articles> articlesArrayList, Context context) {
        this.articlesArrayList = articlesArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_view_layout , parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date date = null;
        try {
            date = dateFormat.parse(articlesArrayList.get(position).getPublishedAt());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date currentTime = Calendar.getInstance().getTime();
        postedTime = (long) (currentTime.getTime()-date.getTime())/(1000*60*60);

        holder.text_posted_time.setText(postedTime+" hours ago");

        holder.text_title.setText(articlesArrayList.get(position).getTitle());
        holder.text_source.setText((articlesArrayList.get(position).getSource().getName()));
        holder.text_description.setText(articlesArrayList.get(position).getDescription());
        Glide
                .with(context)
                .load(articlesArrayList.get(position).getUrlToImage())
                .centerCrop()
                .placeholder(R.drawable.default_img)
                .into(holder.img_headline);

    }

    @Override
    public int getItemCount() {
        return articlesArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView text_title , text_source , text_description , text_posted_time;
        ImageView img_headline;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text_title = itemView.findViewById(R.id.text_title);
            text_posted_time = itemView.findViewById(R.id.text_posted_time);
            text_source = itemView.findViewById(R.id.text_source);
            text_description = itemView.findViewById(R.id.text_description);
            img_headline = itemView.findViewById(R.id.img_headline);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }
    public void filterList(ArrayList<Articles> filterList)
    {
        articlesArrayList = filterList;
        notifyDataSetChanged();
    }
}
