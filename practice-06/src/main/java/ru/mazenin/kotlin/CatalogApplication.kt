package ru.mazenin.kotlin

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val catalogModule = module {
    single { createApi() }
    single { CatalogDb(get()) }
    single { CatalogRepository(get(), get()) }
    viewModel { CatalogViewModel(get()) }
}

class CatalogApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CatalogApplication)
            modules(catalogModule)
        }
    }
}
