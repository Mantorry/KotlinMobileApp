package com.example.labspart2.teacher

import android.provider.ContactsContract.CommonDataKinds.Phone

class Teacher {
    var id: Int
    var chairId: Int
    var postId: Int
    var secondName: String
    var firstName: String
    var lastName: String
    var phone: String
    var email: String
    var chairShortName: String
    var postName: String

    constructor(id: Int, chairId: Int, postId: Int, secondName: String,
        firstName: String, lastName: String, phone: String, email: String,
        chairShortName: String, postName: String){
        this.id = id
        this.chairId = chairId
        this.postId = postId
        this.secondName = secondName
        this.firstName = firstName
        this.lastName = lastName
        this.phone = phone
        this.email = email
        this.chairShortName = chairShortName
        this.postName = postName
    }
}