package com.example.savingmoney.data.local.dao;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u001c\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\t0\b2\u0006\u0010\n\u001a\u00020\u000bH\'J0\u0010\f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\t0\b2\u0006\u0010\n\u001a\u00020\u000b2\b\u0010\r\u001a\u0004\u0018\u00010\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\'J:\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00120\b2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0013\u001a\u00020\u000b2\u0006\u0010\u0014\u001a\u00020\u000b2\b\b\u0002\u0010\u0015\u001a\u00020\u000e2\b\b\u0002\u0010\u0016\u001a\u00020\u000eH\'J6\u0010\u0017\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00180\t0\b2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0013\u001a\u00020\u000b2\u0006\u0010\u0014\u001a\u00020\u000b2\b\b\u0002\u0010\u0016\u001a\u00020\u000eH\'J \u0010\u0019\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u001a\u001a\u00020\u000b2\u0006\u0010\n\u001a\u00020\u000bH\u00a7@\u00a2\u0006\u0002\u0010\u001bJ\u0016\u0010\u001c\u001a\u00020\u000b2\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u001d\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006\u00a8\u0006\u001e"}, d2 = {"Lcom/example/savingmoney/data/local/dao/TransactionDao;", "", "deleteTransaction", "", "transaction", "Lcom/example/savingmoney/data/model/Transaction;", "(Lcom/example/savingmoney/data/model/Transaction;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllTransactions", "Lkotlinx/coroutines/flow/Flow;", "", "userId", "", "getFilteredTransactions", "type", "Lcom/example/savingmoney/data/model/TransactionType;", "categoryName", "", "getIncomeExpenseSummary", "Lcom/example/savingmoney/data/local/dao/IncomeExpenseSummary;", "startDate", "endDate", "incomeType", "expenseType", "getMonthlyExpenseStats", "Lcom/example/savingmoney/data/model/CategoryStatistic;", "getTransactionById", "transactionId", "(JJLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertTransaction", "updateTransaction", "app_debug"})
@androidx.room.Dao()
public abstract interface TransactionDao {
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertTransaction(@org.jetbrains.annotations.NotNull()
    com.example.savingmoney.data.model.Transaction transaction, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateTransaction(@org.jetbrains.annotations.NotNull()
    com.example.savingmoney.data.model.Transaction transaction, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Delete()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteTransaction(@org.jetbrains.annotations.NotNull()
    com.example.savingmoney.data.model.Transaction transaction, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM transaction_table WHERE userId = :userId ORDER BY date DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.example.savingmoney.data.model.Transaction>> getAllTransactions(long userId);
    
    @androidx.room.Query(value = "SELECT * FROM transaction_table WHERE id = :transactionId AND userId = :userId LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getTransactionById(long transactionId, long userId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.savingmoney.data.model.Transaction> $completion);
    
    @androidx.room.Query(value = "\n        SELECT * FROM transaction_table \n        WHERE userId = :userId \n        AND (:type IS NULL OR type = :type) \n        AND (:categoryName IS NULL OR categoryName = :categoryName)\n        ORDER BY date DESC\n    ")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.example.savingmoney.data.model.Transaction>> getFilteredTransactions(long userId, @org.jetbrains.annotations.Nullable()
    com.example.savingmoney.data.model.TransactionType type, @org.jetbrains.annotations.Nullable()
    java.lang.String categoryName);
    
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