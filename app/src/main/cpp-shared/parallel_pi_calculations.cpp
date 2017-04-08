#include <cstdlib>
#include <cmath>

#include <omp.h>
#include "parallel_pi_calculations.h"


float ParallelPiCalculations::CalculateParallelPi(int numberOfOperations, int numberOfThreads) {
    int circle_count = 0;
    int num_threads = numberOfThreads;  // Used in the pragma declaration

#pragma omp parallel num_threads(num_threads)
    {
        int threadNumberOfOperations = numberOfOperations / omp_get_num_threads();
        int innerCircleCount = 0;

        for (int i = 0; i < threadNumberOfOperations; i++) {
            float x = (float) rand() / (float) RAND_MAX;
            float y = (float) rand() / (float) RAND_MAX;
            bool isInCircle = ParallelPiCalculations::FindIsInCircle(x, y);

            if (isInCircle) {
                ++innerCircleCount;

            }
        }

#pragma omp atomic
        circle_count += innerCircleCount;
    }

    float PI = 4 * (float) circle_count / (float) numberOfOperations;
    return PI;
}

bool ParallelPiCalculations::FindIsInCircle(float x, float y) {
    double distance = sqrt(pow(x, 2) + pow(y, 2));
    return distance < 1;
}