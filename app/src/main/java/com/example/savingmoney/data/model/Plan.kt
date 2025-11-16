package com.example.savingmoney.data.model

data class Plan(
    var id: String = "",
    var title: String = "",
    var description: String = "",
    var budgetAmount: Double = 0.0,
    var categoryBudgets: Map<String, Double> = emptyMap(),
    var usedAmount: Double = 0.0
)
