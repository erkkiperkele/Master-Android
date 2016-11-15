#include <jni.h>
#include <cstdlib>
#include <cmath>
#include <chrono>

using namespace std;
using namespace std::chrono;

bool FindIsInCircle(float x, float y);
jobject CreateJResult(JNIEnv *env, float PI, double time_span);

extern "C"
jobject
Java_com_erkkiperkele_master_1android_SimplePiActivity_calculatePi__I(
        JNIEnv *env,
        jobject /* this */,
        jint numberOfOperations) {

    high_resolution_clock::time_point startTime = high_resolution_clock::now();

    int circle_count = 0;
    int i = 0;

    while (i < numberOfOperations) {
        float x = (float) rand() / (float) RAND_MAX;
        float y = (float) rand() / (float) RAND_MAX;
        bool isInCircle = FindIsInCircle(x, y);

        if (isInCircle) {
            ++circle_count;
        }
        ++i;
    }

    float PI = 4 * (float) circle_count / (float) numberOfOperations;

    high_resolution_clock::time_point endTime = high_resolution_clock::now();
    double time_span = duration_cast<duration<double> >(endTime - startTime).count();

    return CreateJResult(env, PI, time_span);
}

bool FindIsInCircle(float x, float y) {
    double distance = sqrt(pow(x, 2) + pow(y, 2));
    return distance < 1;
}

jobject CreateJResult(JNIEnv *env, float PI, double time_span) {
    jclass cls = env->FindClass("com/erkkiperkele/master_android/JResult");
    jmethodID constructor = env->GetMethodID(cls, "<init>", "(FD)V");
    jobject jResult = env->NewObject(cls, constructor, PI, time_span);

    return jResult;
}