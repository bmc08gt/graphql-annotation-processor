package dev.bmcreations.graphql.annotation.processor

import android.content.Context
import android.util.Log
import dev.bmcreations.graphql.annotation.GraphQuery
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class GraphQueryProcessor @JvmOverloads constructor(
    val context: Context,
    val qualifier: String = QUALIFIER,
    val root: String = ROOT
) {

    companion object {
        const val QUALIFIER = ".graphql"
        const val ROOT = "graphql"
    }

    val graphFiles = mutableMapOf<String, String>()

    init {
        findAllFiles(root)
    }

    private fun findAllFiles(path: String) {
        context.assets.list(root)?.forEach { item ->
            val absolute = "$path/$item"
            if (!item.endsWith(QUALIFIER)) {
                findAllFiles(absolute)
            } else {
                graphFiles[item] = getGraphContents(context.assets.open(absolute))
            }
        }
    }

    private fun getGraphContents(input: InputStream): String {
        val queryBuilder = StringBuilder()
        val isr = InputStreamReader(input)
        val br = BufferedReader(isr)
        isr.use { _ ->
            br.useLines { lines ->
                lines.forEach {
                    queryBuilder.append(it)
                }
            }
        }
        return queryBuilder.toString()
    }

    fun getQuery(annotations: Array<Annotation>): String? {
        var query: GraphQuery? = null

        for (element in annotations) {
            if (element is GraphQuery) {
                query = element
                break
            }
        }

        return query?.let {
            val file = "%s%s".format(query.value, qualifier)
            if (graphFiles.containsKey(file)) {
                graphFiles[file]
            } else {
                Log.w("GraphQueryProcessor", "The request query (%s) could not be found!".format(query.value))
                null
            }
        }
    }
}
