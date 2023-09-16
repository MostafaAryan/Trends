package com.programmerofpersia.trends.data.datastore.model

import com.programmerofpersia.trends.data.domain.model.explore.BaseFilterInfo
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.toPersistentMap
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.serialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = PersistentMap::class)
class PersistentMapSerializer(
    private val keySerializer: KSerializer<String>,
    private val valueSerializer: KSerializer<BaseFilterInfo>
) : KSerializer<PersistentMap<String, BaseFilterInfo>> {
    private class PersistentMapDescriptor :
        SerialDescriptor by serialDescriptor<Map<String, BaseFilterInfo>>() {
        @ExperimentalSerializationApi
        override val serialName: String = "kotlinx.serialization.immutable.persistentMap"
    }

    override val descriptor: SerialDescriptor = PersistentMapDescriptor()

    override fun serialize(encoder: Encoder, value: PersistentMap<String, BaseFilterInfo>) {
        return MapSerializer(keySerializer, valueSerializer).serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): PersistentMap<String, BaseFilterInfo> {
        return MapSerializer(keySerializer, valueSerializer).deserialize(decoder).toPersistentMap()
    }

}