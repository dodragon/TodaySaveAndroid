import org.gradle.api.JavaVersion
import org.gradle.api.Project
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.Properties
import java.util.concurrent.TimeUnit

fun Project.getProperties(
    fileName: String
): Properties {
    val properties = Properties()
    val localProperties = File(rootDir, fileName)

    if (localProperties.isFile) {
        InputStreamReader(FileInputStream(localProperties), Charsets.UTF_8).use { reader ->
            properties.load(reader)
        }
    }

    return properties
}

fun Project.getGitBranch(): String {
    try {
        val process = ProcessBuilder("git", "rev-parse", "--abbrev-ref", "HEAD")
            .directory(rootDir)
            .redirectErrorStream(true)
            .start()
        val exited = process.waitFor(5, TimeUnit.SECONDS)
        if (!exited) {
            process.destroyForcibly()
            project.logger.warn("Git command to get branch timed out.")
            return "timeout"
        }
        val output = process.inputStream.bufferedReader().readText().trim()
        if (process.exitValue() != 0) {
            project.logger.warn("Git command failed with exit code ${process.exitValue()}: $output")
            return "git-error"
        }
        return if (output.isNotEmpty() && output != "HEAD") {
            output.replace(Regex("[^a-zA-Z0-9.-]"), "_")
        } else {
            if (project.hasProperty("gitBranch")) {
                project.property("gitBranch").toString()
            } else {
                "detached_or_unknown"
            }
        }
    } catch (e: Exception) {
        project.logger.warn("Could not get git branch: ${e.message}")
        return "exception"
    }
}

fun Project.setCustomArchiveName(
    appName: String,
    versionCode: Int?,
    versionName: String?
) {
    setProperty("archivesBaseName", "${appName}_v${versionCode}(${versionName})_git(${getGitBranch()})")
}

private val Project.versionProperties: Properties
    get() = this.getProperties("version.properties")

val Project.targetSdkVersion: Int
    get() = versionProperties.getProperty("TARGET_SDK").toInt()

val Project.minSdkVersion: Int
    get() = versionProperties.getProperty("MIN_SDK").toInt()

val Project.todaySaveVersionCode: Int
    get() = versionProperties.getProperty("TODAY_SAVE_VERSION_CODE").toInt()

val Project.todaySaveVersionName: String
    get() = versionProperties.getProperty("TODAY_SAVE_VERSION_NAME").replace("\"", "")

