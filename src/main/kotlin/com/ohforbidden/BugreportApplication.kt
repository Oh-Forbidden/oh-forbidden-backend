package com.ohforbidden

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.util.TimeZone

@SpringBootApplication
class BugreportApplication

fun main(args: Array<String>) {
	setDefaultTimeZone("UTC")
	runApplication<BugreportApplication>(*args)
}

private fun setDefaultTimeZone(zoneId: String) = TimeZone.setDefault(TimeZone.getTimeZone(zoneId))
