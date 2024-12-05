package com.example.myapplication.presentation

import com.example.myapplication.domain.Contact
import com.example.myapplication.domain.ContactUseCase
import com.example.myapplication.domain.PhoneCallUseCase
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ContactsViewModel(private val contactsUseCase: ContactUseCase,
                        private val phoneCallUseCase: PhoneCallUseCase) {
    private val viewModelScope = CoroutineScope(Dispatchers.Main)

    private val _contactsState = MutableLiveData<ContactsState>(ContactsState.Loading)
    val contactsState: LiveData<ContactsState> get() = _contactsState

    fun fetchContacts() {
        viewModelScope.launch {
            _contactsState.value = ContactsState.Loading
            val result = contactsUseCase.getContacts()
            _contactsState.value = when {
                result.isSuccess -> ContactsState.Success(result.getOrDefault(emptyList()))
                else -> ContactsState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }

    fun callContact(phoneNumber: String) {
        phoneCallUseCase.execute(phoneNumber)
    }
}

sealed class ContactsState {
    data object Loading : ContactsState()
    data class Success(val contacts: List<Contact>) : ContactsState()
    data class Error(val errorMessage: String) : ContactsState()
}