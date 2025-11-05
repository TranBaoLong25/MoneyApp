package com.example.savingmoney.ui.transaction;

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
public final class TransactionViewModel_Factory implements Factory<TransactionViewModel> {
  @Override
  public TransactionViewModel get() {
    return newInstance();
  }

  public static TransactionViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static TransactionViewModel newInstance() {
    return new TransactionViewModel();
  }

  private static final class InstanceHolder {
    private static final TransactionViewModel_Factory INSTANCE = new TransactionViewModel_Factory();
  }
}
