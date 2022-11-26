val ktor_version = "2.1.3"
val kotlin_version = "1.7.21" // When updating also update kotlin plugins version
val kotlin_coroutine = "1.6.4"
val kotlinx_serialization_version = "1.4.0"

plugins {
    application
    kotlin("multiplatform") version "1.7.21"
    kotlin("plugin.serialization") version "1.7.21"
}

group = "com.lis16"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
}

kotlin {
    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" -> macosX64("native")
        hostOs == "Linux" -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    nativeTarget.apply {
        binaries {
            executable {
                entryPoint = "main"
            }
        }
    }
    sourceSets {
        val nativeMain by getting {
            dependencies {
                implementation("io.ktor:ktor-server-core:$ktor_version")
                implementation("io.ktor:ktor-server-cio:$ktor_version")

                implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlin_coroutine")

                // Fix to sync kotlinx serialization version
//                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$kotlinx_serialization_version") {
//                    version { strictly(kotlinx_serialization_version) }
//                }
//                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinx_serialization_version") {
//                    version { strictly(kotlinx_serialization_version) }
//                }

//                implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
//                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
//                implementation("io.ktor:ktor-server-status-pages:$ktor_version")
//                implementation("io.ktor:ktor-server-call-id:$ktor_version")
            }
        }
        val nativeTest by getting {
            dependencies {
                implementation("io.ktor:ktor-server-test-host:$ktor_version")
                implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
                implementation("org.jetbrains.kotlin:kotlin-test:$kotlin_version")
            }
        }
    }
}
