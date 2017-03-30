#include <jni.h>
#include <cstdlib>
#include <chrono>
#include "ParallelCalculations.h"

using namespace std;
using namespace std::chrono;


jobject CreateJResult(JNIEnv *env, float PI, double time_span);


extern "C"
jobject
Java_com_erkkiperkele_master_1android_activity_MultiPiActivity_calculatePi__II(
        JNIEnv *env,
        jobject /* this */,
        jint numberOfOperations,
        jint numberOfThreads) {

    high_resolution_clock::time_point startTime = high_resolution_clock::now();

    float PI = ParallelCalculations::CalculateParallelPi(numberOfOperations, numberOfThreads);

    high_resolution_clock::time_point endTime = high_resolution_clock::now();
    double time_span = duration_cast<duration<double> >(endTime - startTime).count();

    return CreateJResult(env, PI, time_span);
}

jobject CreateJResult(JNIEnv *env, float PI, double time_span) {
    jclass cls = env->FindClass("com/erkkiperkele/master_android/entity/JResult");
    jmethodID constructor = env->GetMethodID(cls, "<init>", "(FD)V");
    jobject jResult = env->NewObject(cls, constructor, PI, time_span);

    return jResult;
}