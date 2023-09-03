package com.programmerofpersia.trends.data.remote.model.dto.explore

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

object SearchedKeywordSerializer : JsonContentPolymorphicSerializer<KeywordDto>(KeywordDto::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<KeywordDto> {
        return when {
            "topic" in element.jsonObject -> KeywordDto.Topic.serializer()
            "query" in element.jsonObject -> KeywordDto.Query.serializer()
            else -> throw Exception("Serializer error. Type is not 'topic' nor 'query'.")  /* todo use string resource */
        }
    }
}