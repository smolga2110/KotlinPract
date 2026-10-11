@file:Suppress("DEPRECATION")

package ru.mazenin.kotlin

import android.Manifest
import android.content.pm.PackageManager
import android.hardware.Camera
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.*
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity(), SurfaceHolder.Callback {
    private var camera: Camera? = null
    private var holder: SurfaceHolder? = null
    private var cameraScreen = true
    private lateinit var content: FrameLayout
    private lateinit var status: TextView
    private lateinit var capture: Button
    private val photos: File
        get() = File(filesDir, "photos").apply { mkdirs() }

    override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        val page = column()
        page.label("Фотожурнал", 26f)
        val tabs = LinearLayout(this)
        page.addView(tabs)
        tabs.button("Камера") { showCamera() }.layoutParams = LinearLayout.LayoutParams(0, -2, 1f)
        tabs.button("Список") { showList() }.layoutParams = LinearLayout.LayoutParams(0, -2, 1f)
        content = FrameLayout(this)
        page.addView(content, LinearLayout.LayoutParams(-1, 0, 1f))
        showRoot(page)
        if (state?.getBoolean("list") == true) showList() else showCamera()
    }

    override fun onSaveInstanceState(out: Bundle) {
        out.putBoolean("list", !cameraScreen)
        super.onSaveInstanceState(out)
    }

    private fun showCamera() {
        release()
        holder = null
        cameraScreen = true
        content.removeAllViews()
        val page = column()
        page.label("Камера", 22f)
        val preview = SurfaceView(this)
        page.addView(preview, LinearLayout.LayoutParams(-1, 0, 1f))
        preview.holder.addCallback(this)
        status = page.label("Разрешите доступ к камере", 15f)
        capture =
            page.button("Сфотографировать") {
                val current = camera ?: return@button
                capture.isEnabled = false
                try {
                    current.takePicture(null, null) { bytes, cam ->
                        try {
                            val now = LocalDateTime.now()
                            File(photos, "${System.currentTimeMillis()}.jpg").writeBytes(bytes)
                            File(photos, "date")
                                .appendText(
                                    now.format(
                                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
                                    ) + "\n"
                                )
                            status.text = "Снимок и дата сохранены"
                        } catch (e: Exception) {
                            status.text = "Ошибка записи: ${e.message}"
                        } finally {
                            runCatching { cam.startPreview() }
                            capture.isEnabled = true
                        }
                    }
                } catch (e: Exception) {
                    status.text = "Ошибка камеры: ${e.message}"
                    capture.isEnabled = true
                }
            }
        content.addView(page)
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(arrayOf(Manifest.permission.CAMERA), 42)
    }

    private fun showList() {
        cameraScreen = false
        release()
        holder = null
        content.removeAllViews()
        val page = column()
        page.label("Список", 22f)
        val rows =
            File(photos, "date")
                .takeIf { it.exists() }
                ?.readLines()
                ?.filter { it.isNotBlank() }
                ?.sorted() ?: emptyList()
        page.label("Снимков: ${rows.size} · сначала ранние", 15f)
        val recycler = RecyclerView(this)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = DatesAdapter(rows)
        page.addView(recycler, LinearLayout.LayoutParams(-1, 0, 1f))
        content.addView(page)
    }

    private fun open() {
        if (
            !cameraScreen ||
                holder == null ||
                camera != null ||
                checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
        )
            return
        try {
            camera =
                Camera.open().also {
                    it.setDisplayOrientation(90)
                    it.setPreviewDisplay(holder)
                    it.startPreview()
                }
            status.text = "Камера готова"
        } catch (e: Exception) {
            release()
            status.text = "Камера недоступна: ${e.message}"
        }
    }

    private fun release() {
        runCatching { camera?.stopPreview() }
        camera?.release()
        camera = null
    }

    override fun surfaceCreated(h: SurfaceHolder) {
        holder = h
        open()
    }

    override fun surfaceChanged(h: SurfaceHolder, format: Int, width: Int, height: Int) {}

    override fun surfaceDestroyed(h: SurfaceHolder) {
        release()
        holder = null
    }

    override fun onResume() {
        super.onResume()
        open()
    }

    override fun onPause() {
        release()
        super.onPause()
    }

    override fun onRequestPermissionsResult(
        code: Int,
        permissions: Array<out String>,
        results: IntArray,
    ) {
        super.onRequestPermissionsResult(code, permissions, results)
        if (code == 42) {
            if (results.firstOrNull() == PackageManager.PERMISSION_GRANTED) open()
            else status.text = "Доступ запрещён. Разрешите камеру в настройках."
        }
    }
}

class DatesAdapter(private val rows: List<String>) : RecyclerView.Adapter<DatesAdapter.Holder>() {
    class Holder(val text: TextView) : RecyclerView.ViewHolder(text)

    override fun onCreateViewHolder(parent: ViewGroup, type: Int) =
        Holder(
            TextView(parent.context).apply {
                textSize = 18f
                setPadding(12, 22, 12, 22)
            }
        )

    override fun getItemCount() = rows.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.text.text = "${position+1}. ${rows[position]}"
    }
}
