1. 💾 Tầng Data (Data Layer)
Mục đích: Chịu trách nhiệm cung cấp và quản lý nguồn dữ liệu. Tầng này bao gồm việc triển khai cơ sở dữ liệu (Room) và 
định nghĩa các Repositories (Lớp truy cập dữ liệu).
## 🧱 Cấu trúc thư mục & Vai trò các thành phần
| **Thư mục / Thành phần** | **Files Ví dụ** | **Vai trò Cụ thể** |
|----------------------------|------------------|---------------------|
| `data/model` | `User.kt`, `Transaction.kt`, `Category.kt` | **Entities:** Định nghĩa cấu trúc dữ liệu cơ bản (các bảng trong Room). |
| `data/local/dao` | `UserDao.kt`, `TransactionDao.kt`, `CategoryDao.kt` | **Data Access Objects (DAO):** Giao diện truy vấn cơ sở dữ liệu (CRUD). |
| `data/local` | `AppDatabase.kt` | **Database:** Lớp cơ sở dữ liệu chính (Room), kết nối tất cả DAOs và Entities. |
| `data/repository` | `UserRepository.kt`, `TransactionRepository.kt` | **Repositories (Triển khai):** Xử lý logic chọn nguồn dữ liệu (database, cache, hoặc API). |
| `data/preferences` | `UserPreferences.kt` | **Preferences:** Lưu trữ dữ liệu cấu hình hoặc xác thực đơn giản (key-value storage). |
| `di/RepositoryModule.kt` | `RepositoryModule.kt` | **Dependency Injection (Hilt):** Cấu hình Hilt để cung cấp các đối tượng Repository. |

2. 🧠 Tầng Domain (Domain Layer)
Mục đích: Nơi chứa logic nghiệp vụ cốt lõi của ứng dụng (Business Logic).
Tầng này hoàn toàn độc lập và không nên chứa bất kỳ mã Android hoặc UI nào.
## ⚙️ Tầng Domain (Use Cases & Models)
| **Thư mục / Thành phần** | **Files Ví dụ** | **Vai trò Cụ thể** |
|----------------------------|------------------|---------------------|
| `domain/usecase` | `AuthUseCase.kt`, `AddTransactionUseCase.kt`, `GetMonthlySummaryUseCase.kt` | **Use Cases (Interactors):** Thực hiện các kịch bản nghiệp vụ cụ thể bằng cách gọi các Repository. |
| `domain/model` | `TransactionSummary.kt` | **Core Models:** Định nghĩa các đối tượng dữ liệu phức tạp được sử dụng trong các Use Case. |
| `di/UseCaseModule.kt` | `UseCaseModule.kt` | **Dependency Injection (Hilt):** Cấu hình Hilt để cung cấp các đối tượng Use Case. |

3. 🖥️ Tầng Presentation (Presentation Layer)
Mục đích: Chịu trách nhiệm hiển thị giao diện người dùng và quản lý vòng đời trạng thái UI. Tầng này sử dụng Jetpack Compose.
## 🎨 Tầng UI (Presentation Layer)
| **Thư mục / Thành phần** | **Files Ví dụ** | **Vai trò Cụ thể** |
|----------------------------|------------------|---------------------|
| `ui/*` | `LoginScreen.kt`, `HomeScreen.kt`, `StatsScreen.kt` | **Compose Screens:** Xây dựng giao diện bằng Jetpack Compose. |
| `ui/*ViewModel` | `AuthViewModel.kt`, `HomeViewModel.kt`, `TransactionViewModel.kt` | **ViewModels:** Quản lý trạng thái UI, giao tiếp với các Use Case để lấy dữ liệu. |
| `ui/navigation` | `NavGraph.kt`, `Destinations.kt` | **Navigation:** Quản lý điều hướng giữa các màn hình trong ứng dụng. |
| `ui/components` | `BottomNavigationBar.kt`, `CommonButton.kt`, `PieChart.kt` | **UI Components:** Các thành phần giao diện có thể tái sử dụng. |


