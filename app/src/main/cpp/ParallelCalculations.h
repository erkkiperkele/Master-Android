#ifndef _PARALLELCALCULATIONS_H
#define _PARALLELCALCULATIONS_H

class   ParallelCalculations{

public:
    static float CalculateParallelPi(int numberOfOperations, int numberOfThreads);


private:
    static bool FindIsInCircle(float x, float y);

};


#endif
