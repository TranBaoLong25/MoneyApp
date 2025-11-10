package com.example.savingmoney.di;

@dagger.Module()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0007J\u0018\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0007\u001a\u00020\bH\u0007J\u0018\u0010\r\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0007\u00a8\u0006\u0012"}, d2 = {"Lcom/example/savingmoney/di/RepositoryModule;", "", "()V", "provideCategoryRepository", "Lcom/example/savingmoney/data/repository/CategoryRepository;", "categoryDao", "Lcom/example/savingmoney/data/local/dao/CategoryDao;", "userRepository", "Lcom/example/savingmoney/data/repository/UserRepository;", "provideTransactionRepository", "Lcom/example/savingmoney/data/repository/TransactionRepository;", "transactionDao", "Lcom/example/savingmoney/data/local/dao/TransactionDao;", "provideUserRepository", "userDao", "Lcom/example/savingmoney/data/local/dao/UserDao;", "userPreferences", "Lcom/example/savingmoney/data/preferences/UserPreferences;", "app_debug"})
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
public final class RepositoryModule {
    @org.jetbrains.annotations.NotNull()
    public static final com.example.savingmoney.di.RepositoryModule INSTANCE = null;
    
    private RepositoryModule() {
        super();
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.example.savingmoney.data.repository.UserRepository provideUserRepository(@org.jetbrains.annotations.NotNull()
    com.example.savingmoney.data.local.dao.UserDao userDao, @org.jetbrains.annotations.NotNull()
    com.example.savingmoney.data.preferences.UserPreferences userPreferences) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.example.savingmoney.data.repository.TransactionRepository provideTransactionRepository(@org.jetbrains.annotations.NotNull()
    com.example.savingmoney.data.local.dao.TransactionDao transactionDao, @org.jetbrains.annotations.NotNull()
    com.example.savingmoney.data.repository.UserRepository userRepository) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.example.savingmoney.data.repository.CategoryRepository provideCategoryRepository(@org.jetbrains.annotations.NotNull()
    com.example.savingmoney.data.local.dao.CategoryDao categoryDao, @org.jetbrains.annotations.NotNull()
    com.example.savingmoney.data.repository.UserRepository userRepository) {
        return null;
    }
}