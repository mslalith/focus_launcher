package dev.mslalith.focuslauncher.screens.developer.file

import android.content.Context
import android.net.Uri
import org.intellij.lang.annotations.Language
import java.io.FileInputStream

abstract class CacheFileInteractor<T>(
    private val context: Context
) {

    abstract val fileName: String

    protected abstract fun default(): T
    protected abstract fun T.toJson(): String
    protected abstract fun fromJson(@Language("json") json: String): T

    fun readContents(uri: Uri): T = try {
        val json = with(context) { uri.readContents() } ?: "{}"
        fromJson(json = json)
    } catch (ex: Exception) {
        ex.printStackTrace()
        default()
    }

    fun writeContents(uri: Uri, content: T): Boolean = try {
        with(context) { uri.saveContents(content.toJson()) }
        true
    } catch (ex: Exception) {
        ex.printStackTrace()
        false
    }
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
