package com.example.savingmoney.data.repository;

import com.example.savingmoney.data.local.dao.UserDao;
import com.example.savingmoney.data.preferences.UserPreferences;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
    "KotlinInternalInJava"
})
public final class UserRepository_Factory implements Factory<UserRepository> {
  private final Provider<UserDao> userDaoProvider;

  private final Provider<UserPreferences> userPreferencesProvider;

  public UserRepository_Factory(Provider<UserDao> userDaoProvider,
      Provider<UserPreferences> userPreferencesProvider) {
    this.userDaoProvider = userDaoProvider;
    this.userPreferencesProvider = userPreferencesProvider;
  }

  @Override
  public UserRepository get() {
    return newInstance(userDaoProvider.get(), userPreferencesProvider.get());
  }

  public static UserRepository_Factory create(Provider<UserDao> userDaoProvider,
      Provider<UserPreferences> userPreferencesProvider) {
    return new UserRepository_Factory(userDaoProvider, userPreferencesProvider);
  }

  public static UserRepository newInstance(UserDao userDao, UserPreferences userPreferences) {
    return new UserRepository(userDao, userPreferences);
  }
}
