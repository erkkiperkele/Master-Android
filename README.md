# Master-Android
I wish I knew (better)

## General

* Style guide
https://google.github.io/styleguide/cppguide.html


## Useful links on how to add C++ libs to Android project

* Add C++ to project
https://developer.android.com/studio/projects/add-native-code.html

* Android.mk
https://developer.android.com/ndk/guides/android_mk.html

* NDK's APIs
https://developer.android.com/ndk/guides/stable_apis.html

* Simple GitHub project with static and Shared libraries
https://github.com/googlesamples/android-ndk/tree/master-cmake/hello-libs



## Random links for DNN

* Caffe Github project:
https://github.com/sh1r0/caffe-android-lib

* Caffe Android Demo (same guy as the lib):
https://github.com/sh1r0/caffe-android-demo





## Random links for parallel programming:

* doc on openmp
http://bisqwit.iki.fi/story/howto/openmp/#ParallelConstruct

* fix for openmp on Android ndk
https://groups.google.com/forum/#!topic/android-ndk/pUfqxURgNbQ/discussion


## Native Debugging checklist

Assuming it doesn't work:

* Value returned by c++ is default (eg: 0, false, ...)
    - make sure return types correspond to each other in **java** and **c++**
    
    
    
### [Bug] openmp

* Notes on Clang (non) support of openmp from the Google ndk team: 
https://github.com/android-ndk/ndk/issues/9

* Example of an openmp Android project on github (2014 though):
https://github.com/noritsuna/HandDetectorOpenMP

* Android CMake (Same guy as for Caffe!)
https://github.com/sh1r0/android-cmake



Markdown Syntax [Reference Card][1]
[1]: https://en.support.wordpress.com/markdown-quick-reference/
