#include <jni.h>
#include <string>

extern "C"
jdoubleArray
Java_edu_ucla_derrickchang_weather_MainActivity_c2fJNI(
        JNIEnv *env,
        jobject /* this */,
        jdoubleArray inJNIArray) {

    // Step 1: Convert the incoming JNI jdoublearray to C's jdouble[]
    jdouble *inCArray = env->GetDoubleArrayElements(inJNIArray, NULL);
    if (NULL == inCArray) return NULL;
    jsize length = env->GetArrayLength(inJNIArray);

    // Step 2: Perform its intended operations
    int i;
    for (i = 0; i < length; i++) {
        inCArray[i] = 1.8 * inCArray[i] + 32;
    }
    //jdouble average = (jdouble)sum / length;
    //env->ReleaseDoubleArrayElements(inJNIArray, inCArray, 0); // release resources

   // jdouble outCArray[] = {sum, average};


    // Step 3: Convert the C's Native jdouble[] to JNI jdoublearray, and return
    jdoubleArray outJNIArray = env->NewDoubleArray(length);  // allocate
    if (NULL == outJNIArray) return NULL;
    env->SetDoubleArrayRegion(outJNIArray, 0 , length, inCArray);  // copy
    return outJNIArray;
}
