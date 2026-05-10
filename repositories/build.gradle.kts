plugins {
    kotlin("jvm")
    kotlin("plugin.jpa")
    id("io.spring.dependency-management")
    jacoco
}

group = "org.unizar"
version = "0.0.1-SNAPSHOT"

dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:3.5.11")
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":core"))
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-mail")

    implementation("org.springframework.boot:spring-boot-starter-security")


    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

    implementation("io.minio:minio:8.5.7")

    implementation("org.jfree:jfreechart:1.5.4")
    implementation("com.itextpdf:itextpdf:5.5.13.3")


    implementation("org.springframework.boot:spring-boot-starter-web")

    testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")

    // Utilidades de testing para Spring Security y MockMvc
    testImplementation("org.springframework.security:spring-security-test")


    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.h2database:h2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
    finalizedBy("jacocoTestReport")
}

tasks.named<JacocoReport>("jacocoTestReport") {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    classDirectories.setFrom(
        files(classDirectories.files.map {
            fileTree(it) {
                exclude("**/*\$*.class")
                exclude("**/*\$*")
            }
        })
    )
}

kotlin {
    jvmToolchain(21)
}