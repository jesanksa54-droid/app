# App Locker (Android / Java)

A starter Android app-locker project with PIN setup, selectable installed apps, an AccessibilityService foreground-app monitor, and biometric prompt support on Android 9+ devices.

## Build APK using GitHub (phone-friendly)
1. Create a new repository on GitHub.
2. Upload the contents of this folder (not the containing folder itself) to the repository.
3. Open **Actions** and run **Build debug APK**, or push/commit to `main`.
4. Open the completed workflow run and download the `AppLocker-debug-apk` artifact.
5. Extract the artifact ZIP and install `app-debug.apk`. Android may ask you to allow installs from that browser/file manager.

## First launch
1. Set a PIN of at least 4 digits.
2. Check the apps you want to lock.
3. Tap **ENABLE APP MONITORING**, find App Locker in Accessibility settings, and enable it yourself.
4. Return to App Locker. Open a selected app to test the lock screen.

## Important limitations
- This is an educational starter project, not a security-certified product. Accessibility-based app locking can be bypassed or behave differently across Android vendors and system screens.
- Android requires the user to enable the Accessibility service manually. Do not enable accessibility for apps you do not trust.
- The PIN is stored in app-private preferences in this starter version and is not hardened against a rooted/compromised device. Do not reuse an important password.
- Biometric prompt support uses Android's platform API and is available only on supported devices with biometrics enrolled. PIN remains the fallback.
- App list visibility can vary by Android version and manufacturer.
