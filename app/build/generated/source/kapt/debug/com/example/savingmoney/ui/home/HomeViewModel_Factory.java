package com.example.savingmoney.ui.home;

import com.example.savingmoney.data.repository.TransactionRepository;
import com.example.savingmoney.data.repository.UserRepository;
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
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<TransactionRepository> transactionRepositoryProvider;

  private final Provider<UserRepository> userRepositoryProvider;

  public HomeViewModel_Factory(Provider<TransactionRepository> transactionRepositoryProvider,
      Provider<UserRepository> userRepositoryProvider) {
    this.transactionRepositoryProvider = transactionRepositoryProvider;
    this.userRepositoryProvider = userRepositoryProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(transactionRepositoryProvider.get(), userRepositoryProvider.get());
  }

  public static HomeViewModel_Factory create(
      Provider<TransactionRepository> transactionRepositoryProvider,
      Provider<UserRepository> userRepositoryProvider) {
    return new HomeViewModel_Factory(transactionRepositoryProvider, userRepositoryProvider);
  }

  public static HomeViewModel newInstance(TransactionRepository transactionRepository,
      UserRepository userRepository) {
    return new HomeViewModel(transactionRepository, userRepository);
  }
}
