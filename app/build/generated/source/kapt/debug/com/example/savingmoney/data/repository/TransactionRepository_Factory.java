package com.example.savingmoney.data.repository;

import com.example.savingmoney.data.local.dao.TransactionDao;
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
    "KotlinInternalInJava"
})
public final class TransactionRepository_Factory implements Factory<TransactionRepository> {
  private final Provider<TransactionDao> transactionDaoProvider;

  private final Provider<UserRepository> userRepositoryProvider;

  public TransactionRepository_Factory(Provider<TransactionDao> transactionDaoProvider,
      Provider<UserRepository> userRepositoryProvider) {
    this.transactionDaoProvider = transactionDaoProvider;
    this.userRepositoryProvider = userRepositoryProvider;
  }

  @Override
  public TransactionRepository get() {
    return newInstance(transactionDaoProvider.get(), userRepositoryProvider.get());
  }

  public static TransactionRepository_Factory create(
      Provider<TransactionDao> transactionDaoProvider,
      Provider<UserRepository> userRepositoryProvider) {
    return new TransactionRepository_Factory(transactionDaoProvider, userRepositoryProvider);
  }

  public static TransactionRepository newInstance(TransactionDao transactionDao,
      UserRepository userRepository) {
    return new TransactionRepository(transactionDao, userRepository);
  }
}
