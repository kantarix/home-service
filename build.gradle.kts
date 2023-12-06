import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.7.16"
	id("io.spring.dependency-management") version "1.0.15.RELEASE"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
	kotlin("plugin.jpa") version "1.9.10"
}

group = "com.kantarix"
version = "0.0.1"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	/**
	 * Spring
	 */
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.kafka:spring-kafka")

	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.mockito")
	}
	testImplementation("com.ninja-squad:springmockk:3.1.1")
	testImplementation("org.testcontainers:postgresql:1.17.6")

	/**
	 * Other
	 */
	implementation("org.postgresql:postgresql:42.6.0")
	implementation("org.liquibase:liquibase-core:4.24.0")

	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("io.github.microutils:kotlin-logging:3.0.5")

	implementation("org.springdoc:springdoc-openapi-kotlin:1.6.15")
	implementation("org.springdoc:springdoc-openapi-ui:1.6.15")
	implementation("org.springdoc:springdoc-openapi-webmvc-core:1.6.15")
}

allOpen {
	annotations(
		"javax.persistence.Entity",
		"javax.persistence.MappedSuperclass",
		"javax.persistence.Embeddable"
	)
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
