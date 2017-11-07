// QoVHandler.h: interface for the QoVHandler class.
//
//////////////////////////////////////////////////////////////////////
#include "jni.h"
#include "melspa.h"

#if !defined(AFX_QOVHANDLER_H__5F9CC830_6391_412E_A774_8C734E73F83E__INCLUDED_)
#define AFX_QOVHANDLER_H__5F9CC830_6391_412E_A774_8C734E73F83E__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

class QoVHandler  
{
public:
	QoVHandler();
	
	void convertJInputData(JNIEnv* env, jobject jInputObject, INPUTDATAPTR * ptr);
	void convertJResultData(JNIEnv* env, jobject jResultObject, RESULTDATA* data);
	void convertJWaveData(JNIEnv* env, jobject object, WAVEDATA* wavedata);
	void convertJWaveFormatEX( JNIEnv* env, jobject object, WAVEFORMATEX* waveformatex);

	char* convertJString(JNIEnv* env, jfieldID fid, jobject object);

public:
	~QoVHandler();

};

#endif // !defined(AFX_QOVHANDLER_H__5F9CC830_6391_412E_A774_8C734E73F83E__INCLUDED_)
