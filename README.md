# APK Builder for Android

A small Android template with a GitHub Actions build pipeline. You can build a debug APK without installing Android Studio locally.

## Build an APK

1. Open the repository on GitHub.
2. Go to **Actions**.
3. Select **Build APK**.
4. Click **Run workflow**.
5. Enter the app name, package name, and version.
6. Wait for the workflow to finish.
7. Open the completed workflow run and download the **APK artifact**.

The workflow validates the package name, applies the requested app metadata, compiles the Android project, and uploads the resulting APK.

## Notes

- This currently builds a debug APK.
- The Android app is intentionally minimal so it is easy to extend.
- Release signing is not configured. Add your own keystore through GitHub Actions secrets before distributing a release build.
