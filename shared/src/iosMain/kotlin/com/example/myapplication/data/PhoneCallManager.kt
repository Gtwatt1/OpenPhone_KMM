package com.example.myapplication.data

import platform.Foundation.NSURL
import platform.UIKit.UIApplication

actual fun makePhoneCall(phoneNumber: String) {
    val urlString = "tel://$phoneNumber"
    val url = NSURL.URLWithString(urlString)

    if (url != null && UIApplication.sharedApplication.canOpenURL(url)) {
        UIApplication.sharedApplication.openURL(url)
    }
}
