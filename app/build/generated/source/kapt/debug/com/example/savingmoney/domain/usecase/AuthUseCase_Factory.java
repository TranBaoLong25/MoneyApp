package com.example.savingmoney.domain.usecase;

import com.example.savingmoney.data.repository.UserRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
    "KotlinInternalInJava",
    "cast"
})
public final class AuthUseCase_Factory implements Factory<AuthUseCase> {
  private final Provider<UserRepository> userRepositoryProvider;

  public AuthUseCase_Factory(Provider<UserRepository> userRepositoryProvider) {
    this.userRepositoryProvider = userRepositoryProvider;
  }

  @Override
  public AuthUseCase get() {
    return newInstance(userRepositoryProvider.get());
  }

  public static AuthUseCase_Factory create(Provider<UserRepository> userRepositoryProvider) {
    return new AuthUseCase_Factory(userRepositoryProvider);
  }

  public static AuthUseCase newInstance(UserRepository userRepository) {
    return new AuthUseCase(userRepository);
  }
}
