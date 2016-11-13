#include <jni.h>
#include <string>

#include <iostream>
#include <cstdlib>
#include <cmath>
#include <chrono>
#include <fstream>
#include <iomanip>

bool FindIsInCircle(double x, double y);

extern "C"
jstring
Java_com_erkkiperkele_master_1android_SimpleParallel_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";

    return env->NewStringUTF(hello.c_str());
}

extern "C"
jdouble
Java_com_erkkiperkele_master_1android_SimpleParallel_calculatePi(
        JNIEnv* env,
        jobject /* this */)
{
//    high_resolution_clock::time_point startTime = high_resolution_clock::now();

    int npoints = 1000000;
    int circle_count = 0;
    int i = 0;

    while(i< npoints)
    {
        double x = (double)rand() / (double)RAND_MAX;
        double y = (double)rand() / (double)RAND_MAX;
        bool isInCircle = FindIsInCircle(x, y);

        if (isInCircle)
        {
            ++circle_count;
        }
        ++i;
    }

    double PI = 4 * (double)circle_count / (double)npoints;

    return PI;
}

bool FindIsInCircle(double x, double y)
{
    double distance = sqrt(pow(x, 2) + pow(y, 2));
    return distance < 1;
}
