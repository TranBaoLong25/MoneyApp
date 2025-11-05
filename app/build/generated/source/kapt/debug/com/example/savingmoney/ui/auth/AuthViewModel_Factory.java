package com.example.savingmoney.ui.auth;

import com.example.savingmoney.domain.usecase.AuthUseCase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class AuthViewModel_Factory implements Factory<AuthViewModel> {
  private final Provider<AuthUseCase> authUseCaseProvider;

  public AuthViewModel_Factory(Provider<AuthUseCase> authUseCaseProvider) {
    this.authUseCaseProvider = authUseCaseProvider;
  }

  @Override
  public AuthViewModel get() {
    return newInstance(authUseCaseProvider.get());
  }

  public static AuthViewModel_Factory create(Provider<AuthUseCase> authUseCaseProvider) {
    return new AuthViewModel_Factory(authUseCaseProvider);
  }

  public static AuthViewModel newInstance(AuthUseCase authUseCase) {
    return new AuthViewModel(authUseCase);
  }
}
