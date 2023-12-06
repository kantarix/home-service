package com.kantarix.home_service.api.services.container

import org.junit.ClassRule
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.PostgreSQLContainer

class PostgresTestContainer {

    companion object {
        @JvmField
        @ClassRule
        val postgresqlContainer: PostgreSQLContainer<*> = PostgreSQLContainer("postgres")
            .withDatabaseName("integration_tests_db")
            .withUsername("postgres")
            .withPassword("postgres")
            .also { it.start() }
    }

    internal class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
        override fun initialize(configurableApplicationContext: ConfigurableApplicationContext) {
            TestPropertyValues.of(
                "spring.datasource.url=${postgresqlContainer.jdbcUrl}",
                "spring.datasource.username=${postgresqlContainer.username}",
                "spring.datasource.password=${postgresqlContainer.password}"
            ).applyTo(configurableApplicationContext.environment)
        }
    }
}