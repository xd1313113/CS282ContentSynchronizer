package com.dixiao.enhancedcpdemo;

import com.dixiao.enhancedcpdemo.DownloadCallback;

interface DownloadRequest {
    /** the remote method in which the image is downloaded */
    oneway void downloadImage (in Uri uri, in DownloadCallback callback);
}