package ru.mazenin.kotlin

import java.nio.file.Files
import java.util.concurrent.*
import org.junit.Assert.*
import org.junit.Test

class ImageDownloaderTest {
    @Test
    fun validUrlIsTrimmed() {
        assertEquals(
            "https://example.com/a.png",
            ImagePolicy.validate(" https://example.com/a.png "),
        )
    }

    @Test
    fun invalidSchemesAreRejected() {
        listOf("", "file:///a.png", "ftp://host/a", "https:///a").forEach {
            assertTrue(runCatching { ImagePolicy.validate(it) }.isFailure)
        }
    }

    @Test
    fun pngAndJpegRecognized() {
        assertEquals(
            "image_7.png",
            ImagePolicy.fileName(byteArrayOf(-119, 80, 78, 71, 13, 10, 26, 10), 7),
        )
        assertEquals("image_8.jpg", ImagePolicy.fileName(byteArrayOf(-1, -40, -1), 8))
    }

    @Test
    fun htmlIsRejected() {
        assertTrue(
            runCatching { ImagePolicy.fileName("<html>error</html>".toByteArray(), 1) }.isFailure
        )
    }

    @Test
    fun downloadUsesTwoThreadsAndSavesExactBytes() {
        val dir = Files.createTempDirectory("image-test").toFile()
        val bytes = byteArrayOf(-119, 80, 78, 71, 13, 10, 26, 10, 1, 2, 3)
        val downloader = ImageDownloader(dir) { bytes }
        val latch = CountDownLatch(1)
        var outcome: Result<DownloadResult>? = null
        try {
            downloader.download("https://example.com/a.png") {
                outcome = it
                latch.countDown()
            }
            assertTrue(latch.await(5, TimeUnit.SECONDS))
            val result = outcome!!.getOrThrow()
            assertArrayEquals(bytes, result.file.readBytes())
            assertEquals("Network", result.networkThread)
            assertEquals("Disk", result.diskThread)
        } finally {
            downloader.close()
            dir.deleteRecursively()
        }
    }
}
