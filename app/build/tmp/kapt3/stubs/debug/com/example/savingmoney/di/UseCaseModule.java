package com.example.savingmoney.di;

@dagger.Module()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0007J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0007J\u0010\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\u0011\u001a\u00020\u00122\u0006\u0010\r\u001a\u00020\u000eH\u0007J\u0010\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0018H\u0007J\u0010\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\r\u001a\u00020\u000eH\u0007J\u0010\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\r\u001a\u00020\u000eH\u0007\u00a8\u0006\u001d"}, d2 = {"Lcom/example/savingmoney/di/UseCaseModule;", "", "()V", "provideAddTransactionUseCase", "Lcom/example/savingmoney/domain/usecase/AddTransactionUseCase;", "transactionRepository", "Lcom/example/savingmoney/data/repository/TransactionRepository;", "provideAuthUseCase", "Lcom/example/savingmoney/domain/usecase/AuthUseCase;", "userRepository", "Lcom/example/savingmoney/data/repository/UserRepository;", "provideGetLanguageUseCase", "Lcom/example/savingmoney/domain/usecase/GetLanguageUseCase;", "repository", "Lcom/example/savingmoney/domain/repository/SettingsRepository;", "provideGetMonthlySummaryUseCase", "Lcom/example/savingmoney/domain/usecase/GetMonthlySummaryUseCase;", "provideGetThemeUseCase", "Lcom/example/savingmoney/domain/usecase/GetThemeUseCase;", "provideGetTransactionListUseCase", "Lcom/example/savingmoney/domain/usecase/GetTransactionListUseCase;", "provideSaveCategoryUseCase", "Lcom/example/savingmoney/domain/usecase/SaveCategoryUseCase;", "categoryRepository", "Lcom/example/savingmoney/data/repository/CategoryRepository;", "provideUpdateLanguageUseCase", "Lcom/example/savingmoney/domain/usecase/UpdateLanguageUseCase;", "provideUpdateThemeUseCase", "Lcom/example/savingmoney/domain/usecase/UpdateThemeUseCase;", "app_debug"})
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
public final class UseCaseModule {
    @org.jetbrains.annotations.NotNull()
    public static final com.example.savingmoney.di.UseCaseModule INSTANCE = null;
    
    private UseCaseModule() {
        super();
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.example.savingmoney.domain.usecase.AuthUseCase provideAuthUseCase(@org.jetbrains.annotations.NotNull()
    com.example.savingmoney.data.repository.UserRepository userRepository) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.example.savingmoney.domain.usecase.GetMonthlySummaryUseCase provideGetMonthlySummaryUseCase(@org.jetbrains.annotations.NotNull()
    com.example.savingmoney.data.repository.TransactionRepository transactionRepository) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.example.savingmoney.domain.usecase.AddTransactionUseCase provideAddTransactionUseCase(@org.jetbrains.annotations.NotNull()
    com.example.savingmoney.data.repository.TransactionRepository transactionRepository) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.example.savingmoney.domain.usecase.GetTransactionListUseCase provideGetTransactionListUseCase(@org.jetbrains.annotations.NotNull()
    com.example.savingmoney.data.repository.TransactionRepository transactionRepository) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.example.savingmoney.domain.usecase.SaveCategoryUseCase provideSaveCategoryUseCase(@org.jetbrains.annotations.NotNull()
    com.example.savingmoney.data.repository.CategoryRepository categoryRepository) {
        return null;
    }
    
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.example.savingmoney.domain.usecase.GetThemeUseCase provideGetThemeUseCase(@org.jetbrains.annotations.NotNull()
    com.example.savingmoney.domain.repository.SettingsRepository repository) {
        return null;
    }
    
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.example.savingmoney.domain.usecase.UpdateThemeUseCase provideUpdateThemeUseCase(@org.jetbrains.annotations.NotNull()
    com.example.savingmoney.domain.repository.SettingsRepository repository) {
        return null;
    }
    
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.example.savingmoney.domain.usecase.GetLanguageUseCase provideGetLanguageUseCase(@org.jetbrains.annotations.NotNull()
    com.example.savingmoney.domain.repository.SettingsRepository repository) {
        return null;
    }
    
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.example.savingmoney.domain.usecase.UpdateLanguageUseCase provideUpdateLanguageUseCase(@org.jetbrains.annotations.NotNull()
    com.example.savingmoney.domain.repository.SettingsRepository repository) {
        return null;
    }
}