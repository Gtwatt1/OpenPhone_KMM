package com.example.myapplication.data

import com.example.myapplication.domain.Contact

interface ContactRepository {
    suspend fun getContacts(): List<Contact>
}