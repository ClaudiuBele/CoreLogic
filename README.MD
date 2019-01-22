# CoreLogic

CoreLogic is a framework meant to simplify android development, in the new era of single-activity apps.

CoreLogic does the heavy lifting, so you can code your app the way you want to.
Some Android framework components such as Application, Activity, Fragment have subclasses you will need to extend so you can plug in and out different units of functionality into them, and have all the callbacks you need.


## General info
- minSdkVersion 14
- compileSdkVersion 28

## Setup

### 1. Get dependency

In your app's `build.gradle` (not the root one) add this dependency to your dependencies

```
dependencies {
    implementation 'com.github.claudiu-bele:core-logic:0.4.?'
}

```
This will provide you with our library, and all the dependencies we use. Currently there isn't an option to get a version of the library without any dependencies.

### 2. Create navigation graph

Google it for now. I'll look into good links

### 2. Your code

Subclass `BaseActivity` and `BaseFragment` in your classes or the following alternatives based on case:
#### Fragments:
- `CoreNavHostFragment: NavHostFragment`: needed by navigation activities below. Subclass, or easily initialise with `CoreNavHostFragment.create(yourGraphResId: Int, startDestinationArgs: Bundle? = null`)
- `NavFragment: CoreFragment`: **preffered** fragment subclass for consumers of `NavActivityController` and `CoreNavHostFragment`, due to additional property `val: navController: NavController`. This property allows us use navigation actions easily.
- `(preview) DiFragment: CoreFragment`: Fragment containing dependency injection logic

#### Activities:
- `CoreNavActivity: CoreActivity`: activity supporting the new android navigation components.
You only need to provide a `CoreNavHostFragment` to it in a function override.
- `NavActivity: CoreNavActivity`: activity that by default contains a Bottom Bar, a Navigation Drawer, and an Action Bar, all automatically updating based on the current position in the navigation tree.
- `(preview) DiActivity: CoreActivity` Activity containing dependency injection logic

#### Application
- `(preview) DiApplication: CoreApplication`

## Packages

### db (*COMING IN SEPARATE LIBRARY SOON*)

Persistance with Room db

### platform

#### platform.lifecycle
- base/core `CoreApplication : Application`, `CoreActivity : AppCompatActivity` , `CoreFragment : Fragment` (androidx).
and their controllers.
- base controller classes for base classes: `ApplicationController, ActivityController, FragmentController`
- activities and fragments have a `getVm(clazz)` for getting a VM that also saves and restores state.

#### platform.vm
- `StatefulViewModel: ViewModel` is a model that saves and restores state. Check out `MoreInfoViewModel` for an implementation for data that survives `onCleared()`
- `ViewModelActivityController: ActivityController`: controller added automatically by `CoreActivity`. Manages saving and restoring the state in `StatefulViewModel`s
- `StatefulViewModelFactory: AndroidViewModelFactory` is a view model factory that you can use and is already used in `ViewModelActivityController`. It can create `StatefulViewModel` and `ViewModel` in 7 different constructor variations!!!

#### platform.widget
- `ConstraintLayout` classes for animating `ConstraintSet` changes.
- (preview) `Recyclerview`-related classes. Might end up making a custom `RecyclerView.Adapter`

#### platform.ext
- extension functions for `Theme` to retrieve colors from the theme easily (and more to come!)

### nav
- navigation-related fragments and activities
- custom `NavHostFragment`
- support for multiple start destinations
- navigation controller classes containing layout logic

### di

Dependency Injection support
- Relies on Singleton Application Component, which is then used by activities and fragments,
however it can be decoupled and DI-related controllers safely interact and query each other
- At UI level easily integrated through (`DiActivity` and `DiFragment`) in `dk.sidereal.corelogic.di.lifecycle`

### kotlin.ext
- extension functions for `String`
- extension functions for `Class` and `KClass`

### util
- formatting through `NumberFormattingUtils` and `StringFormattingUtils`
- time management through `TimeUtils`
