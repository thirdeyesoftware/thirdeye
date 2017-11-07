// QoVHandler.cpp: implementation of the QoVHandler class.
//
//////////////////////////////////////////////////////////////////////

#include "stdafx.h"
#include "QoVHandler.h"




//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

QoVHandler::QoVHandler()
{

}

QoVHandler::~QoVHandler()
{

}

/* method handles converting jInputObject to INPUTDATAPTR */
/* arg: jInputObject is java telesync.qov.QualityOfVoice.INPUTDATAPTR */
/* arg: inputPtr is c++ INPUTDATAPTR */

void QoVHandler::convertJInputData( JNIEnv* env, jobject jInputObject, INPUTDATAPTR* inputPtr ) {
	 
	//char *FileName; 		/* file pathname */
	//WAVEDATA inputdata;		/* structure defining array data (see above) */
	//int byteorder;			/* byte order. 0 for LSMS, 1 for MSLS */
	//float scaling;			/* number of volts represented by 32768 */
	//void *prevdata;	
	
	
	jclass inputClass;
	jfieldID fid;
	const char* cstrValue;
	jstring jstrValue;

	fid = env->GetFieldID(inputClass = env->GetObjectClass(jInputObject), 
						  "mFilename", 
						  "Ljava/lang/String");
	
	jstrValue = (jstring) env->GetObjectField(jInputObject, fid);
	cstrValue = env->GetStringUTFChars( jstrValue, 0 );
	// put cstrValue into structure
	strcpy(inputPtr->FileName, cstrValue);
	env->ReleaseStringUTFChars(jstrValue, cstrValue);
	
	WAVEDATA wavedata;
	//init wavedata to zero
	memset(&wavedata,0, sizeof(wavedata));
	
	fid = env->GetFieldID(inputClass, "mWaveData",  "Ltelesync/qov/WAVEDATA");
	jobject object = (jobject)env->GetObjectField(jInputObject, fid);
	
	convertJWaveData(env, object, &wavedata);
	inputPtr->inputdata = wavedata;

	fid = env->GetFieldID(inputClass, "mPreviousData", "J");
	inputPtr->prevdata = (void*) env->GetLongField(jInputObject, fid);

	fid = env->GetFieldID(inputClass, "mByteOrder", "I");
	inputPtr->byteorder = env->GetIntField(jInputObject, fid);

	fid = env->GetFieldID(inputClass, "mScaling", "F");
	inputPtr->scaling = env->GetFloatField(jInputObject, fid);

	return;

}
//convert jobject (QualityOfVoice.WAVEDATA to WAVEDATA(c++)
void QoVHandler::convertJWaveData(JNIEnv* env, jobject object, WAVEDATA* wavedata) {
		//WAVEFORMATEX wfx;		/* structure defining data compatible with RIFF header*/
		//int iExtendedData;
		//char* List;
		//BYTE* Data;				/* pointer to input data array */
		//DWORD dwSize;			/* number of bytes in array */
	jfieldID fid;
	jclass inputClass;

	fid = env->GetFieldID(inputClass = env->GetObjectClass(object), "mWFX", 
						  "Ltelesync/qov/WAVEFORMATEX");
	
	WAVEFORMATEX waveformatex;
	
	convertJWaveFormatEX(env, env->GetObjectField(object, fid), &waveformatex);
	wavedata->wfx = waveformatex;

	fid = env->GetFieldID(inputClass, "mExtendedData", "I");
	wavedata->iExtendedData = env->GetIntField(object, fid);

	fid = env->GetFieldID(inputClass, "mList", "Ljava/lang/String");
	jstring jstr = (jstring)env->GetObjectField(object, fid);
	
	const char* cstr = env->GetStringUTFChars(jstr, 0);

	strcpy(wavedata->List, cstr);
	env->ReleaseStringUTFChars(jstr, cstr);
	
	/*fid = env->GetFieldID(inputClass, "mPData", "J");
	wavedata->Data = env->GetLongField(object, fid);
	
	fid = env->GetFieldID(inputClass, "mSize", "J");
	wavedata->dwSize = env->GetLongField(object, fid);
	*/


	return;

}

