# Ani-Agad: Technical Architecture & Documentation

## 1. Project Overview
**Ani-Agad** is a specialized urban micro-farming tracker and educational platform designed for Filipino youth. It leverages a modern Android stack to provide real-time tracking of planting batches, interactive care reminders, and a comprehensive guide for hydroponic and soil-based growth methods.

---

## 2. Core Architecture (The "Front Door")

### `MainActivity.kt`
*   **Role:** The entry point of the application.
*   **Logic:** 
    *   Initializes **Notification Channels** (specifically the "WATERING_CHANNEL") required for Android 8.0+ to show high-priority alerts.
    *   Sets up **Edge-to-Edge** display for a modern, immersive UI.
    *   Hosts the root `MyApplicationTheme` and calls `AppNavigation()`.
*   **Complexity:** **Medium.** It handles system-level services (NotificationManager) and the initial UI composition.

---

## 3. Navigation & Security Layer

### `navigation/AppNavigation.kt`
*   **Role:** The app's "GPS" and Security Gatekeeper.
*   **Key Features:**
    *   **Conditional Navigation:** Uses a Firebase Auth Listener to check if a user is logged in. If `currentUser == null`, the app **force-hides** the sidebar and dashboard, locking the user into the Login/Register flow.
    *   **ModalNavigationDrawer:** Implements the sliding sidebar menu containing "My Crops", "Alerts", and "Guides".
    *   **Custom Top Bar Tile:** A floating, semi-transparent header that dynamically switches between a "Hamburger Menu" icon and a "Back Arrow" depending on the screen depth.
    *   **Facebook-style Profile Button:** A top-right shortcut that uses the user's profile avatar to navigate to settings.
*   **Complexity:** **High.** It manages the global app state, navigation back-stack, and complex UI layouts (Scaffold + Drawer).

---

## 4. Data Layer (Local & Cloud)

### `data/CropDatabase.kt` (Room)
*   **Role:** Handles **Offline-First** local storage.
*   **Version 3 Update:** Stores `ownerId` (to isolate user data) and `growthMethod` (for dynamic alerts).
*   **Logic:** Uses a DAO (Data Access Object) to perform SQL queries. It returns a `Flow<List<CropBatch>>`, which means the UI updates automatically when data changes.

### `data/CropData.kt` & `data/GrowthMethodData.kt`
*   **Role:** The "Knowledge Base" (Single Source of Truth).
*   **Logic:** Stores the verbatim technical manuals for 6 crops and 3 growth methods (Hydroponics, Potting, Microgreens). 
*   **Innovation:** These files use a **Data-Driven** model. The `CreateCropScreen` and `GuidesScreen` pull from the same list, ensuring that any technical change in the guide is instantly reflected in the planting tracker.

### `data/WeatherService.kt` & `WeatherModels.kt`
*   **Role:** Real-time Environmental Monitoring.
*   **Logic:** Uses **Retrofit** and **Gson** to fetch live weather data for Lucena City from the **Open-Meteo API**.
*   **Sync:** Configured to refresh every 8 hours for optimal battery efficiency.

---

## 5. The Backend (Firebase Integration)

### `ui/screens/LoginScreen.kt` & `RegisterScreen.kt`
*   **Role:** Identity Management.
*   **Logic:** 
    *   **Auth:** Communicates with Firebase Authentication to create users and verify passwords.
    *   **Firestore Sync:** During registration, it "Hydrates" a cloud document in the `users` collection with the user's Full Name and Phone Number.
    *   **Validation:** Includes real-time password matching logic and custom error handling ("Incorrect Password", "Invalid Email").

---

## 6. Functional Screens (Features)

### `ui/screens/CropsScreen.kt`
*   **Role:** The User Dashboard.
*   **Interactive UI:** 
    *   **Speed Dial FAB:** A custom-shaped "Pill" button that extends to show "Add" and "Delete" modes.
    *   **Delete Mode:** Highlights all active plans and adds a red "X" for permanent removal.
    *   **Verification:** Uses `AlertDialog` for accidental click protection.

### `ui/screens/CreateCropScreen.kt`
*   **Role:** The Planting Form.
*   **Intelligent Autofill:** Swiping through the `HorizontalPager` (vegetables) automatically fills the `Growth Method` field based on the Technical Guide's recommendation.
*   **Cloud Mirroring:** When "Plant Now" is clicked, it saves to **Firestore** first (for cloud backup) and then to **Room** (for offline access).

### `ui/screens/AlertsScreen.kt` (Weather Engine)
*   **Role:** The "Lucena Guard" Engine.
*   **Smart Thresholds:** Implements logical checks:
    *   **High Heat (>32°C):** Alert to check Hydroponic air gaps.
    *   **High Rain (>70%):** Warning to move soil pots indoors.
    *   **High Wind (>20 km/h):** Reminder to secure hanging setups.
*   **Zero-Error Iconography:** Uses the OS-native Emoji system for 100% reliability and zero certificate dependency.

---

## 7. Notification System

### `notifications/NotificationScheduler.kt`
*   **Role:** The "Brain" of care reminders.
*   **Technical Rules:**
    *   **Hydroponics:** Schedules a check-in every 7 days.
    *   **Soil:** Schedules twice-daily "Knuckle Test" reminders.
    *   **Harvesting:** Schedules a special interactive notification on the exact harvest day (e.g., 30 days for Pechay).

### `notifications/WateringReceiver.kt`
*   **Role:** Handles the actual phone alert.
*   **Logic:** If it's a "Harvest Day" alert, it adds **"Yes/No" buttons**. If the user clicks "Yes", it sends an update to Firestore to increment the "Harvested" count in their profile.

---

## 8. User Profile & Settings

### `ui/screens/ProfileScreen.kt`
*   **Role:** Identity & Progress tracking.
*   **Logic:**
    *   Retrieves "Planted" count from the local database.
    *   Retrieves "Harvested" count and "Personal Info" from Firestore.
    *   **Gallery Integration:** Uses the Android Photo Picker to update the user's avatar locally using the **Coil** library.
    *   **Logout:** Wipes the session and forces a return to the security gate.
