import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.attribute.PosixFilePermission

// Create a task to copy Git hooks from /scripts to .git/hooks path
val installGitHooks by tasks.creating(Copy::class) {
    from(layout.projectDirectory.file("scripts/pre-push"))
    val toDir = layout.projectDirectory.dir(".git/hooks")
    val toFile = toDir.file("pre-push").asFile
    val toFilePath = Paths.get(toFile.absolutePath)
    into(toDir)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    doLast {
        val perms = Files.getPosixFilePermissions(toFilePath)
        perms.add(PosixFilePermission.OWNER_EXECUTE)
        Files.setPosixFilePermissions(toFilePath, perms)
    }
}

// Register the Git task to run at beginning
tasks.getByPath(":app:preBuild").dependsOn(installGitHooks)
