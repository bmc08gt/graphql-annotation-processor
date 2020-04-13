package dev.bmcreations.graphql.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dev.bmcreations.graphql.model.attribute.GraphError
import dev.bmcreations.graphql.model.body.GraphContainer
import retrofit2.Response

fun Response<*>.errors(): List<GraphError> {
    try {
        val body = errorBody()
        val message = body?.string()
        if (body != null && !message.isNullOrBlank()) {
            return message.asGraphQLErrors()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return emptyList()
}

private fun String.asGraphQLErrors(): List<GraphError> {
    val tokenType = object : TypeToken<GraphContainer<*>>() {}.type
    val container = Gson().fromJson<GraphContainer<*>>(this, tokenType)
    return container.errors ?: emptyList()
}

class GraphErrorExtensions {
    companion object {
        @JvmStatic
        fun getErrorsFromResponse(response: Response<*>?): List<GraphError> {
            return response?.errors() ?: emptyList()
        }
    }
}
