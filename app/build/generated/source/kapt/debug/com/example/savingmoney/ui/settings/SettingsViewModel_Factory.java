package com.example.savingmoney.ui.settings;

import com.example.savingmoney.domain.usecase.GetLanguageUseCase;
import com.example.savingmoney.domain.usecase.GetThemeUseCase;
import com.example.savingmoney.domain.usecase.UpdateLanguageUseCase;
import com.example.savingmoney.domain.usecase.UpdateThemeUseCase;
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
public final class SettingsViewModel_Factory implements Factory<SettingsViewModel> {
  private final Provider<GetThemeUseCase> getThemeUseCaseProvider;

  private final Provider<GetLanguageUseCase> getLanguageUseCaseProvider;

  private final Provider<UpdateThemeUseCase> updateThemeUseCaseProvider;

  private final Provider<UpdateLanguageUseCase> updateLanguageUseCaseProvider;

  public SettingsViewModel_Factory(Provider<GetThemeUseCase> getThemeUseCaseProvider,
      Provider<GetLanguageUseCase> getLanguageUseCaseProvider,
      Provider<UpdateThemeUseCase> updateThemeUseCaseProvider,
      Provider<UpdateLanguageUseCase> updateLanguageUseCaseProvider) {
    this.getThemeUseCaseProvider = getThemeUseCaseProvider;
    this.getLanguageUseCaseProvider = getLanguageUseCaseProvider;
    this.updateThemeUseCaseProvider = updateThemeUseCaseProvider;
    this.updateLanguageUseCaseProvider = updateLanguageUseCaseProvider;
  }

  @Override
  public SettingsViewModel get() {
    return newInstance(getThemeUseCaseProvider.get(), getLanguageUseCaseProvider.get(), updateThemeUseCaseProvider.get(), updateLanguageUseCaseProvider.get());
  }

  public static SettingsViewModel_Factory create(Provider<GetThemeUseCase> getThemeUseCaseProvider,
      Provider<GetLanguageUseCase> getLanguageUseCaseProvider,
      Provider<UpdateThemeUseCase> updateThemeUseCaseProvider,
      Provider<UpdateLanguageUseCase> updateLanguageUseCaseProvider) {
    return new SettingsViewModel_Factory(getThemeUseCaseProvider, getLanguageUseCaseProvider, updateThemeUseCaseProvider, updateLanguageUseCaseProvider);
  }

  public static SettingsViewModel newInstance(GetThemeUseCase getThemeUseCase,
      GetLanguageUseCase getLanguageUseCase, UpdateThemeUseCase updateThemeUseCase,
      UpdateLanguageUseCase updateLanguageUseCase) {
    return new SettingsViewModel(getThemeUseCase, getLanguageUseCase, updateThemeUseCase, updateLanguageUseCase);
  }
}
