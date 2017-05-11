package com.example.panage.selfstudykotlin.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.flywaydb.core.Flyway
import org.springframework.jdbc.datasource.DriverManagerDataSource
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import javax.sql.DataSource

/**
 * @author fu-taku
 */
fun loadFromFile(path: Path): Map<*, *> {
    val mapper = ObjectMapper(YAMLFactory())
    mapper.registerModule(KotlinModule())

    return Files.newBufferedReader(path).use {
        mapper.readValue(it, Map::class.java)
    }
}

fun flywayMigrate(dataSource: DataSource = createDataSource(),
                  vararg locations: String = arrayOf("db/migration")): DataSource {
    val flyway = Flyway()
    flyway.dataSource = dataSource
    flyway.setLocations(*locations)
    flyway.migrate()
    return dataSource
}

fun createDataSource(): DataSource {

    //TODO
    val properties: Map<*, *> = loadFromFile(File("src/test/resources/application-test.yml").toPath())
    val springProperties = properties["spring"] as Map<*, *>
    val datasourceProperties = springProperties["datasource"] as Map<*, *>

    val dataSource = DriverManagerDataSource(
            datasourceProperties["url"] as String,
            datasourceProperties["username"] as String? ?: "",
            datasourceProperties["password"] as String? ?: ""
    )
    dataSource.setDriverClassName(datasourceProperties["driver-class-name"] as String)
    return dataSource
}
