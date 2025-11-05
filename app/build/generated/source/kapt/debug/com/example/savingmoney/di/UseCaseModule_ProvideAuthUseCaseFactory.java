package com.example.savingmoney.di;

import com.example.savingmoney.data.repository.UserRepository;
import com.example.savingmoney.domain.usecase.AuthUseCase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
    "KotlinInternalInJava"
})
public final class UseCaseModule_ProvideAuthUseCaseFactory implements Factory<AuthUseCase> {
  private final Provider<UserRepository> userRepositoryProvider;

  public UseCaseModule_ProvideAuthUseCaseFactory(Provider<UserRepository> userRepositoryProvider) {
    this.userRepositoryProvider = userRepositoryProvider;
  }

  @Override
  public AuthUseCase get() {
    return provideAuthUseCase(userRepositoryProvider.get());
  }

  public static UseCaseModule_ProvideAuthUseCaseFactory create(
      Provider<UserRepository> userRepositoryProvider) {
    return new UseCaseModule_ProvideAuthUseCaseFactory(userRepositoryProvider);
  }

  public static AuthUseCase provideAuthUseCase(UserRepository userRepository) {
    return Preconditions.checkNotNullFromProvides(UseCaseModule.INSTANCE.provideAuthUseCase(userRepository));
  }
}
