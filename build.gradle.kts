plugins {
    java
    application
    // id("org.javamodularity.moduleplugin") version "1.8.15"
    // id("org.openjfx.javafxplugin") version "0.0.13"
    id("org.beryx.jlink") version "2.25.0"
}

group = "org.blu3fishez"
version = "1.0.0"

repositories {
    mavenCentral()
}

val junitVersion = "5.12.1"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

application {
    mainModule.set("org.blu3fishez.colima_tray.app")
    mainClass.set("org.blu3fishez.colima_tray.ColimaGuiApplication")
}

//javafx {
//    version = "21.0.6"
//    modules = listOf("javafx.controls", "javafx.fxml")
//}

dependencies {
//    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")
//    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// build.gradle.kts 파일의 하단에 추가
tasks.jar {
    manifest {
        attributes["Main-Class"] = "org.blu3fishez.colima_tray.ColimaGuiApplication" // 본인의 main 클래스 경로
    }
}

jlink {

    imageZip.set(layout.buildDirectory.file("/distributions/app-image.zip"))
    options.set(listOf("--strip-debug", "--compress", "2", "--no-header-files", "--no-man-pages"))
    launcher {
        name = "ColimaTrayApp"
    }
}