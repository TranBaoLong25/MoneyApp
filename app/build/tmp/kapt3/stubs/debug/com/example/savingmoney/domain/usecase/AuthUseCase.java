package com.example.savingmoney.domain.usecase;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\u0005\u001a\u00020\u0006J,\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000bH\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\r\u0010\u000eJ\u0006\u0010\u000f\u001a\u00020\u0010J,\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\t0\b2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000bH\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u0012\u0010\u000eR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006\u0013"}, d2 = {"Lcom/example/savingmoney/domain/usecase/AuthUseCase;", "", "userRepository", "Lcom/example/savingmoney/data/repository/UserRepository;", "(Lcom/example/savingmoney/data/repository/UserRepository;)V", "isLoggedIn", "", "login", "Lkotlin/Result;", "Lcom/example/savingmoney/data/model/User;", "username", "", "passwordHash", "login-0E7RQCE", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "logout", "", "register", "register-0E7RQCE", "app_debug"})
public final class AuthUseCase {
    @org.jetbrains.annotations.NotNull()
    private final com.example.savingmoney.data.repository.UserRepository userRepository = null;
    
    @javax.inject.Inject()
    public AuthUseCase(@org.jetbrains.annotations.NotNull()
    com.example.savingmoney.data.repository.UserRepository userRepository) {
        super();
    }
    
    public final boolean isLoggedIn() {
        return false;
    }
    
    public final void logout() {
    }
}