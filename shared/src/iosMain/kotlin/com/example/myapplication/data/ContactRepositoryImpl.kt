package com.example.myapplication.data

import com.example.myapplication.domain.Contact
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import platform.Contacts.CNContactFamilyNameKey
import platform.Contacts.CNContactFetchRequest
import platform.Contacts.CNContactGivenNameKey
import platform.Contacts.CNContactPhoneNumbersKey
import platform.Contacts.CNContactStore
import platform.Contacts.CNLabeledValue
import platform.Contacts.CNPhoneNumber

class ContactRepositoryImpl : ContactRepository {

    @OptIn(ExperimentalForeignApi::class)
    override suspend fun getContacts(): List<Contact> {
        if (!requestContactsPermission()) {
            return emptyList()
        }
        val contactStore = CNContactStore()
        val keysToFetch = listOf(
            CNContactGivenNameKey,
            CNContactFamilyNameKey,
            CNContactPhoneNumbersKey
        ) as List<Any>
        val fetchRequest = CNContactFetchRequest(keysToFetch)

        val contacts = mutableListOf<Contact>()

        try {
            withContext( Dispatchers.IO) {
                contactStore.enumerateContactsWithFetchRequest(fetchRequest, null) { contact, _ ->
                    val name = "${contact?.givenName} ${contact?.familyName}"
                    val phoneNumber = contact?.phoneNumbers?.firstOrNull()?.let { labeledValue ->
                        (labeledValue as? CNLabeledValue)?.value as? CNPhoneNumber
                    }?.stringValue ?: "No number"
                    contacts.add(Contact(name, phoneNumber))
                }
            }
        } catch (e: Exception) {
            println("Error fetching contacts: ${e.message}")
        }

        return contacts
    }
}