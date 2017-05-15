package me.gurpreetsk.rxgithub;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import me.gurpreetsk.rxgithub.model.Issue;
import me.gurpreetsk.rxgithub.rest.ApiClient;
import me.gurpreetsk.rxgithub.rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        testCall();
        ApiInterface apiService = ApiClient.getInstance().create(ApiInterface.class);
        Observable<List<Issue>> issueObservable = apiService.getIssues("square", "retrofit");
        issueObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(issues -> {
                    List<Issue> issueList = new ArrayList<>();
                    for (Issue issue : issues) {
                        issueList.add(issue);
                    }
                    return issueList;
                })
                .subscribe(issuesList -> {
                    for (Issue issue : issuesList) {
                        Log.i(TAG, "Issue URL: " + issue.getUrl() + "\n");
                    }
                });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view ->
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show());
    }

//    private void testCall() {
//        ApiInterface apiService = ApiClient.getInstance().create(ApiInterface.class);
//        Call<List<Issue>> call = apiService.getIssues("square", "retrofit");
//        call.enqueue(new Callback<List<Issue>>() {
//            @Override
//            public void onResponse(Call<List<Issue>> call, Response<List<Issue>> response) {
//                Log.i(TAG, "onResponse: " + response.body().get(0).getUrl());
//                Log.i(TAG, "onResponse: " + response.body().get(response.body().size() - 1).getUrl());
//            }
//
//            @Override
//            public void onFailure(Call<List<Issue>> call, Throwable t) {
//                Log.e(TAG, "onFailure: ", t);
//            }
//        });
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
