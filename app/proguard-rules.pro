# File: SavingMoneyApp/app/proguard-rules.pro

# Quy tắc chung cho Kotlin
-keepnames class * extends androidx.lifecycle.ViewModel
-keep class kotlin.Metadata { *; }

# Quy tắc cho Room (Database)
# BẢO TOÀN các Entity (Models) và DAO (Data Access Objects)
-keep class com.example.savingmoney.data.model.** { *; }
-keep class * extends androidx.room.RoomDatabase {
    public <init>();
}
-keep public class * extends androidx.room.Entity {
    public <fields>;
}

# Quy tắc cho Dagger Hilt (Dependency Injection)
# BẢO TOÀN các class và phương thức được Hilt sử dụng
-keep class dagger.hilt.android.HiltAndroidApp {
    <init>();
}
-keep class * implements dagger.hilt.internal.GeneratedComponent {
    <fields>;
    <methods>;
}
# BẢO TOÀN các Annotation Processor của KAPT/Hilt
-keep class * extends dagger.hilt.android.HiltWorker
-keep class * extends dagger.hilt.android.ViewModelLifecycle
-keep class * extends androidx.hilt.lifecycle.ViewModelAssistedFactory
-keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager