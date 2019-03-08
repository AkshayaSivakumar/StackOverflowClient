package com.example.stackoverflowclient.model

import java.io.Serializable

data class QuestionsResponseModel(
        val has_more: Boolean = false,
        val items: List<ItemModel> = emptyList(),
        val quota_max: Int = 10000,
        val quota_remaining: Int = 1
) : Serializable

data class ItemModel(
        val tags: List<String> = emptyList(),
        val owner: OwnerModel? = null,
        val is_answered: Boolean = false,
        val view_count: Int = 0,
        val up_vote_count: Int = 0,
        val answer_count: Int = 0,
        val creation_date: Long = 0,
        val question_id: Int = 0,
        val link: String = "",
        val title: String = ""
) : Serializable

data class OwnerModel(
        val display_name: String = "",
        val link: String = "",
        val profile_image: String = "",
        val user_id: Int = 0,
        val user_type: String = ""
) : Serializable