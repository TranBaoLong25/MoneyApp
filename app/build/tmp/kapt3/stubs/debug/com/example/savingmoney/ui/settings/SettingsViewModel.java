package com.example.savingmoney.ui.settings;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\'\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\u0006\u0010\u0017\u001a\u00020\u0018J\u000e\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001cJ\u000e\u0010\u001d\u001a\u00020\u001a2\u0006\u0010\u001e\u001a\u00020\u0018R\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\r0\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0017\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00140\u0013\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001f"}, d2 = {"Lcom/example/savingmoney/ui/settings/SettingsViewModel;", "Landroidx/lifecycle/ViewModel;", "getThemeUseCase", "Lcom/example/savingmoney/domain/usecase/GetThemeUseCase;", "getLanguageUseCase", "Lcom/example/savingmoney/domain/usecase/GetLanguageUseCase;", "updateThemeUseCase", "Lcom/example/savingmoney/domain/usecase/UpdateThemeUseCase;", "updateLanguageUseCase", "Lcom/example/savingmoney/domain/usecase/UpdateLanguageUseCase;", "(Lcom/example/savingmoney/domain/usecase/GetThemeUseCase;Lcom/example/savingmoney/domain/usecase/GetLanguageUseCase;Lcom/example/savingmoney/domain/usecase/UpdateThemeUseCase;Lcom/example/savingmoney/domain/usecase/UpdateLanguageUseCase;)V", "_settingsEventChannel", "Lkotlinx/coroutines/channels/Channel;", "Lcom/example/savingmoney/ui/settings/SettingsEvent;", "settingsEvents", "Lkotlinx/coroutines/flow/Flow;", "getSettingsEvents", "()Lkotlinx/coroutines/flow/Flow;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "Lcom/example/savingmoney/ui/settings/SettingsUiState;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "getCurrentUser", "", "onDarkModeToggled", "", "isDark", "", "onLanguageChanged", "code", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class SettingsViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.example.savingmoney.domain.usecase.GetThemeUseCase getThemeUseCase = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.savingmoney.domain.usecase.GetLanguageUseCase getLanguageUseCase = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.savingmoney.domain.usecase.UpdateThemeUseCase updateThemeUseCase = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.savingmoney.domain.usecase.UpdateLanguageUseCase updateLanguageUseCase = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.channels.Channel<com.example.savingmoney.ui.settings.SettingsEvent> _settingsEventChannel = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<com.example.savingmoney.ui.settings.SettingsEvent> settingsEvents = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.example.savingmoney.ui.settings.SettingsUiState> uiState = null;
    
    @javax.inject.Inject()
    public SettingsViewModel(@org.jetbrains.annotations.NotNull()
    com.example.savingmoney.domain.usecase.GetThemeUseCase getThemeUseCase, @org.jetbrains.annotations.NotNull()
    com.example.savingmoney.domain.usecase.GetLanguageUseCase getLanguageUseCase, @org.jetbrains.annotations.NotNull()
    com.example.savingmoney.domain.usecase.UpdateThemeUseCase updateThemeUseCase, @org.jetbrains.annotations.NotNull()
    com.example.savingmoney.domain.usecase.UpdateLanguageUseCase updateLanguageUseCase) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.example.savingmoney.ui.settings.SettingsEvent> getSettingsEvents() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.example.savingmoney.ui.settings.SettingsUiState> getUiState() {
        return null;
    }
    
    public final void onDarkModeToggled(boolean isDark) {
    }
    
    public final void onLanguageChanged(@org.jetbrains.annotations.NotNull()
    java.lang.String code) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCurrentUser() {
        return null;
    }
}