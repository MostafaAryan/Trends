package com.programmerofpersia.trends.data.datastore

import androidx.datastore.core.Serializer
import com.programmerofpersia.trends.data.datastore.model.ExploreFilter
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object ExploreFilterSerializer : Serializer<ExploreFilter> {

    override val defaultValue: ExploreFilter
        get() = ExploreFilter()

    override suspend fun readFrom(input: InputStream): ExploreFilter {
        return try {
            Json.decodeFromString(input.readBytes().decodeToString())
        } catch (e: SerializationException) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(
        t: ExploreFilter,
        output: OutputStream
    ) {
        output.write(
            Json.encodeToString(t).encodeToByteArray()
        )
    }


}