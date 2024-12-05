package com.example.myapplication.data

import platform.Contacts.CNAuthorizationStatusAuthorized
import platform.Contacts.CNContactStore
import platform.Contacts.CNAuthorizationStatusNotDetermined
import platform.Contacts.CNEntityType
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

actual suspend fun requestContactsPermission(): Boolean {
    val contactStore = CNContactStore()
    val status = CNContactStore.authorizationStatusForEntityType(CNEntityType.CNEntityTypeContacts)

    return when (status) {
        CNAuthorizationStatusNotDetermined -> {
            suspendCoroutine<Boolean> { continuation ->
                contactStore.requestAccessForEntityType(CNEntityType.CNEntityTypeContacts) { granted, error ->
                    if (error != null) {
                        continuation.resume(false)
                    } else {
                        continuation.resume(true)

                    }
                }
            }

        }
        CNAuthorizationStatusAuthorized -> true
        else -> false
    }
}
