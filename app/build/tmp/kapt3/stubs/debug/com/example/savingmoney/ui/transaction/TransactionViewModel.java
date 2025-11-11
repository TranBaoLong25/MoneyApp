package com.example.savingmoney.ui.transaction;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B\u001f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u000e\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013J\b\u0010\u0014\u001a\u00020\u0011H\u0002J\b\u0010\u0015\u001a\u00020\u0011H\u0002J\u0006\u0010\u0016\u001a\u00020\u0011J\u000e\u0010\u0017\u001a\u00020\u00112\u0006\u0010\u0018\u001a\u00020\u0013J\u000e\u0010\u0019\u001a\u00020\u00112\u0006\u0010\u001a\u001a\u00020\u001bJ\u000e\u0010\u001c\u001a\u00020\u00112\u0006\u0010\u001d\u001a\u00020\u001eJ\u001a\u0010\u001f\u001a\u00020\u00112\b\u0010 \u001a\u0004\u0018\u00010!2\b\u0010\u001a\u001a\u0004\u0018\u00010\u0013J\u000e\u0010\"\u001a\u00020\u00112\u0006\u0010\u0018\u001a\u00020\u0013J\u000e\u0010#\u001a\u00020\u00112\u0006\u0010 \u001a\u00020!J\u0006\u0010$\u001a\u00020\u0011R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000b0\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f\u00a8\u0006%"}, d2 = {"Lcom/example/savingmoney/ui/transaction/TransactionViewModel;", "Landroidx/lifecycle/ViewModel;", "addTransactionUseCase", "Lcom/example/savingmoney/domain/usecase/AddTransactionUseCase;", "getTransactionListUseCase", "Lcom/example/savingmoney/domain/usecase/GetTransactionListUseCase;", "categoryRepository", "Lcom/example/savingmoney/data/repository/CategoryRepository;", "(Lcom/example/savingmoney/domain/usecase/AddTransactionUseCase;Lcom/example/savingmoney/domain/usecase/GetTransactionListUseCase;Lcom/example/savingmoney/data/repository/CategoryRepository;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/example/savingmoney/ui/transaction/TransactionUiState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "addCategory", "", "name", "", "loadCategories", "observeTransactions", "saveTransaction", "setAmount", "input", "setCategory", "category", "Lcom/example/savingmoney/data/model/Category;", "setDate", "timestamp", "", "setFilter", "type", "Lcom/example/savingmoney/data/model/TransactionType;", "setNote", "setType", "transactionSavedComplete", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class TransactionViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.example.savingmoney.domain.usecase.AddTransactionUseCase addTransactionUseCase = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.savingmoney.domain.usecase.GetTransactionListUseCase getTransactionListUseCase = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.savingmoney.data.repository.CategoryRepository categoryRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.example.savingmoney.ui.transaction.TransactionUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.example.savingmoney.ui.transaction.TransactionUiState> uiState = null;
    
    @javax.inject.Inject()
    public TransactionViewModel(@org.jetbrains.annotations.NotNull()
    com.example.savingmoney.domain.usecase.AddTransactionUseCase addTransactionUseCase, @org.jetbrains.annotations.NotNull()
    com.example.savingmoney.domain.usecase.GetTransactionListUseCase getTransactionListUseCase, @org.jetbrains.annotations.NotNull()
    com.example.savingmoney.data.repository.CategoryRepository categoryRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.example.savingmoney.ui.transaction.TransactionUiState> getUiState() {
        return null;
    }
    
    public final void addCategory(@org.jetbrains.annotations.NotNull()
    java.lang.String name) {
    }
    
    private final void loadCategories() {
    }
    
    private final void observeTransactions() {
    }
    
    public final void setFilter(@org.jetbrains.annotations.Nullable()
    com.example.savingmoney.data.model.TransactionType type, @org.jetbrains.annotations.Nullable()
    java.lang.String category) {
    }
    
    public final void setAmount(@org.jetbrains.annotations.NotNull()
    java.lang.String input) {
    }
    
    public final void setNote(@org.jetbrains.annotations.NotNull()
    java.lang.String input) {
    }
    
    public final void setType(@org.jetbrains.annotations.NotNull()
    com.example.savingmoney.data.model.TransactionType type) {
    }
    
    public final void setCategory(@org.jetbrains.annotations.NotNull()
    com.example.savingmoney.data.model.Category category) {
    }
    
    public final void setDate(long timestamp) {
    }
    
    public final void saveTransaction() {
    }
    
    public final void transactionSavedComplete() {
    }
}