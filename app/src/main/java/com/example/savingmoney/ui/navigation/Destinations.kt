package com.example.savingmoney.ui.navigation

object Destinations {
    const val Welcome = "welcome"
    const val Login = "login"
    const val Register = "register"
    const val Home = "home"
    const val TransactionList = "transaction_list"
    const val AddTransaction = "add_transaction"
    const val Stats = "stats" // Thêm đích đến cho màn hình thống kê
    const val Settings = "settings"
    const val Profile = "profile"
    const val Faq = "faq" // ✅ Thêm đích đến mới
    // --- Thêm mới ---
    const val EditPlan = "edit_plan/{planId}"

    const val Planning = "planning_list"
    const val AddPlan = "add_plan"
    const val PlanDetail = "plan_detail/{planId}"  // <- phải có {planId}
 }
