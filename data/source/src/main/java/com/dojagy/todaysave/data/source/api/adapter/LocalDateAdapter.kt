package com.dojagy.todaysave.data.source.api.adapter

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.jvm.Throws

class LocalDateAdapter : TypeAdapter<LocalDate>() {

    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    @Throws(IOException::class)
    override fun write(
        out: JsonWriter,
        value: LocalDate?
    ) {
        if(value == null) {
            out.nullValue()
        }else {
            out.value(formatter.format(value))
        }
    }

    @Throws(IOException::class)
    override fun read(input: JsonReader): LocalDate? {
        return when (input.peek()) {
            JsonToken.NULL -> {
                input.nextNull()
                null
            }
            else -> {
                val string = input.nextString()
                LocalDate.parse(string, formatter)
            }
        }
    }
}