#include <jni.h>
#include "com_example_testjni_MainActivity.h"

JNIEXPORT jint JNICALL Java_com_example_testjni_MainActivity_sum
  (JNIEnv * env, jobject obj, jint a, jint b){
    jint result =  a+b;
    jclass cls = (*env)->GetObjectClass(env, obj);
    jmethodID mid = (*env)->GetMethodID(env, cls, "setTextView", "(I)V");
    if(mid == 0) return 0;
    (*env)->CallVoidMethod(env, obj, mid, result); 
    
    
    return result;
  }
  
  
