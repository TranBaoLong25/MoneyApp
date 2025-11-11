package com.example.savingmoney.di;

import com.example.savingmoney.data.local.dao.TransactionDao;
import com.example.savingmoney.data.repository.TransactionRepository;
import com.example.savingmoney.data.repository.UserRepository;
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
    "KotlinInternalInJava",
    "cast"
})
public final class RepositoryModule_ProvideTransactionRepositoryFactory implements Factory<TransactionRepository> {
  private final Provider<TransactionDao> transactionDaoProvider;

  private final Provider<UserRepository> userRepositoryProvider;

  public RepositoryModule_ProvideTransactionRepositoryFactory(
      Provider<TransactionDao> transactionDaoProvider,
      Provider<UserRepository> userRepositoryProvider) {
    this.transactionDaoProvider = transactionDaoProvider;
    this.userRepositoryProvider = userRepositoryProvider;
  }

  @Override
  public TransactionRepository get() {
    return provideTransactionRepository(transactionDaoProvider.get(), userRepositoryProvider.get());
  }

  public static RepositoryModule_ProvideTransactionRepositoryFactory create(
      Provider<TransactionDao> transactionDaoProvider,
      Provider<UserRepository> userRepositoryProvider) {
    return new RepositoryModule_ProvideTransactionRepositoryFactory(transactionDaoProvider, userRepositoryProvider);
  }

  public static TransactionRepository provideTransactionRepository(TransactionDao transactionDao,
      UserRepository userRepository) {
    return Preconditions.checkNotNullFromProvides(RepositoryModule.INSTANCE.provideTransactionRepository(transactionDao, userRepository));
  }
}
