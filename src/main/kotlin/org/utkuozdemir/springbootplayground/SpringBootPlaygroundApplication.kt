package org.utkuozdemir.springbootplayground

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringBootPlaygroundApplication

fun main(args: Array<String>) {
    runApplication<SpringBootPlaygroundApplication>(*args)
}
