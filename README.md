# ⏱️ Sports Timer - Android App

A professional sports timer application designed for training sessions with configurable rounds, rest periods, and audio cues. Perfect for boxing, MMA, HIIT, CrossFit, martial arts, and any interval-based workout. Built with Kotlin for Android devices.

## 📱 Features

- **⏱️ Customizable Timer Settings**
  - Training time: 1-10 minutes
  - Rest time: 15-120 seconds
  - Number of rounds: 1-12 rounds
  - Buffer time: 10-60 seconds

- **🔔 Audio Notifications**
  - Single bell for buffer start and rest end
  - Double bell for training round completion
  - Final bell for workout completion

- **🌙 Dark Theme Interface**
  - Professional dark UI optimized for gym environments
  - Clean, distraction-free design
  - Responsive layout for all screen sizes

- **🔋 Background Processing**
  - Timer continues running when screen is locked
  - Battery-efficient partial wake lock implementation
  - No interruption during workouts

- **📱 User-Friendly Controls**
  - Large, easy-to-tap buttons
  - Start, Pause/Resume, and Reset functionality
  - Real-time phase indicators (Buffer, Training, Rest, Complete)

## 🎯 Default Configuration

- **Training Time:** 3 minutes
- **Rest Time:** 45 seconds
- **Rounds:** 3 rounds
- **Buffer Time:** 30 seconds

## �‍♂️ cPerfect For

- **🥊 Boxing & Combat Sports:** Traditional boxing rounds with rest periods
- **🏋️‍♀️ HIIT Workouts:** High-intensity interval training sessions
- **🤸‍♂️ CrossFit:** Timed workout rounds with rest intervals
- **🥋 Martial Arts:** Sparring sessions and technique practice
- **🏃‍♂️ Cardio Training:** Interval running and cycling workouts
- **💪 Strength Training:** Rest periods between sets
- **🧘‍♀️ Yoga & Meditation:** Timed poses and breathing exercises
- **⚽ Sports Practice:** Drill sessions with timed intervals

## 🏗️ Technical Specifications

- **Language:** Kotlin
- **Platform:** Android (API 21+)
- **Architecture:** Single Activity with lifecycle-aware components
- **UI Framework:** Android Views with LinearLayout
- **Audio:** MediaPlayer for bell sound effects
- **Background Processing:** PowerManager WakeLock for continuous operation

## 📦 Installation

### Method 1: Build from Source
1. Clone this repository
2. Open in Android Studio
3. Build → Build Bundle(s) / APK(s) → Build APK(s)
4. Install the generated APK on your Android device

### Method 2: Direct APK Installation
1. Download the APK from releases
2. Enable "Install from Unknown Sources" on your device
3. Install and enjoy!

## 🎮 How to Use

1. **Configure Settings:** Adjust training time, rest time, rounds, and buffer time using the number pickers
2. **Start Workout:** Tap the START button to begin the timer sequence
3. **Timer Flow:**
   - Buffer Time (preparation) → Single bell
   - Training Round → Double bell
   - Rest Period → Single bell
   - Repeat for configured rounds
   - Workout Complete → Final bell

4. **Controls:**
   - **PAUSE/RESUME:** Pause and resume the current timer
   - **RESET:** Stop the workout and return to default settings

## 🔧 Project Structure

```
SportsTimerApp/
├── app/
│   ├── src/main/
│   │   ├── java/com/sportstimer/
│   │   │   └── MainActivity.kt          # Main application logic
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   │   └── activity_main.xml    # UI layout
│   │   │   ├── values/
│   │   │   │   ├── colors.xml           # Dark theme colors
│   │   │   │   ├── strings.xml          # Text resources
│   │   │   │   └── styles.xml           # UI styles
│   │   │   ├── drawable/                # UI backgrounds and icons
│   │   │   └── raw/                     # Audio files (bells)
│   │   └── AndroidManifest.xml          # App configuration
│   └── build.gradle                     # App dependencies
├── build.gradle                         # Project configuration
└── README.md                           # This file
```

## 🎵 Audio Files

Place these audio files in `app/src/main/res/raw/`:
- `single_bell.mp3` - Single bell sound
- `double_bell.mp3` - Double bell sound  
- `final_bell.mp3` - Final bell sound

*The app works without audio files, but no sound notifications will play.*

## 🔋 Battery Optimization

The app uses a **partial wake lock** to ensure the timer continues running when the screen is locked:
- Only keeps CPU awake (not screen)
- Automatically releases when workout completes
- Minimal battery impact during use
- 10-minute safety timeout to prevent battery drain

## 🛠️ Development

### Requirements
- Android Studio Arctic Fox or newer
- Android SDK API 21+
- Kotlin 1.9.10+
- Gradle 8.5+

### Building
```bash
git clone https://github.com/yourusername/sports-timer-android.git
cd sports-timer-android
./gradlew assembleDebug
```

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## 📞 Support

If you encounter any issues or have feature requests, please open an issue on GitHub.

---

**🏆 Universal Sports Timer**
*Built with ❤️ for the fitness community*

Whether you're training for competition or staying fit, this timer adapts to your workout needs!