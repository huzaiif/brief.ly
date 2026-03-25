# Brief.ly – AI Document Summarizer 🚀

**Brief.ly** is a modern, high-performance Android application designed to help users digest information faster. Using the power of **Google Gemini AI**, it extracts text from documents (PDF/TXT) or manual input and generates concise, actionable summaries and key insights.

---

## ✨ Features

- 🔐 **Secure Authentication**: Firebase-powered Email/Password login and signup.
- 📄 **Document Intelligence**: 
  - Upload **PDF** and **TXT** files directly.
  - Automatic text extraction using **PDFBox-Android**.
- 🧠 **AI Summarization**: Powered by **Google Gemini 1.5 Flash** for lightning-fast and accurate summaries.
- 📊 **Keyword Visualization**: Custom-built **Canvas API** word cloud that visualizes term frequency and document distribution.
- 💾 **Smart Storage**: Save your summaries to **Firebase Realtime Database** for access across sessions.
- 🌐 **Research Mode**: Built-in Research WebView for quick cross-referencing on Wikipedia or external articles.
- 🎨 **Material Design 3**: A clean, minimalistic UI following MD3 principles with support for edge-to-edge display.

---

## 🛠 Tech Stack

- **Language**: [Kotlin](https://kotlinlang.org/)
- **Architecture**: MVVM (Model-View-ViewModel)
- **UI Framework**: XML with Material Design 3
- **Networking**: [Retrofit](https://square.github.io/retrofit/) & Gson
- **Backend/DB**: [Firebase Authentication](https://firebase.google.com/products/auth) & [Realtime Database](https://firebase.google.com/products/realtime-database)
- **AI Engine**: [Google Gemini API](https://ai.google.dev/)
- **Navigation**: Android Navigation Component (SafeArgs)
- **Library**: PDFBox-Android (for document parsing)
- **Image Loading**: Glide

---

## 🏗 Project Structure

```text
com.example.briefly
├── data
│   ├── api          # Retrofit interfaces & Gemini models
│   ├── model        # Data classes (SummaryRecord)
├── ui
│   ├── auth         # Login & Signup Fragments/ViewModels
│   ├── home         # Main dashboard & File Picking
│   ├── saved        # RecyclerView for saved summaries
│   ├── summary      # AI processing & Result screens
│   └── research     # WebView implementation
├── utils            # Custom Views (WordCloudCanvas) & Helpers
└── MainActivity.kt  # Root activity & NavHost setup
```

---

## 🚀 Setup & Installation

### 1. Prerequisites
- Android Studio Ladybug or newer.
- A Firebase Project.
- A Google Gemini API Key.

### 2. Firebase Configuration
1. Go to the [Firebase Console](https://console.firebase.google.com/).
2. Create a new project and add an Android App (package name: `com.example.briefly`).
3. Download the `google-services.json` and place it in the `app/` directory.
4. Enable **Email/Password Authentication** and **Realtime Database**.

### 3. API Key Setup
1. Obtain an API key from [Google AI Studio](https://aistudio.google.com/).
2. Open `SummaryViewModel.kt`.
3. Replace `YOUR_GEMINI_API_KEY` with your actual key:
   ```kotlin
   private val API_KEY = "AIzaSy..." 
   ```

### 4. Build and Run
- Sync the project with Gradle files.
- Run the app on an Emulator or Physical Device (API 24+).

---

## 📸 UI Preview
*(Add your screenshots here later)*
- **Login**: Clean entrance with MD3 TextFields.
- **Home**: Navigation cards for Paste, Upload, and History.
- **Result**: AI summary followed by a dynamic Word Cloud.

---

## 🤝 Contributing
Contributions are welcome! Feel free to open issues or submit pull requests to improve the AI prompts or add support for `.docx` files.

---

## 📄 License
This project is licensed under the MIT License.

---
*Created with ❤️ by [Huzaiif] and Munaf(https://github.com/huzaiif)*
