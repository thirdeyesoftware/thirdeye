// EchoWrapper.cpp : Defines the entry point for the DLL application.
//

#include "stdafx.h" 

#include <iostream.h>
#include <stdio.h>
#include <conio.h>

#include "com_jadi_echo_EchoWrapper.h"



typedef void (CALLBACK* LPFNDLLFUNC1)(double*,int*,int*);
LPFNDLLFUNC1 lpfnDllFunc1; //function pointer to echo function.

HINSTANCE hDLL = NULL;


JNIEXPORT jint JNICALL
 JNI_OnLoad (JavaVM *jvm, void *reserved) {

	/*hDLL = loadEchoDLL("echodll_v1.dll");

	if (hDLL == NULL) {
		return JNI_ERR;
	}
	printf("library loaded...\n");
	*/
	return JNI_VERSION_1_2;



}

JNIEXPORT void JNICALL 
	JNI_OnUnload(JavaVM *jvm, void *reserved) {
	
	if (hDLL != NULL) {
		releaseEchoDLL(hDLL);
		printf("jni unloaded...");
	}


}

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

JNIEXPORT void JNICALL Java_EchoWrapper_test
	(JNIEnv *env, jclass cls) {

 #define MAXMODULE 50

 typedef void (WINAPI*cfunc)();

 cfunc NumberList;
    
       HINSTANCE hLib=LoadLibrary("DLLTEST.DLL");


       if(hLib==NULL) {

            cout << "Unable to load library!" << endl;
            getch();
            return;
       }

       char mod[MAXMODULE];

       GetModuleFileName((HMODULE)hLib, (LPTSTR)mod, MAXMODULE);
       cout << "Library loaded: " << mod << endl;


       NumberList=(cfunc)GetProcAddress((HMODULE)hLib, "doit");
       
       if((NumberList==NULL)) {

            cout << "Unable to load function(s)." << endl;
            FreeLibrary((HMODULE)hLib);
            return;
       }

       NumberList();
       
       FreeLibrary((HMODULE)hLib);

       getch();


}


JNIEXPORT void JNICALL Java_com_jadi_echo_EchoWrapper_analyzeEcho
	(JNIEnv *env, jclass cls, jdoubleArray pOutput, jintArray pInput, jint level) {
	
	
	/* jclass exceptionClass */
	jclass exceptionClass;
	env->ExceptionDescribe();
	env->ExceptionClear();
	exceptionClass = env->FindClass("java/lang/IllegalArgumentException");
	

	jsize arraySize = env->GetArrayLength(pInput);
	if (arraySize < 32000) {
		if (exceptionClass == 0) {
			return;
		}
		else {
			env->ThrowNew(exceptionClass, "Input Array must be 32000 elements.");
		}
	}
	
	
	arraySize = env->GetArrayLength(pOutput);
	if (arraySize < 11) {
		if (exceptionClass == 0) {
			return;
		}
		else {
			env->ThrowNew(exceptionClass, "Output Array must be 11 elements.");
		}
	}
	jboolean copy = JNI_FALSE;
	// We need to get pointers to the array elements first. 
	jdouble pOutputElements[11];

	double* output = (double*)env->GetPrimitiveArrayCritical(pOutput, &copy);

	//int newInput[32000];

	//memset(pOutputElements, 0, sizeof(pOutputElements));
	//memset(newInput, 0, sizeof(newInput));


	jint* pInputElements = (jint*) env->GetPrimitiveArrayCritical(pInput, &copy);
	
	/*for (int i = 0; i < 32000; i++) {
		newInput[i] = (int)pInputElements[i];
	}

	*/
	double param1[11];
	for (int i = 0; i<11;i++) {
		param1[i] = 0;
	}

	int param2[32000];
	//memset(param1, 0, sizeof(param1));
	//memset(param2, 0, sizeof(param2));
	int param3;

	long l = 2;

	param3 = (int)l;


	for (int x = 0; x < 32000; x++) {
		param2[x] = -100;
	}

	BOOL b  = doAnalyze(param1, param2, param3);

	

	if (!b) {

		env->ReleasePrimitiveArrayCritical(pInput, pInputElements, JNI_ABORT);
		exceptionClass = env->FindClass("java/lang/RuntimeException");
		env->ThrowNew(exceptionClass, "An error occured while attempting the Echo analysis.");
	
	}
	else {

		//env->SetDoubleArrayRegion(pOutput, 0, 11, pOutputElements);
		// release memory associated with array elements... 
		env->ReleasePrimitiveArrayCritical(pInput, pInputElements, JNI_ABORT);

	}
	return;

	

}


_declspec(dllexport) BOOL doAnalyze(double* output, int* input, int level) {
	
 	hDLL = loadEchoDLL("echodll_v1.dll");
 
 	LPFNDLLFUNC1 lpfnDllFunc1; //function pointer to echo function.
 	
 	BOOL retVal = FALSE;
 
 	if (hDLL != NULL)
 	{
 	   lpfnDllFunc1 = 
 			(LPFNDLLFUNC1)GetProcAddress(hDLL, "_ed_v1@12");
 
 	   if (!lpfnDllFunc1)
 	   {
 		  retVal = FALSE;
 		  
 	   }
 	   else
 	   {
 			// call the function
			int l = 2;
 			lpfnDllFunc1(output, input, &l);
			printf("\nsuccess\n");
			for (int i =0;i < 11;i++) {
				printf("%d\n",output[i]);
			}
 			retVal = TRUE;
 	   }
 	
 	} else {
 
 		retVal = FALSE;
 	
 	}
 
 	if (retVal == FALSE) {
 		// do something...
 	}
 	
 	releaseEchoDLL(hDLL);
 	
 	return retVal;
 
 }

HINSTANCE loadEchoDLL(LPCSTR library) {
	
	
	return LoadLibrary(library);


}

void releaseEchoDLL(HINSTANCE hDLL) {

	FreeLibrary(hDLL);

}



