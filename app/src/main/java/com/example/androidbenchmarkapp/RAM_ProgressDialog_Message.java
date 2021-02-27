package com.example.androidbenchmarkapp;

import java.util.ArrayList;
import java.util.List;

public class RAM_ProgressDialog_Message {

    List<String> message_list = new ArrayList<>();

    protected void addMessage() {

        message_list.add("Heap-Stack Memory Allocation");
        message_list.add("Read-Write Speed of Memory-Cache");
    }

}
