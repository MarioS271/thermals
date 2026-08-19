plugins {
    id("java")
}

group = "net.marios271"
version = System.getenv("RELEASE_TAG")?.removePrefix("v") ?: "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation("net.java.dev.jna:jna:5.14.0")
    implementation("net.java.dev.jna:jna-platform:5.14.0")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("com.formdev:flatlaf:3.6")
    implementation("org.jfree:jfreechart:1.5.4")
    implementation("com.github.oshi:oshi-core:6.6.1")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
        vendor = JvmVendorSpec.matching("JetBrains")
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.register<Exec>("jpackageAppImage") {
    dependsOn("jar")

    val os = System.getProperty("os.name").lowercase()
    val isWindows = os.contains("win")
    val isLinux = os.contains("linux")

    doFirst {
        if (isWindows) {
            copy {
                from("resources/windows")
                into("build/libs")
            }
        }
    }

    commandLine(buildList {
        add("jpackage")
        add("--input"); add("build/libs")
        add("--main-jar"); add("thermals-${project.version}.jar")
        add("--main-class"); add("net.marios271.thermals.Thermals")
        add("--name"); add("Thermals")
        add("--app-version"); add("${project.version}")
        add("--type"); add("app-image")
        add("--dest"); add("build/package")
        add("--runtime-image"); add(System.getenv("JAVA_HOME") ?: System.getProperty("java.home").removeSuffix("/jre"))
        if (isWindows) { add("--icon"); add("logo/logo.ico") }
        if (isLinux) { add("--icon"); add("logo/logo_256.png") }
    })
}

tasks.register<Exec>("package") {
    dependsOn("jpackageAppImage")

    val os = System.getProperty("os.name").lowercase()

    if (os.contains("win")) {
        commandLine(
            "jpackage",
            "--app-image", "build/package/Thermals",
            "--name", "Thermals",
            "--app-version", "${project.version}",
            "--type", "msi",
            "--dest", "build/installer",
            "--win-shortcut",
            "--win-menu"
        )
    } else {
        doFirst {
            copy {
                from("logo/logo_256.png")
                into("build/package/Thermals")
                rename { "Thermals.png" }
            }

            val desktopFile = file("build/package/Thermals/Thermals.desktop")
            desktopFile.writeText("""
                [Desktop Entry]
                Name=Thermals
                Exec=Thermals
                Icon=Thermals
                Type=Application
                Categories=System;Monitor;
            """.trimIndent())

            val launcher = file("build/package/Thermals/bin/Thermals")
            val content = launcher.readText()
            println("launcher content:\n" + launcher.readText())
            launcher.writeText(content.replace(
                "exec \"\$JAVA_BIN\"",
                "exec \"\$JAVA_BIN\" -Dawt.toolkit.name=WLToolkit"
            ))
        }

        commandLine(
            "appimagetool",
            "build/package/Thermals",
            "build/installer/Thermals.AppImage"
        )
    }
}
