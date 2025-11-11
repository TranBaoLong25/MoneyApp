package com.example.savingmoney.ui.stats;

import com.example.savingmoney.domain.usecase.GetMonthlySummaryUseCase;
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
    "KotlinInternalInJava",
    "cast"
})
public final class StatsViewModel_Factory implements Factory<StatsViewModel> {
  private final Provider<GetMonthlySummaryUseCase> getMonthlySummaryUseCaseProvider;

  public StatsViewModel_Factory(
      Provider<GetMonthlySummaryUseCase> getMonthlySummaryUseCaseProvider) {
    this.getMonthlySummaryUseCaseProvider = getMonthlySummaryUseCaseProvider;
  }

  @Override
  public StatsViewModel get() {
    return newInstance(getMonthlySummaryUseCaseProvider.get());
  }

  public static StatsViewModel_Factory create(
      Provider<GetMonthlySummaryUseCase> getMonthlySummaryUseCaseProvider) {
    return new StatsViewModel_Factory(getMonthlySummaryUseCaseProvider);
  }

  public static StatsViewModel newInstance(GetMonthlySummaryUseCase getMonthlySummaryUseCase) {
    return new StatsViewModel(getMonthlySummaryUseCase);
  }
}
