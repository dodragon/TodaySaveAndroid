pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://devrepo.kakao.com/nexus/content/groups/public/") }
        maven { setUrl("https://jitpack.io") }
    }
}

rootProject.name = "TodaySave"
include(":app")
include(":core:common")
include(":core:common-android")
include(":data:source")
include(":data:domain")
include(":data:dto")
include(":data:model")
include(":core:resources")
include(":core:view")
