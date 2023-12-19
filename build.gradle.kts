repositories {
    mavenCentral()
}

plugins {
    application
}

sourceSets {
    main {
        java {
            srcDir("src/main")
        }
    }
    test {
        java {
            srcDir("src/test")
        }
    }
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.1")
    testImplementation("org.assertj:assertj-core:3.24.2")
}

tasks.withType<Test> {
    testLogging {
        events("passed", "skipped", "failed")
    }
    useJUnitPlatform()
}

application {
    applicationDefaultJvmArgs = listOf("-XX:ActiveProcessorCount=4")
    mainClass.set("Benchmark")
}