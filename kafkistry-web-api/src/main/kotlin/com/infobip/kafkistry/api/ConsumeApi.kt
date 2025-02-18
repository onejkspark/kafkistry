package com.infobip.kafkistry.api

import com.infobip.kafkistry.kafka.Partition
import com.infobip.kafkistry.service.consume.KafkaConsumerService
import com.infobip.kafkistry.service.consume.KafkaRecordsResult
import com.infobip.kafkistry.service.consume.ReadConfig
import com.infobip.kafkistry.model.KafkaClusterIdentifier
import com.infobip.kafkistry.model.TopicName
import com.infobip.kafkistry.service.consume.serialize.KeySerializerType
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("\${app.http.root-path}/api/consume")
@ConditionalOnProperty("app.consume.enabled", matchIfMissing = true)
class ConsumeApi(
    private val topicConsumer: KafkaConsumerService
) {

    @PostMapping("/read-topic")
    fun readTopic(
        @RequestParam("clusterIdentifier") clusterIdentifier: KafkaClusterIdentifier,
        @RequestParam("topicName") topicName: TopicName,
        @RequestBody readConfig: ReadConfig,
    ): KafkaRecordsResult = topicConsumer.readRecords(clusterIdentifier, topicName, readConfig)

    @GetMapping("/partition-of-key")
    fun partitionForKey(
        @RequestParam("clusterIdentifier") clusterIdentifier: KafkaClusterIdentifier,
        @RequestParam("topicName") topicName: TopicName,
        @RequestParam("key") key: String,
        @RequestParam("serializerType") serializerType: KeySerializerType,
    ): Partition = topicConsumer.resolvePartitionForKey(clusterIdentifier, topicName, key, serializerType)

}