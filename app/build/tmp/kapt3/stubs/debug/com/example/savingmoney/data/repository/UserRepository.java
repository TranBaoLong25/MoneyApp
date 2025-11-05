package com.example.savingmoney.data.repository;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0006\u0010\u0007\u001a\u00020\bJ\u0018\u0010\t\u001a\u0004\u0018\u00010\n2\u0006\u0010\u000b\u001a\u00020\fH\u0086@\u00a2\u0006\u0002\u0010\rJ\u0006\u0010\u000e\u001a\u00020\u000fJ\u0006\u0010\u0010\u001a\u00020\u0011J\u0016\u0010\u0012\u001a\u00020\b2\u0006\u0010\u0013\u001a\u00020\nH\u0086@\u00a2\u0006\u0002\u0010\u0014J\u000e\u0010\u0015\u001a\u00020\u00112\u0006\u0010\u0016\u001a\u00020\bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2 = {"Lcom/example/savingmoney/data/repository/UserRepository;", "", "userDao", "Lcom/example/savingmoney/data/local/dao/UserDao;", "userPreferences", "Lcom/example/savingmoney/data/preferences/UserPreferences;", "(Lcom/example/savingmoney/data/local/dao/UserDao;Lcom/example/savingmoney/data/preferences/UserPreferences;)V", "getCurrentUserId", "", "getUserByUsername", "Lcom/example/savingmoney/data/model/User;", "username", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "isLoggedIn", "", "logout", "", "registerUser", "user", "(Lcom/example/savingmoney/data/model/User;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "saveLoginState", "userId", "app_debug"})
public final class UserRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.example.savingmoney.data.local.dao.UserDao userDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.savingmoney.data.preferences.UserPreferences userPreferences = null;
    
    @javax.inject.Inject()
    public UserRepository(@org.jetbrains.annotations.NotNull()
    com.example.savingmoney.data.local.dao.UserDao userDao, @org.jetbrains.annotations.NotNull()
    com.example.savingmoney.data.preferences.UserPreferences userPreferences) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object registerUser(@org.jetbrains.annotations.NotNull()
    com.example.savingmoney.data.model.User user, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getUserByUsername(@org.jetbrains.annotations.NotNull()
    java.lang.String username, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.savingmoney.data.model.User> $completion) {
        return null;
    }
    
    public final void saveLoginState(long userId) {
    }
    
    public final boolean isLoggedIn() {
        return false;
    }
    
    public final void logout() {
    }
    
    public final long getCurrentUserId() {
        return 0L;
    }
}