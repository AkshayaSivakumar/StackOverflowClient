package com.example.stackoverflowclient.feed.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stackoverflowclient.R;
import com.example.stackoverflowclient.base.BaseConstants;
import com.example.stackoverflowclient.model.ItemModel;
import com.example.stackoverflowclient.network.ApiService;
import com.example.stackoverflowclient.network.RestApiClient;
import com.example.stackoverflowclient.feed.viewmodel.CustomViewModelFactory;
import com.example.stackoverflowclient.feed.adapters.ItemAdapter;
import com.example.stackoverflowclient.feed.viewmodel.ItemViewModel;
import com.example.stackoverflowclient.network.NetworkState;
import com.example.stackoverflowclient.utils.PreferenceHelper;
import com.example.stackoverflowclient.utils.StringConstants;

import org.jetbrains.annotations.NotNull;

public class QuestionsByHotFragment extends Fragment {
    @BindView(R.id.hot_recycler_view)
    RecyclerView recyclerView;

    private ApiService apiService;

    private PreferenceHelper preference;
    private String accessToken;

    public QuestionsByHotFragment() {
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_questions_by_hot, container, false);
        ButterKnife.bind(this, view);

        apiService = RestApiClient.getClientInstance().getApiService();

        preference = PreferenceHelper.getInstance(getContext());
        accessToken = preference.get(StringConstants.ACCESS_TOKEN);

        handleRecyclerView();
        return view;
    }

    private void handleRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        ItemViewModel itemViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(apiService, accessToken, BaseConstants.QUESTIONS_BY_HOT_API_SERVICE, BaseConstants.SORT_BY_HOT)).get(ItemViewModel.class);

        final ItemAdapter adapter = new ItemAdapter(getActivity());

        itemViewModel.getItemPagedList().observe(this, new Observer<PagedList<ItemModel>>() {
            @Override
            public void onChanged(PagedList<ItemModel> itemModels) {
                adapter.submitList(itemModels);
            }
        });

        itemViewModel.getNetworkStateLiveData().observe(this, new Observer<NetworkState>() {
            @Override
            public void onChanged(NetworkState networkState) {
                adapter.setNetworkState(networkState);
            }
        });
        recyclerView.setAdapter(adapter);
    }

}
