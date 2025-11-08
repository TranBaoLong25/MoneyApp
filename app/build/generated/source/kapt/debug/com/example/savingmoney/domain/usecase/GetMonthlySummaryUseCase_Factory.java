package com.example.savingmoney.domain.usecase;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class GetMonthlySummaryUseCase_Factory implements Factory<GetMonthlySummaryUseCase> {
  @Override
  public GetMonthlySummaryUseCase get() {
    return newInstance();
  }

  public static GetMonthlySummaryUseCase_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static GetMonthlySummaryUseCase newInstance() {
    return new GetMonthlySummaryUseCase();
  }

  private static final class InstanceHolder {
    private static final GetMonthlySummaryUseCase_Factory INSTANCE = new GetMonthlySummaryUseCase_Factory();
  }
}
