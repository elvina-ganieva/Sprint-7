package com.example.pleasework

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.ServletComponentScan

@SpringBootApplication
@ServletComponentScan
class PleaseworkApplication

fun main(args: Array<String>) {
    runApplication<PleaseworkApplication>(*args)
}
