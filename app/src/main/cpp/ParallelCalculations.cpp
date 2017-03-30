#include <cstdlib>
#include <cmath>

//#include <omp.h>      // TODO: Fix openmp!
#include "ParallelCalculations.h"


float ParallelCalculations::CalculateParallelPi(int numberOfOperations, int numberOfThreads) {
    int circle_count = 0;
    int num_threads = numberOfThreads;  // Used in the pragma declaration

    // TODO: Fix openmp! (pragma simply ignored at the moment)
//#pragma omp parallel num_threads(num_threads)
    {
         //int actualThreadNumber = omp_get_num_threads();   // Fix Openmp!

        //#pragma omp for
        for (int i = 0; i < numberOfOperations; i++) {
            float x = (float) rand() / (float) RAND_MAX;
            float y = (float) rand() / (float) RAND_MAX;
            bool isInCircle = ParallelCalculations::FindIsInCircle(x, y);

            if (isInCircle) {
                ++circle_count;
            }
        }
    }

    float PI = 4 * (float) circle_count / (float) numberOfOperations;
    return PI;
}

bool ParallelCalculations::FindIsInCircle(float x, float y) {
    double distance = sqrt(pow(x, 2) + pow(y, 2));
    return distance < 1;
}