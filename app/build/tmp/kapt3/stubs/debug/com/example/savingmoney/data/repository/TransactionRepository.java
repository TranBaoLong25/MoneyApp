package com.example.savingmoney.data.repository;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u000b\u001a\u00020\b2\u0006\u0010\f\u001a\u00020\rH\u0086@\u00a2\u0006\u0002\u0010\u000eJ\u0016\u0010\u000f\u001a\u00020\u00102\u0006\u0010\f\u001a\u00020\rH\u0086@\u00a2\u0006\u0002\u0010\u000eJ&\u0010\u0011\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\u00130\u00122\b\u0010\u0014\u001a\u0004\u0018\u00010\u00152\b\u0010\u0016\u001a\u0004\u0018\u00010\u0017J\u001c\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00190\u00122\u0006\u0010\u001a\u001a\u00020\b2\u0006\u0010\u001b\u001a\u00020\bJ\"\u0010\u001c\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001d0\u00130\u00122\u0006\u0010\u001a\u001a\u00020\b2\u0006\u0010\u001b\u001a\u00020\bJ\u0012\u0010\u001e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\u00130\u0012J\u0018\u0010\u001f\u001a\u0004\u0018\u00010\r2\u0006\u0010 \u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010!J\u0016\u0010\"\u001a\u00020\u00102\u0006\u0010\f\u001a\u00020\rH\u0086@\u00a2\u0006\u0002\u0010\u000eR\u0014\u0010\u0007\u001a\u00020\b8BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\nR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006#"}, d2 = {"Lcom/example/savingmoney/data/repository/TransactionRepository;", "", "transactionDao", "Lcom/example/savingmoney/data/local/dao/TransactionDao;", "userRepository", "Lcom/example/savingmoney/data/repository/UserRepository;", "(Lcom/example/savingmoney/data/local/dao/TransactionDao;Lcom/example/savingmoney/data/repository/UserRepository;)V", "currentUserId", "", "getCurrentUserId", "()J", "addTransaction", "transaction", "Lcom/example/savingmoney/data/model/Transaction;", "(Lcom/example/savingmoney/data/model/Transaction;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteTransaction", "", "getFilteredTransactions", "Lkotlinx/coroutines/flow/Flow;", "", "type", "Lcom/example/savingmoney/data/model/TransactionType;", "categoryName", "", "getIncomeExpenseSummary", "Lcom/example/savingmoney/data/local/dao/IncomeExpenseSummary;", "startDate", "endDate", "getMonthlyExpenseStats", "Lcom/example/savingmoney/data/model/CategoryStatistic;", "getRecentTransactions", "getTransactionById", "transactionId", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateTransaction", "app_debug"})
public final class TransactionRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.example.savingmoney.data.local.dao.TransactionDao transactionDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.savingmoney.data.repository.UserRepository userRepository = null;
    
    @javax.inject.Inject()
    public TransactionRepository(@org.jetbrains.annotations.NotNull()
    com.example.savingmoney.data.local.dao.TransactionDao transactionDao, @org.jetbrains.annotations.NotNull()
    com.example.savingmoney.data.repository.UserRepository userRepository) {
        super();
    }
    
    private final long getCurrentUserId() {
        return 0L;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object addTransaction(@org.jetbrains.annotations.NotNull()
    com.example.savingmoney.data.model.Transaction transaction, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object updateTransaction(@org.jetbrains.annotations.NotNull()
    com.example.savingmoney.data.model.Transaction transaction, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object deleteTransaction(@org.jetbrains.annotations.NotNull()
    com.example.savingmoney.data.model.Transaction transaction, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.example.savingmoney.data.model.Transaction>> getRecentTransactions() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getTransactionById(long transactionId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.savingmoney.data.model.Transaction> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.example.savingmoney.data.model.Transaction>> getFilteredTransactions(@org.jetbrains.annotations.Nullable()
    com.example.savingmoney.data.model.TransactionType type, @org.jetbrains.annotations.Nullable()
    java.lang.String categoryName) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.example.savingmoney.data.local.dao.IncomeExpenseSummary> getIncomeExpenseSummary(long startDate, long endDate) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.example.savingmoney.data.model.CategoryStatistic>> getMonthlyExpenseStats(long startDate, long endDate) {
        return null;
    }
}