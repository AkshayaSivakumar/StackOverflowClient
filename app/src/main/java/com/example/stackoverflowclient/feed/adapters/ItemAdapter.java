package com.example.stackoverflowclient.feed.adapters;

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
import com.example.stackoverflowclient.network.NetworkState;
import com.example.stackoverflowclient.utils.AppUtils;
import com.example.stackoverflowclient.utils.CustomFlowLayout;
import com.example.stackoverflowclient.utils.StringConstants;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemAdapter extends PagedListAdapter<ItemModel, RecyclerView.ViewHolder> {

    private Context context;
    private static final int QUESTION_ITEM_VIEW_TYPE = 1;
    private static final int LOAD_ITEM_VIEW_TYPE = 0;
    private NetworkState mNetworkState;

    private static DiffUtil.ItemCallback<ItemModel> DIFF_CALLBACK = new DiffUtil.ItemCallback<ItemModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull ItemModel oldItem, @NonNull ItemModel newItem) {
            return oldItem.component8() == newItem.component8();
        }

        @Override
        public boolean areContentsTheSame(@NonNull ItemModel oldItem, @NonNull ItemModel newItem) {
            return oldItem.equals(newItem);
        }
    };

    public ItemAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        return (isLoadingData() && position == getItemCount() - 1) ? LOAD_ITEM_VIEW_TYPE : QUESTION_ITEM_VIEW_TYPE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView;
        if (viewType == QUESTION_ITEM_VIEW_TYPE) {
            itemView = inflater.inflate(R.layout.card_view, parent, false);
            return new ItemViewHolder(itemView);
        } else {
            itemView = inflater.inflate(R.layout.load_progress_item, parent, false);
            return new ProgressViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            ItemModel itemModel = getItem(position);
            itemViewHolder.bind(itemModel);
        }
    }

    public void setNetworkState(NetworkState networkState) {
        NetworkState prevState = networkState;
        boolean wasLoading = isLoadingData();
        mNetworkState = networkState;
        boolean willLoad = isLoadingData();
        if (wasLoading != willLoad) {
            if (wasLoading) notifyItemRemoved(getItemCount());
            else notifyItemInserted(getItemCount());
        }
    }

    public boolean isLoadingData() {
        return (mNetworkState != null && mNetworkState != NetworkState.LOADED);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
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

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(ItemModel itemModel) {
            if (null != itemModel) {
                OwnerModel ownerModel = itemModel.getOwner();

                questionTitleTv.setText(itemModel.getTitle());
                upVotesTv.setText(String.valueOf(itemModel.getUp_vote_count()));

                long creationDate = itemModel.getCreation_date();
                timeTv.setText(AppUtils.convertToLocalDateTime(creationDate, StringConstants.DATE_TIME_FORMAT));

                List<String> tagsList = itemModel.getTags();
                updateTagsView(tagsList);

                if (null != ownerModel)
                    userNameTv.setText(ownerModel.getDisplay_name());
            } else {
                Toast.makeText(context, "No items found", Toast.LENGTH_LONG).show();
            }
        }

        /**
         * Method to create custom button for tags and update in custom flow layout (FlowLayout reference taken from GitHub Library)
         *
         * @param tagsList list of tags
         */
        private void updateTagsView(List<String> tagsList) {
            flowLayout.removeAllViews();
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

                flowLayout.addView(myButton, params);

                myButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        Toast.makeText(view.getContext(), "Tag: " + btnName + " is clicked..", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    private static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressViewHolder(View itemView) {
            super(itemView);
        }
    }
}
