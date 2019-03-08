package com.example.stackoverflowclient.userquestions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stackoverflowclient.R;
import com.example.stackoverflowclient.model.ItemModel;
import com.example.stackoverflowclient.model.OwnerModel;
import com.example.stackoverflowclient.model.QuestionsResponseModel;
import com.example.stackoverflowclient.utils.AppUtils;
import com.example.stackoverflowclient.utils.CustomFlowLayout;
import com.example.stackoverflowclient.utils.StringConstants;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.ViewHolder> {
    private Context context;
    private QuestionsResponseModel model;

    public QuestionsAdapter(Context context, QuestionsResponseModel model) {
        this.context = context;
        this.model = model;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.card_view, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        model.getItems();
        ItemModel itemModel = model.getItems().get(position);
        OwnerModel ownerModel = itemModel.getOwner();

        holder.questionTitleTv.setText(itemModel.getTitle());
        holder.upVotesTv.setText(String.valueOf(itemModel.getUp_vote_count()));

        long creationDate = itemModel.getCreation_date();
        holder.timeTv.setText(AppUtils.convertToLocalDateTime(creationDate, StringConstants.DATE_TIME_FORMAT));

        List<String> tagsList = itemModel.getTags();
        updateTagsView(holder, tagsList);

        if (null != ownerModel)
            holder.userNameTv.setText(ownerModel.getDisplay_name());

    }

    private void updateTagsView(ViewHolder holder, List<String> tagsList) {

        holder.flowLayout.removeAllViews();
        for (int i = 0; i < tagsList.size(); i++) {

            CustomFlowLayout.LayoutParams params = new CustomFlowLayout.LayoutParams(20, 20);

            Button myButton = new Button(context);
            myButton.setId(i);

            String btnName = tagsList.get(i);
            myButton.setText(btnName);
            myButton.setTag(btnName);
            myButton.setPadding(10, 5, 10, 5);

            myButton.setBackgroundResource(R.drawable.bg_selected);
            myButton.setTextColor(ContextCompat.getColor(context, R.color.white));

            holder.flowLayout.addView(myButton, params);

            myButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "Tag: " + btnName.toUpperCase() + " is clicked..", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return model.getItems().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_question_title)
        TextView questionTitleTv;

        @BindView(R.id.tv_user_name)
        TextView userNameTv;

        @BindView(R.id.tv_time_stamp)
        TextView timeTv;

        @BindView(R.id.tv_upvotes)
        TextView upVotesTv;

        @BindView(R.id.flow_layout)
        CustomFlowLayout flowLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
