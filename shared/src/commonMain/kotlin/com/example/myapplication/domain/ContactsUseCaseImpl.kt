package com.example.myapplication.domain

import com.example.myapplication.data.ContactRepository

class ContactsUseCaseImpl(private val contactsRepository: ContactRepository) : ContactUseCase {

    override suspend fun getContacts(): Result<List<Contact>> {
        return try {
            val contacts = contactsRepository.getContacts()
            Result.success(contacts)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}