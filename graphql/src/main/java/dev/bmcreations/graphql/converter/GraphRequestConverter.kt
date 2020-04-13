package dev.bmcreations.graphql.converter

import com.google.gson.Gson
import dev.bmcreations.graphql.annotation.processor.GraphQueryProcessor
import dev.bmcreations.graphql.model.request.QueryContainerBuilder
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Converter

open class GraphRequestConverter constructor(
    private val annotations: Array<Annotation>,
    private val processor: GraphQueryProcessor,
    private val gson: Gson
) : Converter<QueryContainerBuilder, RequestBody> {
    override fun convert(value: QueryContainerBuilder): RequestBody? {
        return processor.getQuery(annotations)?.let { query ->
            val container = value.setQuery(query).build()
            val json = gson.toJson(container)
            json.toByteArray().toRequestBody(GraphQLConverter.MEDIA_TYPE, 0, json.toByteArray().size)
        }
    }

}
