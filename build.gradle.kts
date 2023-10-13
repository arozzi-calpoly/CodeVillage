plugins {
    id("java")
}

group = "org.codevillage"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}


dependencies {
    implementation("java3d:j3d-core:1.3.1")

    implementation(fileTree(mapOf("dir" to "jar/", "include" to listOf("*.jar"))))
    runtimeOnly(fileTree(mapOf("dir" to "jar/", "include" to listOf("*.jar"))))

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}