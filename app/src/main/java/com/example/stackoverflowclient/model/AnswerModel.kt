package com.example.stackoverflowclient.model

import java.io.Serializable
import java.security.acl.Owner

data class AnswerModel(
        var owner: Owner? = null,
        var accepted: Boolean? = false,
        var up_vote_count: Int? = 0,
        var is_accepted: Boolean? = false,
        var score: Int? = 0,
        var last_activity_date: Long? = 0,
        var creation_date: Long? = 0,
        var answer_id: Int? = 0,
        var question_id: Int? = 0,
        var last_edit_date: Int? = null
) : Serializable