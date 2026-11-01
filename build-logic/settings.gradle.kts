pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("androidx\\..*")
                includeGroupByRegex("com\\.android(\\..*)?")
                includeGroupByRegex("com\\.google\\..*")
            }
        }
        mavenCentral {
            content {
                excludeGroupByRegex("androidx\\..*")
                excludeGroupByRegex("com\\.android(\\..*)?")
            }
        }
        gradlePluginPortal {
            content {
                excludeGroupByRegex("androidx\\..*")
                excludeGroupByRegex("com\\.android(\\..*)?")
            }
        }
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google {
            content {
                includeGroupByRegex("androidx\\..*")
                includeGroupByRegex("com\\.android(\\..*)?")
                includeGroupByRegex("com\\.google\\..*")
            }
        }
        mavenCentral {
            content {
                excludeGroupByRegex("androidx\\..*")
                excludeGroupByRegex("com\\.android(\\..*)?")
            }
        }
    }
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "build-logic"
include(":convention")
