package com.example.francisco.w3d3;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.francisco.w3d3.Broadcast.ImgResultReceiver;
import com.example.francisco.w3d3.profile.GitProfile;
import com.example.francisco.w3d3.repos.GitRepo;
import com.example.francisco.w3d3.services.ImgService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ProfileActivity extends AppCompatActivity {

    public static final String PROFILE_URL = "https://api.github.com/users/francisco-villegas";
    public static final String REPOS_URL = "https://api.github.com/users/francisco-villegas/repos";
//    public static final String PROFILE_URL = "http://10.0.0.42/users/francisco-villegas";
//    public static final String REPOS_URL = "http://10.0.0.42/users/francisco-villegas/repos";

    private static final String TAG = "ProfileActivity";

    String resultResponse = "";
    String resultResponse2 = "";
    GitProfile gitProfile;
    ArrayList<GitRepo> reposList;
    ImgResultReceiver resultReceiever;

    RecyclerView rvReposList;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.ItemAnimator itemAnimator;

    @BindView(R.id.tvName)
    TextView tvName;

    @BindView(R.id.tvBio)
    TextView tvBio;

    @BindView(R.id.tvCompany)
    TextView tvCompany;

    @BindView(R.id.tvLocation)
    TextView tvLocation;

    @BindView(R.id.img)
    ImageView img;

    String img_ulr = "";
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ButterKnife.bind(this);
        getProfileData();

        context = getApplicationContext();

    }

    @OnClick({R.id.btnRepos, R.id.img})
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnRepos:
                getRepos();
                break;
            case R.id.img:
                getProfileData();
                break;
        }

    }

    public void getProfileData() {
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(PROFILE_URL)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ProfileActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Please make sure that you have internet connection", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                resultResponse = response.body().string();


                Gson gson = new Gson();
                gitProfile = gson.fromJson(resultResponse, GitProfile.class);
                ProfileActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvName.setText(gitProfile.getName());
                        tvBio.setText(gitProfile.getBio());
                        tvCompany.setText(gitProfile.getCompany());
                        tvLocation.setText(gitProfile.getLocation());
                        Log.d(TAG, "run: "+gitProfile.getAvatarUrl());
                        img_ulr = gitProfile.getAvatarUrl();
                        GetImg(img_ulr);
                    }
                });
            }
        });
    }

    public void GetImg(final String url){
        resultReceiever = new ImgResultReceiver(new Handler(), img, getApplicationContext());
        Intent startIntent = new Intent(this,
                ImgService.class);
        startIntent.putExtra("receiver", resultReceiever);
        startIntent.putExtra("url", url);
        startService(startIntent);
    }

    public void getRepos() {
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(REPOS_URL)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ProfileActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Please make sure that you have internet connection", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                resultResponse2 = response.body().string();


                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<GitRepo>>(){}.getType();
                reposList = gson.fromJson(resultResponse2, listType);
                ProfileActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rvReposList = (RecyclerView) findViewById(R.id.rvReposList);
                        layoutManager = new LinearLayoutManager(getApplicationContext());
                        itemAnimator = new DefaultItemAnimator();
                        rvReposList.setLayoutManager(layoutManager);
                        rvReposList.setItemAnimator(itemAnimator);
                        
                        RandomListAdapter randomsListAdapter = new RandomListAdapter(reposList);
                        rvReposList.setAdapter(randomsListAdapter);
                        randomsListAdapter.notifyDataSetChanged();
                        
                    }
                });
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("value1", reposList);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        reposList = savedInstanceState.getParcelableArrayList("value1");
        rvReposList = (RecyclerView) findViewById(R.id.rvReposList);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        itemAnimator = new DefaultItemAnimator();
        rvReposList.setLayoutManager(layoutManager);
        rvReposList.setItemAnimator(itemAnimator);

        RandomListAdapter randomsListAdapter = new RandomListAdapter(reposList);
        rvReposList.setAdapter(randomsListAdapter);
        randomsListAdapter.notifyDataSetChanged();
    }
}
