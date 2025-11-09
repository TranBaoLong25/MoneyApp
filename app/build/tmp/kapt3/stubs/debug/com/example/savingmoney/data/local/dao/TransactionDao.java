package com.example.savingmoney.data.local.dao;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\bg\u0018\u00002\u00020\u0001J\u001c\u0010\u0002\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u00032\u0006\u0010\u0006\u001a\u00020\u0007H\'J:\u0010\b\u001a\b\u0012\u0004\u0012\u00020\t0\u00032\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\u00072\b\b\u0002\u0010\f\u001a\u00020\r2\b\b\u0002\u0010\u000e\u001a\u00020\rH\'J6\u0010\u000f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\u00040\u00032\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\u00072\b\b\u0002\u0010\u000e\u001a\u00020\rH\'J\u0016\u0010\u0011\u001a\u00020\u00072\u0006\u0010\u0012\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0013\u00a8\u0006\u0014"}, d2 = {"Lcom/example/savingmoney/data/local/dao/TransactionDao;", "", "getAllTransactions", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/example/savingmoney/data/model/Transaction;", "userId", "", "getIncomeExpenseSummary", "Lcom/example/savingmoney/data/local/dao/IncomeExpenseSummary;", "startDate", "endDate", "incomeType", "Lcom/example/savingmoney/data/model/TransactionType;", "expenseType", "getMonthlyExpenseStats", "Lcom/example/savingmoney/data/model/CategoryStatistic;", "insertTransaction", "transaction", "(Lcom/example/savingmoney/data/model/Transaction;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
@androidx.room.Dao()
public abstract interface TransactionDao {
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertTransaction(@org.jetbrains.annotations.NotNull()
    com.example.savingmoney.data.model.Transaction transaction, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM transaction_table WHERE userId = :userId ORDER BY date DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.example.savingmoney.data.model.Transaction>> getAllTransactions(long userId);
    
    @androidx.room.Query(value = "\n        SELECT SUM(CASE WHEN type = :incomeType THEN amount ELSE 0 END) AS totalIncome, \n               SUM(CASE WHEN type = :expenseType THEN amount ELSE 0 END) AS totalExpense\n        FROM transaction_table\n        WHERE userId = :userId AND date >= :startDate AND date <= :endDate\n    ")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<com.example.savingmoney.data.local.dao.IncomeExpenseSummary> getIncomeExpenseSummary(long userId, long startDate, long endDate, @org.jetbrains.annotations.NotNull()
    com.example.savingmoney.data.model.TransactionType incomeType, @org.jetbrains.annotations.NotNull()
    com.example.savingmoney.data.model.TransactionType expenseType);
    
    @androidx.room.Query(value = "\n        SELECT categoryName AS category, SUM(amount) AS amount\n        FROM transaction_table\n        WHERE userId = :userId AND type = :expenseType AND date >= :startDate AND date <= :endDate\n        GROUP BY categoryName\n        ORDER BY amount DESC\n    ")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.example.savingmoney.data.model.CategoryStatistic>> getMonthlyExpenseStats(long userId, long startDate, long endDate, @org.jetbrains.annotations.NotNull()
    com.example.savingmoney.data.model.TransactionType expenseType);
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 3, xi = 48)
    public static final class DefaultImpls {
    }
}