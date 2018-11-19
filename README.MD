# CoreLogic ( dk.sidereal.corelogic )

Kotlin Project


## Packages

This repository will contain a unified android architecture composed of the following features

### db

Persistance with Room db

### nav

Navigation Components

### arch

Architecture Components, Livedata and ViewModel

### di

Dependency Injection support
- At ui level through `dk.sidereal.corelogic.di.ui`

### repo

Repository pattern
- Streams through RX
- interacts with `db package`

### net

Network layer
- Retrofit
- OkHttp
- Gson

#### platform

Platform level utilities and classes related to
- Activity
- Fragment
- Service
- Context
- Resources
- Animations


#### util

Other utils that didn't fit anywhere (although all packages can have a root `.util`) package
- conversion
- dates?
