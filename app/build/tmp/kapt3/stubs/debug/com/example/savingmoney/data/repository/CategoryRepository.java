package com.example.savingmoney.data.repository;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0086@\u00a2\u0006\u0002\u0010\u000fJ\u0018\u0010\u0010\u001a\u0004\u0018\u00010\u000e2\u0006\u0010\u0011\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\u0012J\u0012\u0010\u0013\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\u00150\u0014J\u0012\u0010\u0016\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\u00150\u0014J\u0016\u0010\u0017\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0086@\u00a2\u0006\u0002\u0010\u000fJ\u0016\u0010\u0018\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0086@\u00a2\u0006\u0002\u0010\u000fR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\u00020\b8BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\nR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0019"}, d2 = {"Lcom/example/savingmoney/data/repository/CategoryRepository;", "", "categoryDao", "Lcom/example/savingmoney/data/local/dao/CategoryDao;", "userRepository", "Lcom/example/savingmoney/data/repository/UserRepository;", "(Lcom/example/savingmoney/data/local/dao/CategoryDao;Lcom/example/savingmoney/data/repository/UserRepository;)V", "currentUserId", "", "getCurrentUserId", "()J", "deleteCategory", "", "category", "Lcom/example/savingmoney/data/model/Category;", "(Lcom/example/savingmoney/data/model/Category;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getCategoryById", "categoryId", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getExpenseCategories", "Lkotlinx/coroutines/flow/Flow;", "", "getIncomeCategories", "saveCategory", "updateCategory", "app_debug"})
public final class CategoryRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.example.savingmoney.data.local.dao.CategoryDao categoryDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.savingmoney.data.repository.UserRepository userRepository = null;
    
    @javax.inject.Inject()
    public CategoryRepository(@org.jetbrains.annotations.NotNull()
    com.example.savingmoney.data.local.dao.CategoryDao categoryDao, @org.jetbrains.annotations.NotNull()
    com.example.savingmoney.data.repository.UserRepository userRepository) {
        super();
    }
    
    private final long getCurrentUserId() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.example.savingmoney.data.model.Category>> getIncomeCategories() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.example.savingmoney.data.model.Category>> getExpenseCategories() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object saveCategory(@org.jetbrains.annotations.NotNull()
    com.example.savingmoney.data.model.Category category, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object updateCategory(@org.jetbrains.annotations.NotNull()
    com.example.savingmoney.data.model.Category category, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object deleteCategory(@org.jetbrains.annotations.NotNull()
    com.example.savingmoney.data.model.Category category, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getCategoryById(long categoryId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.savingmoney.data.model.Category> $completion) {
        return null;
    }
}