# CoreLogic

[![](https://jitpack.io/v/claudiu-bele/core-logic.svg)](https://jitpack.io/#claudiu-bele/core-logic)


CoreLogic is a framework meant to simplify android development, in the new era of single-activity apps.

CoreLogic does the heavy lifting, so you can code your app the way you want to.
Some Android framework components such as Application, Activity, Fragment have subclasses you will need to extend so you can plug in and out different units of functionality into them, and have all the callbacks you need.


## General info
- minSdkVersion 14
- compileSdkVersion 28

## Setup

### 1. Get dependency

In your app's `build.gradle` (not the root one) add this dependency to your list of app dependencies

```
dependencies {
    implementation 'com.github.claudiu-bele:core-logic:0.4.3'
}

```
This will provide you with our library, and all the dependencies we use. Currently there isn't an option to get a version of the library without any dependencies.

Our dependencies, which are provided at api level once `core-logic` is imported into your project.
- Network: Retrofit(2.6.1) + Gson(2.8.5) + aux libraries
- DI: Koin(2.0.1) + Koin-androidx(2.0.1) + aux libraries
- Events: RxJava(2.2.12) + RxAndroid(2.1.1) + [RxRelay(2.1.1)](https://github.com/JakeWharton/RxRelay) + [AutoDispose(1.3.0)](https://github.com/uber/AutoDispose)
- kotlin-stdlib-jdk8 (1.3.11)*
- AndroidX Navigation (1.0.0)
- AndroidX lifecycle (2.1.0)
- other UI
    - ConstrainstLayout(2.0.0-beta2)
    - android.material (1.1.0-alpha10)
    - com.sothree.slidinguppanel (3.4.0)*


*: up for change 

The ability to provide test dependencies will soon be removed. Check [Build effective unit tests](https://developer.android.com/training/testing/unit-testing) on setting your project up for all the types of testing you want.

### 2. Create navigation graph

[Get started with the Navigation component](https://developer.android.com/guide/navigation/navigation-getting-started)

### 2. Your code

Subclass `CoreActivity` and `CoreFragment` in your classes or the following alternatives based on case:
#### Fragments:
- `CoreFragment`: Base fragment which comes backed with `core-logic` functionality.
- `FragmentController`: To capture callbacks from `CoreFragment`, such as `onBackPressed`, `onActivityResult`. Useful for creating standalone self-contained units of functionality that control activity flow, so one fragment does not need to handle flow of e.g. `GoogleSignInActivityResult` 
- `CoreNavHostFragment: NavHostFragment`: needed by navigation activities below, maintains navigation graph. Subclass it, or easily initialise with `CoreNavHostFragment.create(yourGraphResId: Int, startDestinationArgs: Bundle? = null`)
- `NavFragment: CoreFragment`: **preffered** fragment subclass for consumers of `NavActivityController` and `CoreNavHostFragment`, due to additional property `val: navController: NavController`. This property allows us to easily access the navigation graph.

#### Activities:
- `CoreActivity`: Base activity which comes backed with `core-logic` functionality
- `ActityController`: To capture callbacks from `CoreActivity`, such as `onActivityResult`, `onBackPressed`. 
- `CoreNavActivity: CoreActivity`: activity supporting the new android navigation components.
You only need to provide a `CoreNavHostFragment` to it in a function override.
- `NavActivity: CoreNavActivity`: activity that by default contains a Bottom Bar, a Navigation Drawer, and an Action Bar, all automatically updating based on the current position in the navigation tree.

#### Application
- `CoreActivity`: Base application which comes baked with `core-logic` functionality
- `ApplicationController`: Capture callbacks from `CoreApplication`

We'll go through the packages of the library and what they try to achieve. Explanation for the implementation of the sample and its libraries is [here](https://github.com/claudiu-bele/core-logic/tree/master/app)
## Packages 

### kotlin

##### kotlin.ext
- Extension functions for reflection (`Class.kt`, `KClass.kt`)
- for Rx (`Rx.kt`)
- for types (`String.kt`)

### platform

##### platform.lifecycle
- base/core `CoreApplication : Application`, `CoreActivity : AppCompatActivity` , `CoreFragment : Fragment` (androidx).
and their controllers.
- base controller classes for base classes: `ApplicationController, ActivityController, FragmentController`
- activities and fragments have a `getVm(clazz)` for getting a VM that also saves and restores state.

##### platform.vm
- `StatefulViewModel: ViewModel` is a model that saves and restores state. Check out `MoreInfoViewModel` for an implementation for data that survives `onCleared()`
- `ViewModelActivityController: ActivityController`: controller added automatically by `CoreActivity`. Manages saving and restoring the state in `StatefulViewModel`s
- `StatefulViewModelFactory: AndroidViewModelFactory` is a view model factory that you can use and is already used in `ViewModelActivityController`. It can create `StatefulViewModel` and `ViewModel` in 7 different constructor variations!!!

##### platform.widget
- `ConstraintLayout` classes for animating `ConstraintSet` changes.
- (preview) `Recyclerview`-related classes. Might end up making a custom `RecyclerView.Adapter`

##### platform.ext
- extension functions for `Theme` to retrieve colors from the theme easily (and more to come!)

### nav
- navigation-related fragments and activities
- custom `NavHostFragment`
- support for multiple start destinations
- navigation controller classes containing layout logic

### util
- formatting through `NumberFormattingUtils` and `StringFormattingUtils`
- time management through `TimeUtils`
