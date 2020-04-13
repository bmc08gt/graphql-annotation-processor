package dev.bmcreations.graphql.model.body

import dev.bmcreations.graphql.model.attribute.GraphError

data class GraphContainer<T>(
    val data: T?,
    val errors: List<GraphError>? = emptyList())
