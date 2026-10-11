package ru.mazenin.kotlin

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

fun Context.column(): LinearLayout =
    LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        setPadding(24, 16, 24, 16)
        setBackgroundColor(Color.rgb(245, 248, 246))
    }

fun LinearLayout.label(value: String, size: Float = 18f): TextView =
    TextView(context).also {
        it.text = value
        it.textSize = size
        it.setTextColor(Color.rgb(22, 47, 42))
        it.setPadding(0, 12, 0, 12)
        addView(it)
    }

fun LinearLayout.button(value: String, action: () -> Unit): Button =
    Button(context).also {
        it.text = value
        it.isAllCaps = false
        addView(it, LinearLayout.LayoutParams(-1, -2))
        it.setOnClickListener { action() }
    }

fun LinearLayout.input(hintText: String, value: String = ""): EditText =
    EditText(context).also {
        it.hint = hintText
        it.setText(value)
        it.textSize = 16f
        it.isSingleLine = true
        addView(it, LinearLayout.LayoutParams(-1, -2))
    }

fun AppCompatActivity.showRoot(root: View) {
    setContentView(root)
    ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->
        val b = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        v.setPadding(24, b.top + 16, 24, b.bottom + 16)
        insets
    }
}
