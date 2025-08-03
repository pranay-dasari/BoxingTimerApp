# Changelog

All notable changes to the Sports Timer project will be documented in this file.

## [1.0.0] - 2025-01-03

### 🎉 Initial Release

#### ✨ Features
- **Configurable Timer Settings**
  - Training time: 1-10 minutes
  - Rest time: 15-120 seconds  
  - Number of rounds: 1-12 rounds
  - Buffer time: 10-60 seconds

- **Audio Notifications**
  - Single bell for buffer start and rest end
  - Double bell for training round completion
  - Final bell for workout completion

- **Professional Dark Theme**
  - Optimized for gym environments
  - Clean, distraction-free design
  - Responsive layout for all screen sizes

- **Background Processing**
  - Timer continues running when screen is locked
  - Battery-efficient partial wake lock implementation
  - No interruption during workouts

- **User-Friendly Interface**
  - Large, easy-to-tap buttons
  - Start, Pause/Resume, and Reset functionality
  - Real-time phase indicators

#### 🏗️ Technical Implementation
- Built with Kotlin for Android (API 21+)
- Single Activity architecture with lifecycle-aware components
- PowerManager WakeLock for continuous background operation
- MediaPlayer for audio cues
- LinearLayout-based responsive design

#### 🎯 Supported Activities
- Boxing and combat sports training
- HIIT (High-Intensity Interval Training)
- CrossFit workouts
- Martial arts practice
- Cardio interval training
- Strength training rest periods
- Yoga and meditation timing
- General sports practice sessions

### 🔧 Technical Details
- **Minimum SDK:** API 21 (Android 5.0)
- **Target SDK:** API 33 (Android 13)
- **Language:** Kotlin 1.9.10
- **Build Tools:** Android Gradle Plugin 8.2.0
- **Dependencies:** AndroidX libraries, Material Design components