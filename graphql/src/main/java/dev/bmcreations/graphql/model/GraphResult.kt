package dev.bmcreations.graphql.model

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response

data class Extension(val code: String? = null)
data class GraphQLError(val message: String, val extensions: Extension? = null)

fun <T> GraphQLError.asException(): Throwable {
    val responseCode = extensions?.code?.toIntOrNull() ?: 0

    return if (responseCode > 0) {
        // http exception
        // create response for retrofit
        val res = Response.error<T>(
            responseCode,
            message.toResponseBody("application/json".toMediaTypeOrNull())
        )
        HttpException(res)
    } else {
        GraphQLException(message)
    }
}

abstract class GraphResult<T, E>(
    open val errors: List<GraphQLError>? = emptyList(),
    val data: T? = null
) {
    fun hasErrors(): Boolean = errors?.isNotEmpty() ?: false
    abstract fun hasData(): Boolean
    abstract fun result(): E?
}

class GraphQLException(override val message: String) : Throwable(message)
