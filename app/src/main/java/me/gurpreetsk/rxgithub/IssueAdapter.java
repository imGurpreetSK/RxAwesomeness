package me.gurpreetsk.rxgithub;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.gurpreetsk.rxgithub.model.Issue;

/**
 * Created by Gurpreet on 15/05/17.
 */

public class IssueAdapter extends RecyclerView.Adapter<IssueAdapter.IssueViewHolder> {

    private Context context;
    private List<Issue> issues;

    private static final String TAG = IssueAdapter.class.getSimpleName();


    public IssueAdapter(Context context, List<Issue> issues) {
        this.context = context;
        this.issues = issues;
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
//        Picasso.with(context)
//                .load(issues.get(holder.getAdapterPosition()).getUser().getAvatarUrl())
//                .into(holder.imageviewUser);
        holder.issueLayout.setOnClickListener(v -> {
            new MaterialDialog.Builder(context)
                    .content(R.string.open_in_browser)
                    .positiveText(R.string.open)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            Intent intent = new Intent(Intent.ACTION_VIEW)
                                    .setData(Uri.parse(issues.get(holder.getAdapterPosition()).getHtmlUrl()));
                            context.startActivity(intent);
                        }
                    })
                    .negativeText(android.R.string.no)
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
