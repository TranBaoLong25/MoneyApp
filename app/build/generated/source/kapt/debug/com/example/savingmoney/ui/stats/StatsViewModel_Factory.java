package com.example.savingmoney.ui.stats;

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
public final class StatsViewModel_Factory implements Factory<StatsViewModel> {
  @Override
  public StatsViewModel get() {
    return newInstance();
  }

  public static StatsViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static StatsViewModel newInstance() {
    return new StatsViewModel();
  }

  private static final class InstanceHolder {
    private static final StatsViewModel_Factory INSTANCE = new StatsViewModel_Factory();
  }
}
