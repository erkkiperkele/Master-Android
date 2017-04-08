#include <cstdlib>
#include <cmath>

#include "simple_pi_calculations.h"


float SimplePiCalculations::CalculateSimplePi(int numberOfOperations) {
    int circle_count = 0;

    for (int i = 0; i<numberOfOperations; i++) {
        float x = (float) rand() / (float) RAND_MAX;
        float y = (float) rand() / (float) RAND_MAX;
        bool isInCircle = FindIsInCircle(x, y);

        if (isInCircle) {
            ++circle_count;
        }
    }

    float PI = 4 * (float) circle_count / (float) numberOfOperations;

    return PI;
}

bool SimplePiCalculations::FindIsInCircle(float x, float y) {
    double distance = sqrt(pow(x, 2) + pow(y, 2));
    return distance < 1;
}