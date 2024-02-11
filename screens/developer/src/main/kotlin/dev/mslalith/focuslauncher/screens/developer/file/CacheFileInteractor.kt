package dev.mslalith.focuslauncher.screens.developer.file

import android.content.Context
import android.net.Uri
import org.intellij.lang.annotations.Language
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException

abstract class CacheFileInteractor<T>(
    private val context: Context
) {

    abstract val fileName: String

    protected abstract fun default(): T
    protected abstract fun T.toJson(): String
    protected abstract fun fromJson(@Language("json") json: String): T

    fun readContents(uri: Uri): T = safeIO(default = default()) {
        val json = with(context) { uri.readContents() } ?: "{}"
        fromJson(json = json)
    }

    fun writeContents(uri: Uri, content: T): Boolean = safeIO(default = false) {
        with(context) { uri.saveContents(content.toJson()) }
        true
    }
}

private fun <T> safeIO(
    default: T,
    block: () -> T
): T = try {
    block()
} catch (ex: FileNotFoundException) {
    // ex.printStackTrace()
    default
} catch (ex: IOException) {
    // ex.printStackTrace()
    default
}

context (Context)
private fun Uri.readContents(): String? = contentResolver.openFileDescriptor(this, "r")
    ?.use { fileDescriptor ->
        FileInputStream(fileDescriptor.fileDescriptor).use {
            it.bufferedReader().readText()
        }
    }

context (Context)
private fun Uri.saveContents(content: String) {
    contentResolver.openOutputStream(this)?.use {
        it.write(content.toByteArray())
    }
}
