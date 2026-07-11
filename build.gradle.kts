plugins {
    id("java")
}

group = "net.marios271"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation("com.formdev:flatlaf:3.4.1")
    implementation("org.jfree:jfreechart:1.5.4")
    implementation("com.github.oshi:oshi-core:6.6.1")
}

tasks.test {
    useJUnitPlatform()
}
