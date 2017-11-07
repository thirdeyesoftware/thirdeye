// QoVImpl.cpp : Defines the entry point for the DLL application.
//

#include "stdafx.h"
#include "telesync_qov_QualityOfVoiceManager.h"
#include "mmsystem.h"
#include "melspa.h"
#include "QoVHandler.h"
#include "stdio.h"

HANDLE g_instance;


BOOL APIENTRY DllMain( HANDLE hModule, 
                       DWORD  ul_reason_for_call, 
                       LPVOID lpReserved
					 )
{
	switch (ul_reason_for_call) {
		case DLL_THREAD_ATTACH:
		case DLL_THREAD_DETACH:
		case DLL_PROCESS_DETACH:
		case DLL_PROCESS_ATTACH:
		{
			g_instance = hModule;
			break;
		}

		
	}
    return TRUE;
}

/* NATIVE METHOD */
/* returns MelSPA return value (long) */

JNIEXPORT jlong JNICALL Java_telesync_qov_QualityOfVoiceManager_compare__JLtelesync_qov_INPUTDATA_2Ltelesync_qov_INPUTDATA_2Ltelesync_qov_RESULTDATA_2
(JNIEnv *env, jclass, jlong type, jobject inputObject, jobject degObject, jobject resObject) {
	/* we must first convert the java objects to MelSPA structures (C++) */
	jlong ret = 0;
	INPUTDATAPTR input, deg;
	RESULTDATA result;
	memset(&input, 0, sizeof(input));
	memset(&result, 0, sizeof(result));
	memset(&deg, 0, sizeof(deg));
	printf("init done\n");

	QoVHandler* handler = new QoVHandler();
	printf("created handler\n");
	handler->convertJInputData(env, inputObject, &input);
	printf("filename = %c",input.FileName);

	handler->convertJInputData(env, degObject, &deg);
	handler->convertJResultData(env, resObject, &result);
	

	ret = MelSPA (PESQ | PSQM | SILENCE_W_2 | P56_FN | PAMS_FN | MIRS_FN | 
		HOLD_FN , &input, &deg, &result);

	return ret;


}
JNIEXPORT jfloatArray JNICALL Java_telesync_qov_QualityOfVoiceManager_compare__JLjava_lang_String_2Ljava_lang_String_2Ljava_lang_String_2
(JNIEnv *env, jclass jClass, jlong type, jstring input, jstring degraded, jstring results) {

	INPUTDATAPTR inputPtr, deg;
	RESULTDATA res;

	memset(&inputPtr, 0, sizeof(INPUTDATAPTR));
	memset(&res, 0, sizeof(RESULTDATA));
	memset(&deg, 0, sizeof(INPUTDATAPTR));

	char str1[MAX_PATH];
	char str2[MAX_PATH];
	char str3[MAX_PATH];
	const char* filename;

	strcpy(str1, filename = env->GetStringUTFChars( input, 0 ));	
	/*RIFF file containing reference data */
	env->ReleaseStringUTFChars(input, filename);

	strcpy(str2, filename = env->GetStringUTFChars(degraded,0));	
	/*RIFF file containing degraded data */
	env->ReleaseStringUTFChars(degraded, filename);
	strcpy(str3, filename = env->GetStringUTFChars(results,0));
	env->ReleaseStringUTFChars(results, filename);

	/*Text file to store results in */

	inputPtr.FileName = str1;	/*set up ptr to filename */
	deg.FileName = str2;	/*set up ptr to filename */
	
	res.rst_filename = str3;
	res.rst_option = 1;	
	
	
	long ret = MelSPA ((long)type , &inputPtr, &deg, &res);
	
	int arraySize = sizeof(res.results) / sizeof(res.results[0]);
	
	jfloatArray retResults = env->NewFloatArray(arraySize);
	env->SetFloatArrayRegion(retResults, 0, arraySize, res.results);


	return retResults;

}

JNIEXPORT jfloatArray JNICALL Java_telesync_qov_QualityOfVoiceManager_compare__JLjava_lang_String_2Ljava_lang_String_2
(JNIEnv *env, jclass jClass, jlong type, jstring reference, jstring degraded) {
	INPUTDATAPTR inputPtr, deg;
	RESULTDATA res;

	memset(&inputPtr, 0, sizeof(INPUTDATAPTR));
	memset(&res, 0, sizeof(RESULTDATA));
	memset(&deg, 0, sizeof(INPUTDATAPTR));

	char str1[MAX_PATH];
	char str2[MAX_PATH];
	char str3[MAX_PATH];
	const char* filename;

	strcpy(str1, filename = env->GetStringUTFChars( reference, 0 ));	
	/*RIFF file containing reference data */
	env->ReleaseStringUTFChars(reference, filename);

	strcpy(str2, filename = env->GetStringUTFChars(degraded,0));	
	/*RIFF file containing degraded data */
	env->ReleaseStringUTFChars(degraded, filename);
	strcpy(str3, "");
	

	/*Text file to store results in */

	inputPtr.FileName = str1;	/*set up ptr to filename */
	deg.FileName = str2;	/*set up ptr to filename */
	res.filename = str3;	/*set up ptr to filename */

	/*the following 3 lines cause an rst file to be stored if PAMS LQ is less than 3.00*/
	
	//res.rst_filename = "c:\\projects\\ts\\results.rst";
	res.rst_option = 0;	/*selects conditional storage*/
		
	long ret = MelSPA ((long)type , &inputPtr, &deg, &res);

	int arraySize = sizeof(res.results) / sizeof(res.results[0]);
	
	jfloatArray retResults = env->NewFloatArray(arraySize);
	env->SetFloatArrayRegion(retResults, 0, arraySize, res.results);


	return retResults;
}



JNIEXPORT void JNICALL Java_telesync_qov_QualityOfVoiceManager_init(JNIEnv *env, jclass jClass) {
	init();
	
}

void init() {
	long ret = MelSPA(0,NULL,NULL,NULL);
}

JNIEXPORT void JNICALL JNI_OnUnload(JavaVM *vm, void *reserved) {
	// frees any resources being held by Malden Library
	//long ret = MelSPA(0,NULL, NULL, NULL);
	init();

}

	