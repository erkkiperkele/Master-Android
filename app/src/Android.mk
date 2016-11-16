LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_CFLAGS += -fopenmp
LOCAL_LDFLAGS += -fopenmp