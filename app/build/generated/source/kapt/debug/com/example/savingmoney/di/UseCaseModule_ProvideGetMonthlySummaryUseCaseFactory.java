package com.example.savingmoney.di;

import com.example.savingmoney.data.repository.TransactionRepository;
import com.example.savingmoney.domain.usecase.GetMonthlySummaryUseCase;
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
public final class UseCaseModule_ProvideGetMonthlySummaryUseCaseFactory implements Factory<GetMonthlySummaryUseCase> {
  private final Provider<TransactionRepository> transactionRepositoryProvider;

  public UseCaseModule_ProvideGetMonthlySummaryUseCaseFactory(
      Provider<TransactionRepository> transactionRepositoryProvider) {
    this.transactionRepositoryProvider = transactionRepositoryProvider;
  }

  @Override
  public GetMonthlySummaryUseCase get() {
    return provideGetMonthlySummaryUseCase(transactionRepositoryProvider.get());
  }

  public static UseCaseModule_ProvideGetMonthlySummaryUseCaseFactory create(
      Provider<TransactionRepository> transactionRepositoryProvider) {
    return new UseCaseModule_ProvideGetMonthlySummaryUseCaseFactory(transactionRepositoryProvider);
  }

  public static GetMonthlySummaryUseCase provideGetMonthlySummaryUseCase(
      TransactionRepository transactionRepository) {
    return Preconditions.checkNotNullFromProvides(UseCaseModule.INSTANCE.provideGetMonthlySummaryUseCase(transactionRepository));
  }
}
