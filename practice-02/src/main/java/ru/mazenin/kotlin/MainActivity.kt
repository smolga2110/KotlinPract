package ru.mazenin.kotlin

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment

class MainActivity : AppCompatActivity() {
    var useNavigation = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        useNavigation = savedInstanceState?.getBoolean("navigation") ?: false
        val page = column()
        page.label("Три экрана", 26f)
        val switch =
            Switch(this).apply {
                text = "Navigation API (выкл. — транзакции)"
                isChecked = useNavigation
            }
        page.addView(switch)
        page.addView(
            FrameLayout(this).apply { id = R.id.fragment_host },
            LinearLayout.LayoutParams(-1, 0, 1f),
        )
        showRoot(page)
        switch.setOnCheckedChangeListener { _, checked ->
            useNavigation = checked
            reset()
        }
        if (savedInstanceState == null) reset()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean("navigation", useNavigation)
        super.onSaveInstanceState(outState)
    }

    private fun reset() {
        supportFragmentManager.popBackStackImmediate(
            null,
            androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE,
        )
        val fragment: Fragment =
            if (useNavigation) NavHostFragment.create(R.navigation.main_graph) else HomeFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_host, fragment)
            .setPrimaryNavigationFragment(if (useNavigation) fragment else null)
            .commit()
    }

    fun go(id: Int) {
        if (useNavigation)
            (supportFragmentManager.findFragmentById(R.id.fragment_host) as NavHostFragment)
                .navController
                .navigate(id)
        else {
            val target =
                when (id) {
                    R.id.screen_counter -> CounterFragment()
                    R.id.screen_note -> NoteFragment()
                    else -> HomeFragment()
                }
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_host, target)
                .addToBackStack(null)
                .commit()
        }
    }

    fun back() {
        onBackPressedDispatcher.onBackPressed()
    }
}
