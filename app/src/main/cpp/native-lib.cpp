#include <jni.h>
#include <string>

extern "C"
jdoubleArray
Java_edu_ucla_derrickchang_weather_MainActivity_c2fJNI(
        JNIEnv *env,
        jobject /* this */,
        jdoubleArray inJNIArray) {

    // Step 1: Convert the incoming JNI jdoubleArray to C's jdouble[]
    jdouble *inCArray = env->GetDoubleArrayElements(inJNIArray, NULL);
    if (NULL == inCArray) return NULL;
    jsize length = env->GetArrayLength(inJNIArray);

    // Step 2: Convert C to F
    for (int i = 0; i < length; i++) {
        inCArray[i] = 1.8 * inCArray[i] + 32;
    }

    // Step 3: Convert the C's Native jdouble[] to JNI jdoubleArray, and return
    jdoubleArray outJNIArray = env->NewDoubleArray(length);  // allocate
    if (NULL == outJNIArray) return NULL;
    env->SetDoubleArrayRegion(outJNIArray, 0 , length, inCArray);  // copy

    return outJNIArray;
}
