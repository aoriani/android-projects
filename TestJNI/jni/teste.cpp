#include <jni.h>
#include <sstream>
#include <string>
#include <android/log.h>
#include "com_example_testjni_MainActivity.h"

#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)

using std::string;
using std::stringstream;


const char* LOG_TAG = "TesteNative";

static string generateOutput(int a, int b){
	stringstream buffer;
	buffer<<"The sum of "<<a<<" and "<<b<<" is "<<(a+b);
	return buffer.str();
}



JNIEXPORT jint JNICALL Java_com_example_testjni_MainActivity_sum
  (JNIEnv * env, jobject obj, jint a, jint b){
    jint result =  a+b;
    jclass cls = env->GetObjectClass(obj);
    jmethodID mid = env->GetMethodID(cls, "setTextView", "(I)V");
    if(mid == 0) return 0;
    env->CallVoidMethod(obj, mid, result); 
    
    
    return result;
  }
  
  
