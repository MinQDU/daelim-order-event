package com.daelim

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DaelimOrderEventApplication

fun main(args: Array<String>) {
	runApplication<DaelimOrderEventApplication>(*args)
}
