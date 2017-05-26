package me.gurpreetsk.rxgithub;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.StringJoiner;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.gurpreetsk.rxgithub.model.Comment;
import me.gurpreetsk.rxgithub.model.Issue;
import me.gurpreetsk.rxgithub.rest.ApiInterface;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Gurpreet on 15/05/17.
 */

public class IssueAdapter extends RecyclerView.Adapter<IssueAdapter.IssueViewHolder> {

    private Context context;
    private List<Issue> issues;
    private ApiInterface apiClient;
    private SharedPreferences preferences;

    private static final String TAG = IssueAdapter.class.getSimpleName();


    public IssueAdapter(Context context, List<Issue> issues, ApiInterface apiClient) {
        this.context = context;
        this.issues = issues;
        this.apiClient = apiClient;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public IssueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_issue, parent, false);
        return new IssueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IssueViewHolder holder, int position) {
        holder.textviewIssueTitle.setText(issues.get(holder.getAdapterPosition()).getTitle());
        holder.textviewIssueBody.setText(issues.get(holder.getAdapterPosition()).getBody());
        holder.textviewIssueNumber
                .setText(String.format("%s %s", "#", issues.get(holder.getAdapterPosition()).getNumber()));
        holder.textviewIssueUser
                .setText(String.format("%s: %s", "Username",
                        issues.get(holder.getAdapterPosition()).getUser().getLogin()));
        holder.textviewIssueUserUrl.setText(String.format("%s: %s", "Url",
                issues.get(holder.getAdapterPosition()).getUser().getUrl()));
        holder.textviewIssueUrl.setText(issues.get(holder.getAdapterPosition()).getUrl());
        holder.issueLayout.setOnClickListener(v -> {
            new MaterialDialog.Builder(context)
                    .content(R.string.create_comment)
                    .input(context.getString(R.string.add_comment_hint), context.getString(R.string.empty),
                            false, (dialog, input) -> {
//                                HashMap<String, String> map = new HashMap<String, String>();
//                                map.put("body", input.toString());
//                                RequestBody body =
//                                        RequestBody.create(
//                                                okhttp3.MediaType.parse("application/json; charset=utf-8"),
//                                                (new JSONObject(map)).toString());
                                Call<Comment> call = apiClient.addComment(
                                        preferences.getString("org", "").toLowerCase(),
                                        preferences.getString("repo", "").toLowerCase(),
                                        String.valueOf(issues
                                                .get(holder.getAdapterPosition()).getNumber()),
                                        input.toString());
                                call.enqueue(new Callback<Comment>() {
                                    @Override
                                    public void onResponse(Call<Comment> call,
                                                           Response<Comment> response) {
                                        Log.d(TAG, "onResponse: " + response.raw());
                                        try {
                                            Log.i(TAG, "onResponse: " + response.body());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Comment> call, Throwable t) {
                                        Log.e(TAG, "onFailure: ", t);
                                    }
                                });
                            })
                    .positiveText(R.string.create)
                    .negativeText(android.R.string.cancel)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return issues.size();
    }


    class IssueViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textview_issue_title)
        TextView textviewIssueTitle;
        @BindView(R.id.textview_issue_id)
        TextView textviewIssueNumber;
        @BindView(R.id.textview_issue_body)
        TextView textviewIssueBody;
        @BindView(R.id.textview_issue_url)
        TextView textviewIssueUrl;
        @BindView(R.id.textview_issue_user)
        TextView textviewIssueUser;
        @BindView(R.id.textview_issue_user_url)
        TextView textviewIssueUserUrl;
        @BindView(R.id.issue_layout)
        LinearLayout issueLayout;

        IssueViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
