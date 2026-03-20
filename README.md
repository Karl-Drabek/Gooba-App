### Resources

 * [Beginner and Intermediate Kotlin Tutorial](https://kotlinlang.org/docs/kotlin-tour-welcome.html)
 * [Kotlin Multiplatform quickstart](https://kotlinlang.org/docs/multiplatform/quickstart.html)
 * [Create a Kotlin Multiplatform App](https://kotlinlang.org/docs/multiplatform/multiplatform-create-first-app.html#write-common-declarations)
 * [Compose Multiplatform](https://kotlinlang.org/docs/multiplatform/compose-layout.html)
### Setup
* Choose and install the IDE. Kotlin Multiplatform is supported in IntelliJ IDEA and Android Studio, so you can use the IDE you prefer.

* The JetBrains Toolbox App is the recommended tool to install IDEs. It allows you to manage multiple products or versions, including Early Access Program (EAP) and Nightly releases.

* For standalone installations, download the installer for IntelliJ IDEA or Android Studio.

* The plugins necessary for Kotlin Multiplatform require at least IntelliJ IDEA 2025.2.2 or Android Studio Otter 2025.2.1.

* Install the Kotlin Multiplatform IDE plugin (not to be confused with the Kotlin Multiplatform Gradle plugin).

* Installing the Kotlin Multiplatform IDE plugin for IntelliJ IDEA also installs all necessary dependencies if you don't have them yet (Android Studio has all necessary plugins bundled).

* You can create your first project in IntelliJ IDEA to intall your [environment variables](https://developer.android.com/tools/variables), it may help to look at Android's [sdkmanager](https://developer.android.com/tools/sdkmanager). If you do, make sure that your sdkmanager has a compatible version with your IntelliJ IDEA.
  
### Using Kotlin Multiplatform
This is a Kotlin Multiplatform project targeting Android, iOS.

* [/composeApp](./composeApp/src) is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
    - [commonMain](./composeApp/src/commonMain/kotlin) is for code that’s common for all targets.
    - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
      For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
      the [iosMain](./composeApp/src/iosMain/kotlin) folder would be the right place for such calls.
      Similarly, if you want to edit the Desktop (JVM) specific part, the [jvmMain](./composeApp/src/jvmMain/kotlin)
      folder is the appropriate location.

* [/iosApp](./iosApp/iosApp) contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform,
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.

### Build and Run Android Application

To build and run the development version of the Android app, use the run configuration from the run widget
in your IDE’s toolbar or build it directly from the terminal:

- on macOS/Linux
  ```shell
  ./gradlew :composeApp:assembleDebug
  ```
- on Windows
  ```shell
  .\gradlew.bat :composeApp:assembleDebug
  ```

### Build and Run iOS Application

To build and run the development version of the iOS app, use the run configuration from the run widget
in your IDE’s toolbar or open the [/iosApp](./iosApp) directory in Xcode and run it from there.

---

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…