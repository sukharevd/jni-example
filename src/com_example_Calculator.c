#include <stdlib.h>
#include <jni.h>
#include "com_example_Calculator.h"

JNIEXPORT jobjectArray JNICALL Java_com_example_Calculator_multiply
  (JNIEnv* env, jobject obj, jobjectArray a, jobjectArray b) {

     jint size = (*env)->GetArrayLength(env, a);
     
     // COPY MATRIX B TO BUFFER
     int k;
     jint** buf_b = malloc(size * sizeof(jint*));
     for (k = 0; k < size; k++) {                                  // copy b[k] to buffer
         buf_b[k] = (jint*)malloc(size * sizeof(jint));
         jintArray b_k = (*env)->GetObjectArrayElement(env, b, k); // fetch b[k] aka b_k
         (*env)->GetIntArrayRegion(env, b_k, 0, size, buf_b[k]);   // copy b_k to buffer
         (*env)->DeleteLocalRef(env, b_k);                         // delete references b_k
     }

     // CREATE RESULT ARRAY
     jclass intArrCls = (*env)->FindClass(env, "[I");              // find int[] class
     if (intArrCls == NULL)
         return NULL; /* exception thrown */                       // return NULL if wasn't found
     jobjectArray result =                                         // create array of int[]
                           (*env)->NewObjectArray(env, size, intArrCls, NULL);
     if (result == NULL)
         return NULL; /* out of memory error thrown */

     // MULTIPLY MATRICES
     int i;
     jint* buf_a_i = (jint*)malloc(size * sizeof(jint));
     jint* buf_res_row = (jint*)malloc(size * sizeof(jint));
     for (i = 0; i < size; i++) {
         // COPY I-TH ROW FROM A TO BUFFER
         jintArray a_i = (*env)->GetObjectArrayElement(env, a, i);
         (*env)->GetIntArrayRegion(env, a_i, 0, size, buf_a_i);
         (*env)->DeleteLocalRef(env, a_i);
         
         // CALCULATE I-TH ROW OF RESULT
         int j;
         for (j = 0; j < size; j++) {
             buf_res_row[j] = 0;
             for (k = 0; k < size; k++) {
                 buf_res_row[j] += buf_a_i[k] * buf_b[k][j];
             }
         }

         // COPY I-TH ROW TO RESULT OBJECT
         jintArray res_row = (*env)->NewIntArray(env, size);
         if (res_row == NULL)
             return NULL; /* out of memory error thrown */
         (*env)->SetIntArrayRegion(env, res_row, 0, size, buf_res_row); // firstly copy to new array object
         (*env)->SetObjectArrayElement(env, result, i, res_row);    // and then to matrix
         (*env)->DeleteLocalRef(env, res_row);
     }
     
     // CLEAN UP
     free(buf_res_row);
     free(buf_a_i);
     for (k = 0; k < size; k++)
         free(buf_b[k]);
     free(buf_b);
     
     return result;
}
