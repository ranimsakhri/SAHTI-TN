# SA7TI Android

This Kotlin/Jetpack Compose client uses the same Spring Boot API as the Web app.
Open this directory in Android Studio and sync the Gradle project, or build the
debug APK from PowerShell with `.\gradlew.bat assembleDebug`.

For the Android emulator, the API base URL is `http://10.0.2.2:8081/`, which
forwards to the development machine's localhost. Cleartext traffic is enabled
only in the debug manifest. A physical device needs a reachable development-
machine address instead. For a release build, set `-PsahtiApiBaseUrl=` to the
HTTPS address of the deployed API.

The backend and MySQL database must be running first. The app supports client
registration/sign-in, profile and goal entry, Tunisian-food search, daily meal
and activity tracking, and daily progress. Only fictitious/demo user data should
be used while testing.
