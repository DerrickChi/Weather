#include <jni.h>
#include <string>

extern "C"
jfloat
Java_edu_ucla_derrickchang_weather_MainActivity_cfConvertJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    jfloat result = 1.0;
    return result;
    //return env->NewStringUTF(hello.c_str());
}
