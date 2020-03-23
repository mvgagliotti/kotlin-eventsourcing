package com.github.onlinestore.orderservice.akka.serialization

import akka.serialization.JSerializer
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

class JacksonSerializer : JSerializer() {

    private val mapper = jacksonObjectMapper()

    override fun fromBinaryJava(bytes: ByteArray?, manifest: Class<*>?): Any {
        check(manifest != null) { "manifest must be available" }
        return mapper.readValue(bytes, manifest)
    }

    override fun identifier(): Int = 40123

    override fun toBinary(o: Any?): ByteArray {
        return mapper.writeValueAsBytes(o)
    }

    override fun includeManifest(): Boolean = true
}