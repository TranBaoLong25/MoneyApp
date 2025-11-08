package com.example.savingmoney.di;

import com.example.savingmoney.domain.usecase.GetMonthlySummaryUseCase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class UseCaseModule_ProvideGetMonthlySummaryUseCaseFactory implements Factory<GetMonthlySummaryUseCase> {
  @Override
  public GetMonthlySummaryUseCase get() {
    return provideGetMonthlySummaryUseCase();
  }

  public static UseCaseModule_ProvideGetMonthlySummaryUseCaseFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static GetMonthlySummaryUseCase provideGetMonthlySummaryUseCase() {
    return Preconditions.checkNotNullFromProvides(UseCaseModule.INSTANCE.provideGetMonthlySummaryUseCase());
  }

  private static final class InstanceHolder {
    private static final UseCaseModule_ProvideGetMonthlySummaryUseCaseFactory INSTANCE = new UseCaseModule_ProvideGetMonthlySummaryUseCaseFactory();
  }
}
