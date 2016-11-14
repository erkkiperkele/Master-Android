#include <jni.h>

#include <cstdlib>
#include <cmath>

bool FindIsInCircle(double x, double y);

extern "C"
jdouble
Java_com_erkkiperkele_master_1android_SimpleParallel_calculatePi(
        JNIEnv* env,
        jobject /* this */,
        jint numberOfOperations)
{
//    high_resolution_clock::time_point startTime = high_resolution_clock::now();

    int circle_count = 0;
    int i = 0;

    while(i< numberOfOperations)
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

    double PI = 4 * (double)circle_count / (double)numberOfOperations;

    return PI;
}

bool FindIsInCircle(double x, double y)
{
    double distance = sqrt(pow(x, 2) + pow(y, 2));
    return distance < 1;
}