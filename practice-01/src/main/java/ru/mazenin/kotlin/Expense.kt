package ru.mazenin.kotlin

import java.math.BigDecimal
import java.time.LocalDate

data class Expense(val amount: BigDecimal, val category: String, val date: LocalDate) {
    init {
        require(amount > BigDecimal.ZERO) { "Сумма должна быть положительной" }
        require(category.isNotBlank()) { "Укажите категорию" }
    }

    fun displayInfo(): String = "$date · $category · ${amount.setScale(2)} ₽"
}

class ExpenseTracker(initial: List<Expense> = emptyList()) {
    private val expenses = initial.toMutableList()

    fun add(expense: Expense) {
        expenses.add(expense)
    }

    fun all(): List<Expense> = expenses.toList()

    fun displayAll(): String = expenses.joinToString("\n") { it.displayInfo() }

    fun totalsByCategory(): Map<String, BigDecimal> {
        val totals = linkedMapOf<String, BigDecimal>()
        for (expense in expenses) totals[expense.category] =
            (totals[expense.category] ?: BigDecimal.ZERO) + expense.amount
        return totals
    }
}
