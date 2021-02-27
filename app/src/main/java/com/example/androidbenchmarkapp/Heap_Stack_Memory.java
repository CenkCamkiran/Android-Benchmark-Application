package com.example.androidbenchmarkapp;

import android.util.Log;

public class Heap_Stack_Memory {

    public double start, finish, elapsedTimeStack, elapsedTimeHeap;

    //main() method thread creates space in stack memory
    public void Main() {

        start = System.nanoTime();
        // primitive datatype created inside main() method space in stack memory
        int i=1;
        finish = System.nanoTime();
        elapsedTimeStack = (finish - start) / 1000000;


        start = System.nanoTime();
        // Heap_Stack Object created in heap memory and its refference objnew in stack memory
        Heap_Stack_Memory objnew = new Heap_Stack_Memory();
        finish = System.nanoTime();
        elapsedTimeHeap = (finish - start) / 1000000;

        // New space for foo() method created in the top of the stack memory
        objnew.foo(objnew);

    }

    private void foo(Object p) {

        //  String for p.toString() is created in String Pool and refference str created in stack memory
        String str = p.toString();

        Log.i("Object", str);
    }

}
