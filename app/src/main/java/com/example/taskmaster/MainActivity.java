package com.example.taskmaster;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.analytics.AnalyticsEvent;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
import com.example.taskmaster.Adapter.RecyclerViewAdapter;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TASK_NAME = "taskName";
    public static final String TASK_TITLE = "taskTitle";
    public static final String TASK_BODY = "taskBody";
    public static final String TASK_STATUS = "taskStatus";
    public static final String TASK_LIST = "TaskList";
    public static final String TASK_ID = "taskID";
    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerViewAdapter adapter;
    private Handler handler;
    private Team teamData = null;
    private String teamNameData = null;
    private String currentUsername = null;
    private String userId ;
    private InterstitialAd mInterstitialAd;
    private RewardedAd mRewardedAd;
    //ca-app-pub-7951919024402182/8901294398

    List<Task> dataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button newTask = findViewById(R.id.addTaskButton);
        newTask.setOnClickListener(this.newTask);

        Button allTasks = findViewById(R.id.allTasksButton);
        allTasks.setOnClickListener(this.allTasks);


        handler = new Handler(Looper.getMainLooper(),
                new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message message) {
                        listItemDeleted();
                        return false;
                    }
                });

        if (Amplify.Auth.getCurrentUser()!= null){
            Log.i(TAG, "Auth: " + Amplify.Auth.getCurrentUser().toString());
        }else {
            Log.i(TAG, "Auth:  no user " + Amplify.Auth.getCurrentUser());
            Intent goToLogin= new Intent(this,LoginActivity.class);
            startActivity(goToLogin);
        }

        // Banner Ads

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Log.i(TAG , "The ads is appear");
            }
        });

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // Interstitial Ads
        Button InterstitialBtn = findViewById(R.id.Interstitial_ads_btn);
        InterstitialBtn.setOnClickListener(view -> {
        setInterstitialAd();
        if (mInterstitialAd != null) {
            mInterstitialAd.show(MainActivity.this);
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }
        });

        // Reward Ads

        Button RewardedBtn = findViewById(R.id.rewarded_btn);

        RewardedBtn.setOnClickListener(view -> {
            setRewardedAd();
            if (mRewardedAd != null) {
                Activity activityContext = MainActivity.this;
                mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                    @Override
                    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                        // Handle the reward.
                        Log.d(TAG, "The user earned the reward.");
                        int rewardAmount = rewardItem.getAmount();
                        String rewardType = rewardItem.getType();
                    }
                });
            } else {
                Log.d(TAG, "The rewarded ad wasn't ready yet.");
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();

        if (Amplify.Auth.getCurrentUser()!= null){
            TextView userNameText = (findViewById(R.id.userTasksLabel));
            userNameText.setText(Amplify.Auth.getCurrentUser().getUsername()+ "'s Tasks");
        }else {
            Intent goToLogin= new Intent(this,LoginActivity.class);
            startActivity(goToLogin);
        }

        RecyclerView recyclerView = findViewById(R.id.List_tasks);
        dataList = new ArrayList<>();

        showTeamTasks();
        adapter = new RecyclerViewAdapter(dataList, new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(int position) {
                Intent goToDetailsIntent = new Intent(getApplicationContext(), TaskDetailActivity.class);
                goToDetailsIntent.putExtra(TASK_ID ,  dataList.get(position).getId());
                AnalyticsEvent event = AnalyticsEvent.builder()
                        .name("Task Detail")
                        .addProperty("Id",dataList.get(position).getId())
                        .addProperty("Title", dataList.get(position).getTitle())
                        .addProperty("Body", dataList.get(position).getBody())
                        .build();

                Amplify.Analytics.recordEvent(event);
                startActivity(goToDetailsIntent);
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void showTeamTasks(){
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        //String username = preference.getString("username", "user") + "'s Tasks";
        String teamName = "Your Team Name is: " + preference.getString("teamName", "Choose your team");

        TextView userLabel = findViewById(R.id.userTasksLabel);
        TextView teamNameLabel = findViewById(R.id.teamTasksLabel);
        teamNameData = preference.getString("teamName", teamName);
        //userLabel.setText(username);
        teamNameLabel.setText(teamName);
        if (teamNameData!= null){
            getTeamDetailFromAPIByName();
        }else{
            getTaskDataFromAPI();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                goToSettings();
                return true;

            case R.id.logout:
                signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private View.OnClickListener newTask = new View.OnClickListener() {
        public void onClick(View v) {
            Intent i = new Intent(getBaseContext(), AddTask.class);
            startActivity(i);
        }
    };

    private View.OnClickListener allTasks = new View.OnClickListener() {
        public void onClick(View v) {
            Intent i = new Intent(getBaseContext(), AllTasks.class);
            startActivity(i);
        }
    };

    private void goToSettings(){
            Intent intent = new Intent(getBaseContext(), SettingsActivity.class);
            startActivity(intent);
    }

    public  void getTaskDataFromAPI(){
        Amplify.API.query(ModelQuery.list(Task.class),
                response -> {
                    for (Task task : response.getData()) {
                        dataList.add(task);
                        Log.i(TAG, "getFrom api: the Task from api are => " + task.toString());
                    }
                    handler.sendEmptyMessage(1);
                },
                error -> Log.e(TAG, "getFrom api: Failed to get Task from api => " + error)
        );
    }

    public static void saveTaskToAPI(Task item){
        //System.out.println(item);
        Amplify.API.mutate(ModelMutation.create(item),
                success -> Log.i(TAG, "Saved item to api : " + success.getData().getTitle()),
                error -> Log.e(TAG, "Could not save item to API/dynamodb", error));
    }


    public static void saveTeamToAPI(Team item){
        System.out.println(item + "**************************rawzi*********************");
        Amplify.API.mutate(ModelMutation.create(item),
                success -> Log.i(TAG, "Saved item to api : " + success),
                error -> Log.e(TAG, "Could not save item to API/dynamodb", error));
    }

    public void getTeamDetailFromAPIByName() {
        Log.i(TAG, "getTeamDetailFromAPIByName: get team");
        Amplify.API.query(
                ModelQuery.list(Team.class, Team.NAME.contains(teamNameData)),
                response -> {
                    for (Team teamDetail : response.getData()) {
                        Log.i(TAG, teamDetail.toString());
                        if(teamDetail != null) {
                            teamData = teamDetail;
                            dataList.clear();
                            getTaskDataFromAPIByTeam();
                        }
                    }
                },
                error -> Log.e(TAG, "Query failure", error)
        );
    }

    public  void  getTaskDataFromAPIByTeam(){
        Log.i(TAG, "getTaskDataFromAPIByTeam: get task by team");
        Amplify.API.query(ModelQuery.list(Task.class, Task.TEAM_TASKS_ID.contains(teamData.getId())),
                response -> {
                    for (Task task : response.getData()) {

                        Log.i(TAG, "task-team-id: " + task.getId());
                        Log.i(TAG, "team-id: "+ teamData.getId());
                        dataList.add(task);
                        Log.i(TAG, "getFrom api by team: the Task from api are => " + task);
                    }
                    handler.sendEmptyMessage(1);
                },
                error -> Log.e(TAG, "getFrom api: Failed to get Task from api => " + error)
        );
    }

    public void signOut(){
        Amplify.Auth.signOut(
                () ->{
                    Log.i(TAG, "Signed out successfully");
                    Intent goToLogin = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(goToLogin);
                } ,
                error -> Log.e(TAG, error.toString())
        );
    }

    @SuppressLint("NotifyDataSetChanged")
    private void listItemDeleted() {
        adapter.notifyDataSetChanged();
    }

    //********************************************* InterstitialAd**********************************

    public void setInterstitialAd(){
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");

                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when fullscreen content is dismissed.
                                Log.d("TAG", "The ad was dismissed.");
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when fullscreen content failed to show.
                                Log.d("TAG", "The ad failed to show.");
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when fullscreen content is shown.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                mInterstitialAd = null;
                                Log.d("TAG", "The ad was shown.");
                            }
                        });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i(TAG, loadAdError.getMessage());
                        mInterstitialAd = null;
                    }
                });



    }

    //***********************************RewardedAd*************************************************

    private void setRewardedAd(){
        AdRequest adRequest = new AdRequest.Builder().build();

        RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917",
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d(TAG, loadAdError.getMessage());
                        mRewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                        Log.d(TAG, "Ad was loaded.");

                        mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when ad is shown.
                                Log.d(TAG, "Ad was shown.");
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when ad fails to show.
                                Log.d(TAG, "Ad failed to show.");
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when ad is dismissed.
                                // Set the ad reference to null so you don't show the ad a second time.
                                Log.d(TAG, "Ad was dismissed.");
                                mRewardedAd = null;
                            }
                        });
                    }
                });
    }
}