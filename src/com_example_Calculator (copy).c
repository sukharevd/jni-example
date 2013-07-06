#include <jni.h>
#include "com_example_Calculator.h"

JNIEXPORT jobjectArray JNICALL Java_com_example_Calculator_multiply
  (JNIEnv* env, jobject obj, jobjectArray a, jobjectArray b) {

     jint size = (*env)->GetArrayLength(env, a);
	 jobjectArray result;
     int i;
     jclass intArrCls = (*env)->FindClass(env, "[I");
     if (intArrCls == NULL) {
         return NULL; /* exception thrown */
     }

     int k;
     jint** buf_b = malloc(size * sizeof(jint*));
     for (k = 0; k < size; k++) {
			    // get k-row from b
buf_b[k] = (jint*)malloc(size * sizeof(jint));
		        jintArray b_k = (*env)->GetObjectArrayElement(env, b, k);
		        (*env)->GetIntArrayRegion(env, b_k, 0, size, buf_b[k]);
				
				// calculate everything
                        (*env)->DeleteLocalRef(env, b_k);
                        
		    }
     
     result = (*env)->NewObjectArray(env, size, intArrCls, NULL);                                                                                                                                                                                   
     if (result == NULL) {
         return NULL; /* out of memory error thrown */
     }
	 for (i = 0; i < size; i++) {
	     // get i-row from a
		 jintArray a_i = (*env)->GetObjectArrayElement(env, a, i);
		 jint buf_a_i[size];
		 (*env)->GetIntArrayRegion(env, a_i, 0, size, buf_a_i);
		 
		 int j;
		 jint res_row[size];
		 for (j = 0; j < size; j++) {
		    res_row[j] = 0;
		    //int k;
			for (k = 0; k < size; k++) {
			    // calculate everything
                            res_row[j] += buf_a_i[k] * buf_b[k][j];
		    }
		 }
                 (*env)->DeleteLocalRef(env, a_i);

		 jintArray iarr = (*env)->NewIntArray(env, size);
         if (iarr == NULL) {
            return NULL; /* out of memory error thrown */
         }
		 (*env)->SetIntArrayRegion(env, iarr, 0, size, res_row);
		 (*env)->SetObjectArrayElement(env, result, i, iarr);
         (*env)->DeleteLocalRef(env, iarr);
     }
	 return result;
}

