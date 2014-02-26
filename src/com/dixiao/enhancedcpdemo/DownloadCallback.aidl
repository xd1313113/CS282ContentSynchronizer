package com.dixiao.enhancedcpdemo;

interface DownloadCallback {
    /** The callback method to send imagePath back the thread */
   oneway void sendPath (in String imageFilePath); 
}