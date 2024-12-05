package com.example.myapplication.data

import android.content.ContentResolver
import android.database.Cursor
import android.provider.ContactsContract
import com.example.myapplication.domain.Contact

class ContactRepositoryImpl(private val contentResolver: ContentResolver) : ContactRepository {
    override suspend fun getContacts(): List<Contact> {
        val contactsList = mutableListOf<Contact>()

        val cursor: Cursor? = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER),
            null,
            null,
            null
        )

        cursor?.use {
            val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

            if (nameIndex >= 0 && numberIndex >= 0) { // Ensure valid column indices
                while (it.moveToNext()) {
                    val name = it.getString(nameIndex) ?: "Unknown Name"
                    val phoneNumber = it.getString(numberIndex) ?: "Unknown Number"
                    contactsList.add(Contact(name, phoneNumber))
                }
            }
        }

        return contactsList
    }
}