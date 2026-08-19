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

val mainClassName = "net.marios271.thermals.Thermals"

// Self-contained ("fat") jar: bundles every runtime dependency so the packaged
// app has a complete classpath. The previous thin jar was why the elevated
// instance died once it tried to load OSHI/JNA/FlatLaf/etc.
tasks.jar {
    manifest {
        attributes["Main-Class"] = mainClassName
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from({
        configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) }
    })
    exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA", "module-info.class")
}

// ---- Packaging ------------------------------------------------------------
// Two stages: build a jpackage "app-image", then wrap it (MSI on Windows,
// AppImage on Linux). The JBR is bundled directly as the runtime image so the
// app ships with the JetBrains runtime (needed for WLToolkit on Wayland).

val jbrHome = System.getenv("JAVA_HOME") ?: System.getProperty("java.home")

tasks.register<Exec>("appImage") {
    dependsOn("jar")

    val os = System.getProperty("os.name").lowercase()
    val isWindows = os.contains("win")
    val isLinux = os.contains("linux")

    doFirst {
        // On Windows, bundle the PawnIO driver + reader (placed in build/libs by CI)
        // so they land next to the jar inside the app directory.
        if (isWindows) {
            copy {
                from("resources/windows")
                into("build/libs")
            }
        }
        delete("build/package")
    }

    commandLine(buildList {
        add("jpackage")
        add("--input"); add("build/libs")
        add("--main-jar"); add("thermals-${project.version}.jar")
        add("--main-class"); add(mainClassName)
        add("--name"); add("Thermals")
        add("--app-version"); add("${project.version}")
        add("--type"); add("app-image")
        add("--dest"); add("build/package")
        add("--runtime-image"); add(jbrHome)
        if (isWindows) { add("--icon"); add("logo/logo.ico") }
        if (isLinux) { add("--icon"); add("logo/logo_256.png") }
    })
}

tasks.register<Exec>("package") {
    dependsOn("appImage")

    val os = System.getProperty("os.name").lowercase()

    doFirst { delete("build/installer") }

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

            file("build/package/Thermals/Thermals.desktop").writeText(
                """
                [Desktop Entry]
                Name=Thermals
                Exec=Thermals
                Icon=Thermals
                Type=Application
                Categories=System;Monitor;
                """.trimIndent()
            )

            // Force the JetBrains Wayland toolkit for the packaged launcher.
            val launcher = file("build/package/Thermals/bin/Thermals")
            launcher.writeText(
                launcher.readText().replace(
                    "exec \"\$JAVA_BIN\"",
                    "exec \"\$JAVA_BIN\" -Dawt.toolkit.name=WLToolkit"
                )
            )
        }

        commandLine(
            "appimagetool",
            "build/package/Thermals",
            "build/installer/Thermals.AppImage"
        )
    }
}
