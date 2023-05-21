package com.example

import com.example.controller.MainCrawler
import io.ktor.server.application.*
import com.example.plugins.*

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    MainCrawler.startCrawler()
    configureSerialization()
    configureRouting()
}
