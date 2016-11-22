# Copyright (C) 2012 Motisan Radu
#
# radu.motisan@gmail.com
#
# All rights reserved.
# 

LOCAL_PATH:= $(call my-dir)


# second lib, which will depend on and include the first one
include $(CLEAR_VARS)
LOCAL_LDLIBS := -llog
LOCAL_MODULE    := input
LOCAL_SRC_FILES := input.c
LOCAL_STATIC_LIBRARIES := 
include $(BUILD_SHARED_LIBRARY)
