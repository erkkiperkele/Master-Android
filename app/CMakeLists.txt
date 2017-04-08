# Sets the minimum version of CMake required to build the native
# library. You should either keep the default value or only pass a
# value of 3.4.0 or lower.

cmake_minimum_required(VERSION 3.4.1)


# Searches for a specified prebuilt library and stores the path as a
# variable. Because system libraries are included in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )

#libraries to build

add_library(
    pi_calculations
    SHARED
    src/main/cpp-shared/simple_pi_calculations.cpp
    src/main/cpp-shared/parallel_pi_calculations.cpp)

add_library(
    simple_pi_service
    SHARED
    src/main/cpp/simple_pi_service.cpp)

add_library(
    parallel_pi_service
    SHARED
    src/main/cpp/parallel_pi_service.cpp)

target_link_libraries(
    parallel_pi_service
    pi_calculations
    ${log-lib})

target_link_libraries(
    simple_pi_service
    pi_calculations
    ${log-lib})