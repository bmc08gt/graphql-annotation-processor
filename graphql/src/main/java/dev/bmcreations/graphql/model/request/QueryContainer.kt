package dev.bmcreations.graphql.model.request

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class QueryContainer @JvmOverloads constructor(
    var query: String = "",
    val variables: @RawValue MutableMap<String, Any?> = mutableMapOf()
): Parcelable
