package cl.donaton.donaton

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DonatonApplication

fun main(args: Array<String>) {
	runApplication<DonatonApplication>(*args)
}
