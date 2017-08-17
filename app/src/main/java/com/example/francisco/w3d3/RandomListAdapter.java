package com.example.francisco.w3d3;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.francisco.w3d3.repos.GitRepo;

import java.util.ArrayList;

/**
 * Created by FRANCISCO on 10/08/2017.
 */

public class RandomListAdapter extends RecyclerView.Adapter<RandomListAdapter.ViewHolder> {

    private static final String TAG = "RandomListAdapter";
    ArrayList<GitRepo> repoList = new ArrayList<>();
    Context context;

    public RandomListAdapter(ArrayList<GitRepo> repoList) {
        this.repoList = repoList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvname, tv_owner, tv_description,tv_created,tv_pushed, tv_link;

        public ViewHolder(View itemView) {
            super(itemView);

            tvname = (TextView) itemView.findViewById(R.id.tvname);
            tv_owner = (TextView) itemView.findViewById(R.id.tv_owner);
            tv_description = (TextView) itemView.findViewById(R.id.tv_description);
            tv_created = (TextView) itemView.findViewById(R.id.tv_created);
            tv_pushed = (TextView) itemView.findViewById(R.id.tv_pushed);
            tv_link = (TextView) itemView.findViewById(R.id.tv_link);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.randomlist_item, parent, false);
        return new ViewHolder(view);
    }

    private int lastPosition = -1;
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if(position > lastPosition){
            //Animation animation = AnimationUtils
        }

        Log.d(TAG, "onBindViewHolder: ");
        final GitRepo repos = repoList.get(position);
        holder.tvname.setText("Name: "+context.getString(R.string.tab2)+repos.getName());
        holder.tv_owner.setText("Owner: "+context.getString(R.string.tab2)+repos.getOwner().getLogin());
        holder.tv_description.setText("Description: "+context.getString(R.string.tab1)+String.valueOf(repos.getDescription()));
        holder.tv_created.setText("Date Created: "+context.getString(R.string.tab)+repos.getCreatedAt());
        holder.tv_pushed.setText("Date Pushed: "+context.getString(R.string.tab)+repos.getPushedAt());
        holder.tv_pushed.setText("Link: "+repos.getHtmlUrl());

    }

    @Override
    public int getItemCount() {
        //Log.d(TAG, "getItemCount: "+repoList.size());
        return repoList.size();
    }
}
