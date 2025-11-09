package com.example.savingmoney.ui.auth;

import com.example.savingmoney.data.repository.UserRepository;
import com.google.firebase.auth.FirebaseAuth;
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
  private final Provider<FirebaseAuth> authProvider;

  private final Provider<UserRepository> userRepositoryProvider;

  public AuthViewModel_Factory(Provider<FirebaseAuth> authProvider,
      Provider<UserRepository> userRepositoryProvider) {
    this.authProvider = authProvider;
    this.userRepositoryProvider = userRepositoryProvider;
  }

  @Override
  public AuthViewModel get() {
    return newInstance(authProvider.get(), userRepositoryProvider.get());
  }

  public static AuthViewModel_Factory create(Provider<FirebaseAuth> authProvider,
      Provider<UserRepository> userRepositoryProvider) {
    return new AuthViewModel_Factory(authProvider, userRepositoryProvider);
  }

  public static AuthViewModel newInstance(FirebaseAuth auth, UserRepository userRepository) {
    return new AuthViewModel(auth, userRepository);
  }
}
