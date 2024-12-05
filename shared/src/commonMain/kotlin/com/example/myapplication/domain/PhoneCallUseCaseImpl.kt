package com.example.myapplication.domain

import com.example.myapplication.data.makePhoneCall

class PhoneCallUseCaseImpl() : PhoneCallUseCase {
    override fun execute(phoneNumber: String) {
       makePhoneCall(phoneNumber)
    }
}