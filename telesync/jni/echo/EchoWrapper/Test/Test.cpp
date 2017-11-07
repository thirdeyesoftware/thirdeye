// Test.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include <windows.h>
#include <iostream.h>
#include <stdio.h>
#include <conio.h>
#include <fcntl.h>
#include "ed_v1.h"


#include <stdlib.h>


typedef void (_stdcall* LPFNDLLFUNC1)(double*,int*,int*);

int main(int argc, char* argv[])
{



	double param1[11];
	int param2[32000];

	int *pIn; //= (int*)malloc(32000 * sizeof(int));
	double *pOut; //= (double*)malloc(11 * sizeof(double));

	//memset(param2, 0, 32000 * sizeof(int));
	//memset(param1, 0, 11 * sizeof(double));

	int l;
	l = 0;

	for (int i = 0;i<11;i++) {
		param1[i] = 0;
	}

	int* pLevel;
	pLevel = &l;


	pIn = &param2[0];
	pOut = &param1[0];

	BOOL b  = TRUE;
	
	for (int x = 0; x < 32000; x++) {
		param2[x] = 0;
	}
	l = 2;


	ed_v1(pOut, pIn, pLevel);
	
	if (b) {
		for (x = 0;x < 11;x++) {
			printf("element = %d\n\r",param1[x]);
		}
	}
	
	
	//free(pIn);
	free(pOut);

	return b;

}


BOOL doAnalyze(double* output, int* input, int* level) {

	HINSTANCE hDLL = loadEchoDLL("echodll_v1.dll");

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
			//lpfnDllFunc1(output, input, level);
		   ed_v1(output, input, level);



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

