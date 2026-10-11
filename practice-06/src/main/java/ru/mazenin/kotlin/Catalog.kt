package ru.mazenin.kotlin

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.lifecycle.*
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class Product(val id: Int, val title: String, val price: Double)

data class ProductsResponse(val products: List<Product>)

interface ProductsApi {
    @GET("products?limit=10") suspend fun products(): ProductsResponse
}

fun createApi(): ProductsApi =
    Retrofit.Builder()
        .baseUrl("https://dummyjson.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ProductsApi::class.java)

class CatalogDb(context: Context) : SQLiteOpenHelper(context, "catalog.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE products (id INTEGER PRIMARY KEY,title TEXT NOT NULL,price REAL NOT NULL)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, old: Int, new: Int) {}

    fun replace(items: List<Product>) {
        val db = writableDatabase
        db.beginTransaction()
        try {
            db.delete("products", null, null)
            items.forEach { p ->
                db.insertOrThrow(
                    "products",
                    null,
                    ContentValues().apply {
                        put("id", p.id)
                        put("title", p.title)
                        put("price", p.price)
                    },
                )
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    fun all(): List<Product> =
        readableDatabase.query("products", null, null, null, null, null, "id").use { c ->
            buildList {
                while (c.moveToNext()) add(Product(c.getInt(0), c.getString(1), c.getDouble(2)))
            }
        }
}

class CatalogRepository(private val api: ProductsApi, private val db: CatalogDb) {
    suspend fun refresh(): List<Product> =
        withContext(Dispatchers.IO) {
            val products = api.products().products
            db.replace(products)
            products
        }

    suspend fun local(): List<Product> = withContext(Dispatchers.IO) { db.all() }
}

data class CatalogState(
    val items: List<Product> = emptyList(),
    val message: String = "Нажмите «Загрузить из API»",
    val busy: Boolean = false,
)

class CatalogViewModel(private val repository: CatalogRepository) : ViewModel() {
    private val mutable = MutableLiveData(CatalogState())
    val state: LiveData<CatalogState> = mutable

    fun load(remote: Boolean) {
        if (mutable.value?.busy == true) return
        mutable.value = CatalogState(message = "Загрузка…", busy = true)
        viewModelScope.launch {
            try {
                val rows = if (remote) repository.refresh() else repository.local()
                mutable.value =
                    CatalogState(
                        rows,
                        if (remote) "Получено из API и записано в SQLite: ${rows.size}"
                        else "Прочитано из локальной базы: ${rows.size}",
                    )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                mutable.value =
                    CatalogState(
                        message =
                            "Не удалось загрузить данные. Проверьте интернет. Локальная база сохранена."
                    )
            }
        }
    }
}
