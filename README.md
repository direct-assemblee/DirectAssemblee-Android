Direct Assemblée for Android 
===============

Download on the [Google Play Store](https://play.google.com/store/apps/details?id=org.ladlb.directassemblee).

## Setup

### Building the code

1. Install the latest [Android Studio](https://developer.android.com/studio/) from Google.
2. Clone the repository:
    ```shell
    git clone <URL>
    ```
4. Open `direct-assemblee` cloned directory in Android Studio.
6. This project uses Firebase for push notifications and analytics. See **Firebase** section below to configure project.
7. This project uses Fabric to report crashes in release mode. See **Fabric** section below to configure project.
8. Run the `app` module in Android Studio.

###  Firebase

This project uses Firebase. You should register your own Firebase account and generate `google_service.json` files if you want to use push notifications and analytics. We use two Firebase projects : one for developments in progress and one for production.

So, the existing configuration use two `google_service.json` files : 
1) One for `debug` build variant, located in `src/debug` folder, corresponding to development Firebase project.
2) One for `release` build variant, located in `src/release` folder, corresponding to production Firebase project.

###  Fabric

This project uses Fabric to report crashes in release build variant. You should register with Crashlytics and get your own API key and build secret if you want to build it with crashes reporting.
To set these information, see the [documentation](https://docs.fabric.io/android/fabric/settings/working-in-teams.html) from Fabric

##  API

### Production

The project use the production API, available if you want to test your application changes with the latest stable API version.

:warning: Actually it's not possible to use push notifications with the production api because of the firebase key used who is specific with the released version of the application. 

### Development

The development API isn't available because the Direct Assemblée teams uses it as sandbox. You can setup our API on your computer from Github projects [API](https://github.com/direct-assemblee/DirectAssemblee-api) and [Scraper](https://github.com/direct-assemblee/DirectAssemblee-scraper).

If you run API on your computer, you have to specify its URL in the `app/src/build.gradle` file.

## Third-party

### Open source libraries

* [CircleImageView](https://github.com/hdodenhof/CircleImageView)
* [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart)
* [RxBinding](https://github.com/JakeWharton/RxBinding)
* [RxLocation](https://github.com/patloew/RxLocation)

### Api

* [https://api-adresse.data.gouv.fr](https://adresse.data.gouv.fr/api)

##  Contribute

Pull request are more than welcome ! If you want to do it, use a feature branch and please make sure to use a descriptive title and description for your pull request. 

The project use unit tests. You must update them depending on your changes in the code. All unit tests should be OK in your pull request.

## License

Direct Assemblée for Android is under the GPLv3.

See [LICENSE](https://github.com/direct-assemblee/DirectAssemblee-Android/blob/master/LICENSE) for more license info.

## Contact

For any question or if you need help, you can send contact us at contact@direct-assemblee.org.