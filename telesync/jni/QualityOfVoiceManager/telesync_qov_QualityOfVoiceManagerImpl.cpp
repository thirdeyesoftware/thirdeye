// telesync_qov_QualityOfVoiceManagerImpl.cpp : Defines the entry point for the DLL application.
//

#include "stdafx.h"
#include "telesync_qov_QualityOfVoiceManager.h"
#include "mmsystem.h"
#include "melspa.h"

BOOL APIENTRY DllMain( HANDLE hModule, 
                       DWORD  ul_reason_for_call, 
                       LPVOID lpReserved
					 )
{
    return TRUE;
}

/* JNI Call compare() method
	@args - jobject (INPUTDATA reference)
	      - jobject (INPUTDATA degraded)
		  - jobject (RETURNDATA results);
*/
/* impl details - 
	1) we want to convert telesync.qov.QualityOfVoice.* arguments to c++ equivalents.
	2) once we convert, call to melspa library to get results...
*/

JNIEXPORT jlong JNICALL Java_telesync_qov_QualityOfVoiceManager_compare
  (JNIEnv *env, jclass cls, jlong lng, jobject inputObject, jobject degObject, jobject retObject) {

	INPUTDATAPTR input, degraded;
	RESULTDATA res;
	const char* cstr;

	/* get data from first argument (reference); */
	jfieldID fid;
	
	jclass inputClass, resultsClass;
	inputClass = env->GetObjectClass(inputObject);

	memset(&input,0,sizeof(INPUTDATAPTR));
	memset(&degraded,0,sizeof(INPUTDATAPTR));
	memset(&res,0,sizeof(RESULTDATA));

	fid = env->GetFieldID(inputClass, "mFilename", "Ljava/lang/String");
	jstring jstr;
	jstr = (jstring)env->GetObjectField(inputObject, fid);
	
	cstr = env->GetStringUTFChars(jstr,0);
	strcpy(input.FileName,cstr);

	env->ReleaseStringUTFChars(jstr, cstr);

	jstr = (jstring)env->GetObjectField(degObject, fid);
	cstr = env->GetStringUTFChars(jstr,0);
	strcpy(degraded.FileName,cstr);

	/* results operations */
	resultsClass = env->GetObjectClass(retObject);
	fid = env->GetFieldID(resultsClass, "mFilename", "Ljava/lang/String");
	jstr = (jstring)env->GetObjectField(retObject, fid);
	cstr = env->GetStringUTFChars(jstr,0);
	strcpy(res.filename,cstr);
	


	jlong ret = MelSPA (PESQ | PSQM | SILENCE_W_2 | P56_FN | PAMS_FN | MIRS_FN | 
		HOLD_FN , &input, &degraded, &res);
	return ret;



}

