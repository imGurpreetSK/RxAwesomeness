package me.gurpreetsk.rxgithub;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.gurpreetsk.rxgithub.model.Issue;
import me.gurpreetsk.rxgithub.rest.ApiClient;
import me.gurpreetsk.rxgithub.rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    ApiInterface apiService;

    private static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        apiService = ApiClient.getInstance().create(ApiInterface.class);

        fab.setOnClickListener(view -> showInputDialog());
    }

    @Override
    protected void onStart() {
        super.onStart();
        showInputDialog();
    }

    private void showInputDialog() {
        new MaterialDialog.Builder(this)
                .title(R.string.enter_full_repo_name)
                .content(R.string.empty)
                .input(R.string.example_repo, R.string.empty, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        MaterialDialog progressDialog = new MaterialDialog.Builder(MainActivity.this)
                                .content(R.string.fetching_data)
                                .progress(true, 0)
                                .cancelable(false)
                                .show();
                        try {
                            String org = input.toString().trim().split("/")[0];
                            String repo = input.toString().trim().split("/")[1];
                            Observable<List<Issue>> issueObservable = apiService.getIssues(org, repo);
                            issueObservable.subscribeOn(Schedulers.newThread())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .map(issues -> issues)
                                    .subscribe(new Subscriber<List<Issue>>() {
                                        @Override
                                        public void onCompleted() {
                                            Log.i(TAG, "onCompleted: COMPLETED!");
                                            progressDialog.dismiss();
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            Log.e(TAG, "onError: ", e);
                                            Toast.makeText(MainActivity.this, "No such repository found",
                                                    Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        }

                                        @Override
                                        public void onNext(List<Issue> issues) {
                                            recyclerView.setAdapter(new IssueAdapter(MainActivity.this, issues));
                                            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                                        }
                                    });
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Please check the input",
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                }).show();
    }

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
