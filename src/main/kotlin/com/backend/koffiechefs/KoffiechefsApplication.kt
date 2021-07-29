package com.backend.koffiechefs

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer

@SpringBootApplication(
		exclude = [DataSourceAutoConfiguration::class]
)
class KoffiechefsApplication : SpringBootServletInitializer() {

	override fun configure (application: SpringApplicationBuilder): SpringApplicationBuilder? {
		return application.sources(KoffiechefsApplication::class.java)
	}
}

fun main(args: Array<String>) {
	runApplication<KoffiechefsApplication>(*args)

}