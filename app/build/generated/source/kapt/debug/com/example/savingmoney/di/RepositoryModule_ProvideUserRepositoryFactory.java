package com.example.savingmoney.di;

import com.example.savingmoney.data.local.dao.UserDao;
import com.example.savingmoney.data.preferences.UserPreferences;
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
public final class RepositoryModule_ProvideUserRepositoryFactory implements Factory<UserRepository> {
  private final Provider<UserDao> userDaoProvider;

  private final Provider<UserPreferences> userPreferencesProvider;

  public RepositoryModule_ProvideUserRepositoryFactory(Provider<UserDao> userDaoProvider,
      Provider<UserPreferences> userPreferencesProvider) {
    this.userDaoProvider = userDaoProvider;
    this.userPreferencesProvider = userPreferencesProvider;
  }

  @Override
  public UserRepository get() {
    return provideUserRepository(userDaoProvider.get(), userPreferencesProvider.get());
  }

  public static RepositoryModule_ProvideUserRepositoryFactory create(
      Provider<UserDao> userDaoProvider, Provider<UserPreferences> userPreferencesProvider) {
    return new RepositoryModule_ProvideUserRepositoryFactory(userDaoProvider, userPreferencesProvider);
  }

  public static UserRepository provideUserRepository(UserDao userDao,
      UserPreferences userPreferences) {
    return Preconditions.checkNotNullFromProvides(RepositoryModule.INSTANCE.provideUserRepository(userDao, userPreferences));
  }
}
