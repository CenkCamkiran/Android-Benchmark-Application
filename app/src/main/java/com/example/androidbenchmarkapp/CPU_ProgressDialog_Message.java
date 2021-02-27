package com.example.androidbenchmarkapp;

import java.util.ArrayList;
import java.util.List;

public class CPU_ProgressDialog_Message {

    List<String> message_list = new ArrayList<>();

    protected void addMessage() {
        message_list.add("Integer Calculation");
        message_list.add("Matrix Calculation");
        message_list.add("Floating Point Calculation");
        message_list.add("QuickSort");
        message_list.add("Dijkstra");
        message_list.add("FFT");
        message_list.add("HTML Parser");
        message_list.add("AES");
        message_list.add("QRCode Scan");
        message_list.add("Canny");
        message_list.add("Gaussian Blur");
    }
}
