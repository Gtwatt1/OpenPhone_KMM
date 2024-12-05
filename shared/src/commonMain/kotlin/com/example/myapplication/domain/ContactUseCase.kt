package com.example.myapplication.domain

interface ContactUseCase {
    suspend fun getContacts(): Result<List<Contact>>
}