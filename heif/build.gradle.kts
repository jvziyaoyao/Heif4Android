plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

val projectDir: String = getProjectDir().absolutePath
val rootPath: String = file("$projectDir/libs/heif").path

val javaApiSourcePath: String = file("$rootPath/srcs/api-java/").path
val javaApiJavaPath: String = file("$javaApiSourcePath/java/").path
val javaApiNativePath: String = file("$javaApiSourcePath/cpp/").path

@Suppress("UnstableApiUsage")
android {
    namespace = "com.jvziyaoyao.heif"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        externalNativeBuild {
            cmake {
                targets("heifjni")
                abiFilters("armeabi-v7a", "arm64-v8a", "x86")
                arguments("-DANDROID_STL=c++_shared", "-DNO_TESTS=true")
                cppFlags("-std=c++11")
            }
        }
        ndkVersion = "21.4.7075529"
        ndk {
            abiFilters.add("armeabi-v7a")
            abiFilters.add("arm64-v8a")
            abiFilters.add("x86")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    externalNativeBuild {
        cmake {
            path("$javaApiNativePath/CMakeLists.txt")
        }
    }
    sourceSets.getByName("main") {
        java.srcDir(javaApiJavaPath)
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}