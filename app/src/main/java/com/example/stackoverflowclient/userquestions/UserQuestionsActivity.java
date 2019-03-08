package com.example.stackoverflowclient.userquestions;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.stackoverflowclient.R;
import com.example.stackoverflowclient.base.BaseActivity;
import com.example.stackoverflowclient.base.BaseConstants;
import com.example.stackoverflowclient.model.QuestionsResponseModel;
import com.example.stackoverflowclient.network.ApiResponse;
import com.example.stackoverflowclient.network.ApiResponseListener;
import com.example.stackoverflowclient.network.ApiService;
import com.example.stackoverflowclient.network.ErrorModel;
import com.example.stackoverflowclient.network.RestApiClient;
import com.example.stackoverflowclient.utils.PreferenceHelper;
import com.example.stackoverflowclient.utils.StringConstants;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

public class UserQuestionsActivity extends BaseActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.tv_error)
    TextView errorTextView;

    ApiService apiService;

    PreferenceHelper preference;
    private String accessToken;

    QuestionsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_questions);

        ButterKnife.bind(this);

        apiService = RestApiClient.getClientInstance().getApiService();

        preference = PreferenceHelper.getInstance(this);
        accessToken = preference.get(StringConstants.ACCESS_TOKEN);

        setupRecyclerView();

        sendRequest();
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
    }

    private void sendRequest() {
        Call<QuestionsResponseModel> call = apiService.getQuestionsByUser(accessToken, BaseConstants.KEY, BaseConstants.ORDER_DESCENDING, BaseConstants.SORT_BY_ACTIVITY, BaseConstants.SITE, BaseConstants.FILTER);
        showProgress(this, "Loading...");

        ApiResponse.sendRequest(call, BaseConstants.QUESTIONS_BY_USER_API_SERVICE, new ApiResponseListener() {
            @Override
            public void onSuccess(String strApiName, Object response) {
                hideProgress();
                QuestionsResponseModel model = (QuestionsResponseModel) response;

                if (!model.getItems().isEmpty()) {
                    errorTextView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                    adapter = new QuestionsAdapter(UserQuestionsActivity.this, model);
                    recyclerView.setAdapter(adapter);
                } else {
                    errorTextView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);

                    errorTextView.setText("You have not posted any questions");
                }
            }

            @Override
            public void onError(String strApiName, ErrorModel errorModel) {
                hideProgress();

                errorTextView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);

                errorTextView.setText(errorModel.getError_message());
            }

            @Override
            public void onFailure(String strApiName, String message) {
                hideProgress();
                errorTextView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);

                errorTextView.setText(message);
            }
        });
    }

}
