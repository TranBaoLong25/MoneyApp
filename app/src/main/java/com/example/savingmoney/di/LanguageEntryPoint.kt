package com.example.savingmoney.di

import com.example.savingmoney.domain.usecase.GetLanguageUseCase
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Hilt EntryPoint: Cho phép truy cập GetLanguageUseCase từ Application Component (SingletonComponent)
 * một cách thủ công, cần thiết khi truy cập dependency quá sớm (trong attachBaseContext).
 *
 * Lưu ý: Tệp này phải nằm trong gói mà Hilt có thể quét được (ví dụ: gói di hoặc root).
 */
@EntryPoint
@InstallIn(SingletonComponent::class)
interface LanguageEntryPoint {
    /**
     * Định nghĩa hàm để expose (lộ ra) GetLanguageUseCase.
     * Hilt sẽ cung cấp instance đã được tạo sẵn trong ApplicationComponent.
     */
    fun getLanguageUseCase(): GetLanguageUseCase
}