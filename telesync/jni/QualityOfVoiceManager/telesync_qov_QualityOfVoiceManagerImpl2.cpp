// QualityOfVoiceManagerImpl.cpp : Defines the entry point for the DLL application.
//


#include "telesync_qov_QualityOfVoiceManager.h"

/* JNI Call compare() method
	@args - jobject (INPUTDATA reference)
	      - jobject (INPUTDATA degraded)
		  - jobject (RETURNDATA results);
*/
JNIEXPORT jlong JNICALL Java_telesync_qov_QualityOfVoiceManager_compare
  (JNIEnv *, jclass, jlong, jobject, jobject, jobject) {

	jlong ret = 20612;
	return ret;

}


