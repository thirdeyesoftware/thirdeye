/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class telesync_qov_QualityOfVoiceManager */

#ifndef _Included_telesync_qov_QualityOfVoiceManager
#define _Included_telesync_qov_QualityOfVoiceManager
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     telesync_qov_QualityOfVoiceManager
 * Method:    compare
 * Signature: (JLjava/lang/String;Ljava/lang/String;Ljava/lang/String;)[F
 */
JNIEXPORT jfloatArray JNICALL Java_telesync_qov_QualityOfVoiceManager_compare__JLjava_lang_String_2Ljava_lang_String_2Ljava_lang_String_2
  (JNIEnv *, jclass, jlong, jstring, jstring, jstring);

/*
 * Class:     telesync_qov_QualityOfVoiceManager
 * Method:    compare
 * Signature: (JLjava/lang/String;Ljava/lang/String;)[F
 */
JNIEXPORT jfloatArray JNICALL Java_telesync_qov_QualityOfVoiceManager_compare__JLjava_lang_String_2Ljava_lang_String_2
  (JNIEnv *, jclass, jlong, jstring, jstring);

/*
 * Class:     telesync_qov_QualityOfVoiceManager
 * Method:    compare
 * Signature: (JLjava/lang/String;Ljava/lang/String;Ljava/lang/String;[F[F)[F
 */
JNIEXPORT jfloatArray JNICALL Java_telesync_qov_QualityOfVoiceManager_compare__JLjava_lang_String_2Ljava_lang_String_2Ljava_lang_String_2_3F_3F
  (JNIEnv *, jclass, jlong, jstring, jstring, jstring, jfloatArray, jfloatArray);

/*
 * Class:     telesync_qov_QualityOfVoiceManager
 * Method:    init
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_telesync_qov_QualityOfVoiceManager_init
  (JNIEnv *, jclass);


void init();

#ifdef __cplusplus
}
#endif
#endif
