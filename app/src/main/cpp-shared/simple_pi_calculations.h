#ifndef _SIMPLEPICALCULATIONS_H
#define _SIMPLEPICALCULATIONS_H

class   SimplePiCalculations{

public:
    static float CalculateSimplePi(int numberOfOperations);


private:
    static bool FindIsInCircle(float x, float y);
};


#endif
