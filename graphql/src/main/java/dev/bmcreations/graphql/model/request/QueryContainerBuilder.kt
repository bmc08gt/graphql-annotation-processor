package dev.bmcreations.graphql.model.request

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
open class QueryContainerBuilder(private val container: QueryContainer? = QueryContainer()) : Parcelable {

    fun setQuery(query: String): QueryContainerBuilder {
        container?.query = query
        return this
    }

    fun putVariable(key: String, value: Any?): QueryContainerBuilder {
        container?.variables?.put(key, value)
        return this
    }

    fun containsVariable(key: String): Boolean = container?.variables?.containsKey(key) ?: false

    fun build(): QueryContainer? = container
}
