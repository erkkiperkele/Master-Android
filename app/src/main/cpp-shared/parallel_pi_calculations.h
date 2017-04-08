#ifndef _PARALLELPICALCULATIONS_H
#define _PARALLELPICALCULATIONS_H

class   ParallelPiCalculations{

public:
    static float CalculateParallelPi(int numberOfOperations, int numberOfThreads);


private:
    static bool FindIsInCircle(float x, float y);

};


#endif
