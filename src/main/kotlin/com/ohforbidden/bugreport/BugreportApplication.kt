package com.ohforbidden.bugreport

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BugreportApplication

fun main(args: Array<String>) {
	runApplication<BugreportApplication>(*args)
}
