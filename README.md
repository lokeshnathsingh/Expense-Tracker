# Expense-Tracker

A simple, offline expense tracking Android app built in Java. Add expenses via a bottom sheet, store them locally with Room, and view spending with charts (MPAndroidChart).

## ✨ Features

- ➕ Add expenses via a bottom-sheet dialog (`AddExpenseBottomSheet`)
- 💾 Local, offline-first storage with Room (`AppDatabase`, `ExpenseDao`)
- 📋 Expense list with a `RecyclerView` (`ExpenseAdapter`)
- 📊 Spending visualized with charts (MPAndroidChart)
- 🚀 Splash screen on launch

## 🛠️ Tech Stack

| Category | Tech |
|---|---|
| Language | Java |
| UI | XML layouts, RecyclerView, BottomSheetDialog |
| Local database | Room (`room-runtime` + `room-compiler`) |
| Charts | MPAndroidChart |
| Build system | Gradle (Kotlin DSL) |
| Min SDK / Target SDK | 24 / 36 |

## 📂 Project Structure

```
Expense-Tracker/
├── app/
│   ├── build.gradle.kts                          # Dependencies: Room, MPAndroidChart, etc.
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml
│       │   └── java/com/company/exptracker/
│       │       ├── MainActivity.java              # Expense list + chart screen
│       │       ├── SplashActivity.java             # Launch screen
│       │       ├── AddExpenseBottomSheet.java       # Bottom sheet for adding a new expense
│       │       ├── AppDatabase.java                 # Room database setup
│       │       ├── ExpenseDao.java                  # Room DAO (queries for expenses)
│       │       ├── Adapters/
│       │       │   └── ExpenseAdapter.java           # RecyclerView adapter for the expense list
│       │       └── Models/
│       │           └── Expense.java                  # Room entity / expense data model
│       │   └── res/
│       │       ├── layout/          # activity_main, activity_splash, bottom_add_expense, item_expense
│       │       ├── drawable/        # Icons (add, delete, done, logo)
│       │       ├── anim/            # slide_up / slide_down (bottom sheet animation)
│       │       ├── font/            # Roboto font
│       │       ├── menu/            # Toolbar menu
│       │       └── values/          # Strings, colors, themes, arrays
│       ├── androidTest/             # Instrumented tests
│       └── test/                    # Unit tests
├── gradle/
│   └── libs.versions.toml           # Version catalog
├── build.gradle.kts                 # Top-level build config
└── settings.gradle.kts
```

## 🚀 Getting Started

### Prerequisites

- Android Studio (latest stable)
- JDK 11+

### Setup

```bash
git clone https://github.com/lokeshnathsingh/Expense-Tracker.git
cd Expense-Tracker
```

1. Open the project in Android Studio.
2. Sync Gradle.
3. Run on an emulator or physical device.

### CLI Build

```bash
./gradlew assembleDebug
```

## 🗺️ Roadmap

- [ ] Expense categories & filtering
- [ ] Monthly/weekly budget limits with alerts
- [ ] Edit/delete existing expenses from the list
- [ ] Export data (CSV)

## 🤝 Contributing

Contributions, issues, and feature requests are welcome. Feel free to check the [issues page](https://github.com/lokeshnathsingh/Expense-Tracker/issues).

## 📄 License

This project is open for educational use. Add a license file if you plan to distribute it publicly.

## 👤 Author

**Lokesh Nath Singh**
- GitHub: [@lokeshnathsingh](https://github.com/lokeshnathsingh)
- LinkedIn: [lokesh-nath-singh](https://linkedin.com/in/lokesh-nath-singh/)
