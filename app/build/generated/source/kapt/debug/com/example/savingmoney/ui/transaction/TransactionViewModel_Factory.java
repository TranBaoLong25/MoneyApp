package com.example.savingmoney.ui.transaction;

import com.example.savingmoney.data.repository.CategoryRepository;
import com.example.savingmoney.domain.usecase.AddTransactionUseCase;
import com.example.savingmoney.domain.usecase.GetTransactionListUseCase;
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
public final class TransactionViewModel_Factory implements Factory<TransactionViewModel> {
  private final Provider<AddTransactionUseCase> addTransactionUseCaseProvider;

  private final Provider<GetTransactionListUseCase> getTransactionListUseCaseProvider;

  private final Provider<CategoryRepository> categoryRepositoryProvider;

  public TransactionViewModel_Factory(Provider<AddTransactionUseCase> addTransactionUseCaseProvider,
      Provider<GetTransactionListUseCase> getTransactionListUseCaseProvider,
      Provider<CategoryRepository> categoryRepositoryProvider) {
    this.addTransactionUseCaseProvider = addTransactionUseCaseProvider;
    this.getTransactionListUseCaseProvider = getTransactionListUseCaseProvider;
    this.categoryRepositoryProvider = categoryRepositoryProvider;
  }

  @Override
  public TransactionViewModel get() {
    return newInstance(addTransactionUseCaseProvider.get(), getTransactionListUseCaseProvider.get(), categoryRepositoryProvider.get());
  }

  public static TransactionViewModel_Factory create(
      Provider<AddTransactionUseCase> addTransactionUseCaseProvider,
      Provider<GetTransactionListUseCase> getTransactionListUseCaseProvider,
      Provider<CategoryRepository> categoryRepositoryProvider) {
    return new TransactionViewModel_Factory(addTransactionUseCaseProvider, getTransactionListUseCaseProvider, categoryRepositoryProvider);
  }

  public static TransactionViewModel newInstance(AddTransactionUseCase addTransactionUseCase,
      GetTransactionListUseCase getTransactionListUseCase, CategoryRepository categoryRepository) {
    return new TransactionViewModel(addTransactionUseCase, getTransactionListUseCase, categoryRepository);
  }
}
