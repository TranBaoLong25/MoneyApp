package com.example.savingmoney.domain.usecase;

import com.example.savingmoney.data.repository.TransactionRepository;
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
public final class GetMonthlySummaryUseCase_Factory implements Factory<GetMonthlySummaryUseCase> {
  private final Provider<TransactionRepository> transactionRepositoryProvider;

  public GetMonthlySummaryUseCase_Factory(
      Provider<TransactionRepository> transactionRepositoryProvider) {
    this.transactionRepositoryProvider = transactionRepositoryProvider;
  }

  @Override
  public GetMonthlySummaryUseCase get() {
    return newInstance(transactionRepositoryProvider.get());
  }

  public static GetMonthlySummaryUseCase_Factory create(
      Provider<TransactionRepository> transactionRepositoryProvider) {
    return new GetMonthlySummaryUseCase_Factory(transactionRepositoryProvider);
  }

  public static GetMonthlySummaryUseCase newInstance(TransactionRepository transactionRepository) {
    return new GetMonthlySummaryUseCase(transactionRepository);
  }
}
