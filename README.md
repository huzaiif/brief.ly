# Brief.ly – AI-Powered Document Intelligence 🚀

**Brief.ly** is a modern, high-performance Android application designed to help users digest information faster. Leveraging the power of **Google Gemini AI** and **ML Kit**, it transforms documents, images, and raw text into concise, actionable summaries and key insights.

---

## ✨ Features

- 🔐 **Secure Authentication**: Firebase-powered Email/Password login and signup with Google Identity support.
- 📄 **Document Intelligence**: 
  - Upload **PDF** and **TXT** files directly.
  - High-fidelity text extraction using **PDFBox-Android**.
- 📸 **Smart Scanning (OCR)**: 
  - Capture or pick images to extract text using **ML Kit Text Recognition**.
  - Instantly summarize text from physical documents.
- 🧠 **AI Summarization**: Powered by **Google Gemini 1.5 Flash** for lightning-fast and accurate multi-perspective summaries.
- 💬 **Interactive AI Chat**: Deep dive into your documents with a contextual chat interface.
- 💾 **Personal Library**: Save and manage your summaries using **Firebase Realtime Database**.
- 🌐 **Research Hub**: Integrated Research WebView for instant cross-referencing and deeper exploration.
- 🎨 **Material 3 Excellence**: 
  - Sleek, minimalistic UI following Material Design 3 guidelines.
  - Native **Dark Mode** support with an easy toggle.
  - Smooth edge-to-edge experience.

---

## 🛠 Tech Stack

- **Language**: [Kotlin](https://kotlinlang.org/)
- **Architecture**: MVVM (Model-View-ViewModel) with Clean Architecture principles.
- **UI Framework**: XML Layouts with Material Design 3.
- **Networking**: [Retrofit](https://square.github.io/retrofit/) & Gson for API communication.
- **Backend/DB**: [Firebase Authentication](https://firebase.google.com/products/auth) & [Realtime Database](https://firebase.google.com/products/realtime-database).
- **AI/ML Engine**: 
  - [Google Gemini API](https://ai.google.dev/) (Summarization & Chat).
  - [Google ML Kit](https://developers.google.com/ml-kit) (OCR/Text Recognition).
- **Navigation**: Android Navigation Component with SafeArgs.
- **Key Libraries**: PDFBox-Android, Glide, Splashscreen API.

---

## 🏗 Project Structure

```text
com.huzaif.briefly
├── data
│   ├── api          # Retrofit interfaces & Gemini API models
│   ├── model        # Domain & Data classes (SummaryRecord, ChatMessage)
├── ui
│   ├── auth         # Login & Signup flows
│   ├── home         # Dashboard for quick actions
│   ├── scan         # OCR & Image-to-text implementation
│   ├── summary      # AI processing logic & Result screens
│   ├── saved        # User's personal summary history
│   ├── research     # Integrated web exploration
│   ├── profile      # User account & settings
│   └── custom       # Custom UI components (Wave views, etc.)
├── utils            # Extension functions & Helper classes
└── MainActivity.kt  # Root activity with Navigation Drawer & Dark Mode control
```

---

## 🚀 Setup & Installation

### 1. Prerequisites
- Android Studio Ladybug or newer.
- A Firebase Project.
- A Google Gemini API Key.

### 2. Firebase Configuration
1. Create a project in the [Firebase Console](https://console.firebase.google.com/).
2. Add an Android App with package name `com.huzaif.briefly`.
3. Download `google-services.json` and place it in the `app/` directory.
4. Enable **Email/Password Authentication** and **Realtime Database**.

### 3. API Key Setup
1. Obtain an API key from [Google AI Studio](https://aistudio.google.com/).
2. In the project, locate `SummaryViewModel.kt` (or your config file).
3. Replace the placeholder with your actual key:
   ```kotlin
   private val API_KEY = "YOUR_GEMINI_API_KEY" 
   ```

### 4. Build and Run
- Sync the project with Gradle.
- Run on an emulator or physical device (Min SDK 24).

---

## 🤝 Contributing
Contributions are welcome! Whether it's adding support for `.docx` files, improving AI prompts, or enhancing the UI, feel free to open a PR.

---

## 📄 License
This project is licensed under the MIT License.

---
*Developed with ❤️ by [Huzaif](https://github.com/huzaiif) and team.*
