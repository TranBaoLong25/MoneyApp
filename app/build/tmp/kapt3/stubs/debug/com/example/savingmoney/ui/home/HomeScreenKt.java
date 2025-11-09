package com.example.savingmoney.ui.home;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000H\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a0\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\u0005\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\u0003H\u0007\u001a2\u0010\b\u001a\u00020\u00012\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\rH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u000f\u0010\u0010\u001a\u0018\u0010\u0011\u001a\u00020\u00012\u0006\u0010\u0012\u001a\u00020\n2\u0006\u0010\u0013\u001a\u00020\nH\u0007\u001a&\u0010\u0014\u001a\u00020\u00012\u0012\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00010\u00162\b\b\u0002\u0010\u0017\u001a\u00020\u0018H\u0007\u001a$\u0010\u0019\u001a\u00020\u00012\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u001c0\u001b2\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u00010\u001eH\u0007\u001a\u0016\u0010\u001f\u001a\u00020\u00012\f\u0010 \u001a\b\u0012\u0004\u0012\u00020!0\u001bH\u0007\u001a\u0010\u0010\"\u001a\u00020\u00012\u0006\u0010#\u001a\u00020\u001cH\u0007\u0082\u0002\u0007\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006$"}, d2 = {"BalanceCard", "", "balance", "", "income", "expense", "monthlyIncome", "monthlyExpense", "FlowItem", "label", "", "amount", "color", "Landroidx/compose/ui/graphics/Color;", "indicatorColor", "FlowItem-0YGnOg8", "(Ljava/lang/String;DJJ)V", "HeaderSection", "userName", "currentMonth", "HomeScreen", "onNavigateTo", "Lkotlin/Function1;", "viewModel", "Lcom/example/savingmoney/ui/home/HomeViewModel;", "RecentTransactionsSection", "transactions", "", "Lcom/example/savingmoney/data/model/Transaction;", "onViewAll", "Lkotlin/Function0;", "StatsSection", "stats", "Lcom/example/savingmoney/data/model/CategoryStatistic;", "TransactionRow", "tx", "app_debug"})
public final class HomeScreenKt {
    
    @androidx.compose.runtime.Composable()
    public static final void HomeScreen(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onNavigateTo, @org.jetbrains.annotations.NotNull()
    com.example.savingmoney.ui.home.HomeViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void HeaderSection(@org.jetbrains.annotations.NotNull()
    java.lang.String userName, @org.jetbrains.annotations.NotNull()
    java.lang.String currentMonth) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void BalanceCard(double balance, double income, double expense, double monthlyIncome, double monthlyExpense) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void StatsSection(@org.jetbrains.annotations.NotNull()
    java.util.List<com.example.savingmoney.data.model.CategoryStatistic> stats) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void RecentTransactionsSection(@org.jetbrains.annotations.NotNull()
    java.util.List<com.example.savingmoney.data.model.Transaction> transactions, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onViewAll) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void TransactionRow(@org.jetbrains.annotations.NotNull()
    com.example.savingmoney.data.model.Transaction tx) {
    }
}