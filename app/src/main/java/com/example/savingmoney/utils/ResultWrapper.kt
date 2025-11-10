// File: ResultWrapper.kt
package com.example.savingmoney.utils

/**
 * Lớp bọc sealed class để quản lý trạng thái tải dữ liệu/thực hiện tác vụ
 *
 * @param T Loại dữ liệu được trả về khi thành công (Success)
 */
sealed class ResultWrapper<out T> {

    /**
     * Trạng thái thành công, chứa dữ liệu hợp lệ.
     * @param data Dữ liệu thành công.
     */
    data class Success<out T>(val data: T) : ResultWrapper<T>()

    /**
     * Trạng thái lỗi.
     * @param message Thông báo lỗi.
     * @param code Mã lỗi (tùy chọn).
     */
    data class Error(val message: String? = null, val code: Int? = null) : ResultWrapper<Nothing>()

    /**
     * Trạng thái đang tải (Loading).
     */
    object Loading : ResultWrapper<Nothing>()
}