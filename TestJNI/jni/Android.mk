LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := teste
LOCAL_SRC_FILES := teste.cpp
LOCAL_LDLIBS    := -llog

include $(BUILD_SHARED_LIBRARY)
