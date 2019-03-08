package com.example.stackoverflowclient.network;

import com.example.stackoverflowclient.base.BaseConstants;
import com.example.stackoverflowclient.model.QuestionsResponseModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    /**
     * Logout request - Since there is no proper way to logout, Access token is invalidated and cache is cleared
     *
     * @param accessToken Current user's access token
     * @return json object with empty "items" object
     */
    @GET(BaseConstants.ACCESS_TOKEN_INVALIDATION_END_POINT)
    Call<QuestionsResponseModel> invalidateAccessToken(@Path("accessTokens") String accessToken);

    @GET(BaseConstants.QUESTIONS_END_POINT)
    Call<QuestionsResponseModel> getQuestionsForLoggedInUser(
            @Query(value = "access_token", encoded = true) String accessToken,
            @Query(value = "key", encoded = true) String key,
            @Query("page") int page,
            @Query("pagesize") int pageSize,
            @Query("order") String order,
            @Query("sort") String sortCondition,
            @Query("site") String site,
            @Query(value = "filter", encoded = true) String filter
    );

    @GET(BaseConstants.QUESTIONS_END_POINT)
    Call<QuestionsResponseModel> getQuestionsForAll(
            @Query("page") int page,
            @Query("pagesize") int pageSize,
            @Query("order") String order,
            @Query("sort") String sortCondition,
            @Query("site") String site,
            @Query(value = "filter", encoded = true) String filter
    );

    @GET(BaseConstants.USER_QUESTIONS_END_POINT)
    Call<QuestionsResponseModel> getQuestionsByUser(
            @Query(value = "access_token", encoded = true) String accessToken,
            @Query(value = "key", encoded = true) String key,
            @Query("order") String order,
            @Query("sort") String sortCondition,
            @Query("site") String site,
            @Query(value = "filter", encoded = true) String filter
    );

}
