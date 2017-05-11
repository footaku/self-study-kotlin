package com.example.panage.selfstudykotlin

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class SelfStudyKotlinApplication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(SelfStudyKotlinApplication::class.java, *args)
        }
    }
}
