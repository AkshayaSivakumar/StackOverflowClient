package com.example.stackoverflowclient.network

import java.io.Serializable

data class ErrorModel(
        var error_id: Int = 0,
        var error_message: String = "",
        var error_name: String = "",
        var apiName: String = ""
) : Serializable