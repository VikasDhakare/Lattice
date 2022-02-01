package com.example.newsapiapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newsapiapplication.Models.Articles;
import com.example.newsapiapplication.Models.NewsModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView ;
    EditText searchView;
    ArrayList<Articles> articlesArrayList;
    NewsAdapter newsAdapter;
    TextView text_count;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        text_count = findViewById(R.id.text_count);
        searchView = findViewById(R.id.search_view);
        progressBar = findViewById(R.id.progress_bar);
        articlesArrayList= new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this , 1));
        newsAdapter = new NewsAdapter( articlesArrayList , this);
        recyclerView.setAdapter(newsAdapter);

        Searching();
        getNews();
    }

    private void getNews(){
        progressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://newsapi.org/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitApi retrofitApi = retrofit.create(RetrofitApi.class);
        Call<NewsModel> call = retrofitApi.getNews("in" , "general" , null , this.getString(R.string.api_key));

        call.enqueue(new Callback<NewsModel>() {
            @Override
            public void onResponse(Call<NewsModel> call, Response<NewsModel> response) {
                NewsModel model = response.body();
                ArrayList<Articles> articles = model.getArticles();
                for(int i =0 ; i<articles.size() ; i++){
                    articlesArrayList.add(new Articles(articles.get(i).getSource()  ,articles.get(i).getTitle() , articles.get(i).getDescription()
                    , articles.get(i).getUrl(), articles.get(i).getUrlToImage(), articles.get(i).getPublishedAt()));
                    newsAdapter.notifyDataSetChanged();
                }
                text_count.setText(articlesArrayList.size()+"");
                progressBar.setVisibility(View.GONE);


            }

            @Override
            public void onFailure(Call<NewsModel> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error!!!", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });

    }
    private void filter(String text) {
        ArrayList<Articles> filterList = new ArrayList<>();

        for (Articles model : articlesArrayList)
        {

            if (model.getTitle().toLowerCase().contains(text.toLowerCase()) )
            {
                filterList.add(model);
            }
        }
        newsAdapter.filterList(filterList);
        if(filterList.isEmpty()){
            text_count.setText("No Data Found");
        }else{
            text_count.setText(filterList.size()+"");
        }


    }

    private void Searching(){

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }
}