void QoVHandler::convertJWaveFormatEX(JNIEnv* env, jobject object, WAVEFORMATEX* waveformatex) {
	
	jfieldID fid;
	jclass inputClass;

	fid = env->GetFieldID(inputClass = env->GetObjectClass(object), "mFormatTag", "I");

	waveformatex->wFormatTag = (int)env->GetIntField(object, fid);
	
	fid = env->GetFieldID(inputClass, "mChannels", "I");
	waveformatex->nChannels = env->GetShortField(object, fid);

	fid = env->GetFieldID(inputClass, "mSamplesPerSec", "J");
	waveformatex->nSamplesPerSec = env->GetLongField(object, fid);

	fid = env->GetFieldID(inputClass, "mAvgBytesPerSec", "J");
	waveformatex->nAvgBytesPerSec = env->GetLongField(object, fid);

	fid = env->GetFieldID(inputClass, "mBlockAlign", "I");
	waveformatex->nBlockAlign = (short)env->GetIntField(object, fid);

	fid = env->GetFieldID(inputClass, "mBitsPerSample", "I");
	waveformatex->wBitsPerSample = (short)env->GetIntField(object, fid);

	fid = env->GetFieldID(inputClass, "mSize", "I");
	waveformatex->cbSize = (short)env->GetIntField(object, fid);

	return;
}

void QoVHandler::convertJResultData( JNIEnv* env, jobject jResultObject, RESULTDATA* data ) {
	
	jfieldID fid;
	jclass inputClass;
	
	fid = env->GetFieldID(inputClass = env->GetObjectClass(jResultObject), "mVersionID",
						  "Ljava/lang/String");
	
	char ver[20];
	strcpy(ver, convertJString(env, fid, jResultObject));

	strcpy(data->versionid, ver);

	/*fid = env->GetFieldID(inputClass, "mCopyright1", "Ljava/lang/String");
	data->copyright1 = convertJString(env, fid, jResultObject);
	fid = env->GetFieldID(inputClass, "mCopyright2", "Ljava/lang/String");
	data->copyright2 = convertJString(env, fid, jResultObject);
	*/
	fid = env->GetFieldID(inputClass, "mDate", "Ljava/lang/String");
	data->date = convertJString(env, fid, jResultObject);

	fid = env->GetFieldID(inputClass, "mTime", "Ljava/lang/String");
	data->time = convertJString(env, fid, jResultObject);

	fid = env->GetFieldID(inputClass, "mDescription1", "Ljava/lang/String");
	data->description1 = convertJString(env, fid, jResultObject);

	fid = env->GetFieldID(inputClass, "mDescription2", "Ljava/lang/String");
	data->description2 = convertJString(env, fid, jResultObject);

	fid = env->GetFieldID(inputClass, "mDegSourceName", "Ljava/lang/String");
	data->Degsourcename = convertJString(env, fid, jResultObject);

	fid = env->GetFieldID(inputClass, "mFilename", "Ljava/lang/String");
	data->filename = convertJString(env, fid, jResultObject);

	fid = env->GetFieldID(inputClass, "mConditionalFilename", "Ljava/lang/String");
	data->conditionalfilename = convertJString(env, fid, jResultObject);

	fid = env->GetFieldID(inputClass, "mAmplitudeUnits", "Ljava/lang/String");
	data->AmplitudeUnits = convertJString(env, fid, jResultObject);

	return;
}
char* QoVHandler::convertJString( JNIEnv* env, jfieldID fid, jobject object) {

	jstring str = (jstring)env->GetObjectField(object, fid);
	const char* cstr = env->GetStringUTFChars(str, 0);
	
	char* ret;
	strcpy(ret, cstr);
	env->ReleaseStringUTFChars(str, cstr);

	return ret;

}
