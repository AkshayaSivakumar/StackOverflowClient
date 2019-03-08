package com.example.stackoverflowclient.base;

public interface BaseConstants {

    String BASE_URL = "https://api.stackexchange.com";
    String QUESTIONS_END_POINT = "/2.2/questions";
    String USER_QUESTIONS_END_POINT = "/2.2/me/questions";

    String ACCESS_TOKEN_INVALIDATION_END_POINT = "/access-tokens/{accessTokens}/invalidate";

    String LOGIN_URL = "https://stackexchange.com/oauth/dialog?client_id=YOUR_CLIENT_ID&amp;scope=no_expiry,private_info&amp;redirect_uri=https://stackexchange.com/oauth/login_success";

    String KEY = "YOUR_KEY";

    String SITE = "stackoverflow";

    /**
     * Filter for getting upvote count in the response
     */
    String FILTER = "!Fc6b6oecdMxo)a9GWh_oLGugZw";

    int PAGE_SIZE = 50;

    String ORDER_DESCENDING = "desc";

    String SORT_BY_ACTIVITY = "activity";
    String SORT_BY_VOTES = "votes";
    String SORT_BY_CREATION = "creation";
    String SORT_BY_HOT = "hot";

    String LOGOUT_API_SERVICE = "LogoutApi";
    String QUESTIONS_BY_ACTIVITY_API_SERVICE = "QuestionsByActivityApi";
    String QUESTIONS_BY_VOTES_API_SERVICE = "QuestionsByVoteApi";
    String QUESTIONS_BY_CREATION_API_SERVICE = "QuestionsByApi";
    String QUESTIONS_BY_HOT_API_SERVICE = "QuestionsByHotApi";

    String QUESTIONS_BY_USER_API_SERVICE = "QuestionsByUserApi";

}
