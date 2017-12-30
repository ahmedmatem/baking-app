package com.example.android.bakingapp.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.models.Step;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ahmed on 20/12/2017.
 */

public class RecipeStepListAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<Step> mSteps;

    public interface RecipeSteClickHandler{
        void onStepClick(int stepId);
    }

    public RecipeStepListAdapter(Context context,
                                 LayoutInflater layoutInflater,
                                 List<Step> steps) {
        mContext = context;
        mLayoutInflater = layoutInflater;
        mSteps = steps;
    }

    @Override
    public int getCount() {
        if(mSteps == null)
            return 0;
        return mSteps.size();
    }

    @Override
    public Step getItem(int position) {
        return mSteps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return (long)((Step)getItem(position)).mId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null){
            convertView = mLayoutInflater.inflate(R.layout.recipe_step_list_item,
                    parent, false);
            viewHolder = new ViewHolder();
            viewHolder.title = convertView.findViewById(R.id.tv_step_title);
            viewHolder.shortDescription =
                    convertView.findViewById(R.id.tv_step_short_description);
            convertView.setTag(viewHolder);
            viewHolder.videoHint = convertView.findViewById(R.id.iv_video_hint);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Step step = mSteps.get(position);
        if(step != null){
            viewHolder.stepId = step.mId;
            if(position == 0){
                viewHolder.title.setText("Introduction");
            } else {
                viewHolder.title.setText(String.format(mContext
                        .getString(R.string.step_number_text), position));
            }
            viewHolder.shortDescription.setText(step.mShortDescription);
            if(!(step.mVideoURL == null || step.mVideoURL.isEmpty())){
                // step video exists
                viewHolder.videoHint.setImageDrawable(
                        mContext.getResources().getDrawable(R.drawable.ic_video_camera)
                );
                viewHolder.videoHint.setVisibility(View.VISIBLE);
            } else {
                // no step video
                viewHolder.videoHint.setVisibility(View.INVISIBLE);
            }
        }

        return convertView;
    }

    static class ViewHolder {
        private int stepId;
        private TextView title;
        private TextView shortDescription;
        private ImageView videoHint;
    }
}
