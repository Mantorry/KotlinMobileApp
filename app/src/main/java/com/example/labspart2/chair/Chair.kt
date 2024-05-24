package com.example.labspart2.chair

class Chair {
    var id: Int
    var facultyId: Int
    var code: String
    var chairName: String
    var chairShortName: String
    var facultyShortName: String

    constructor(id: Int, facultyId: Int, code: String, chairName: String,
                chairShortName: String, facultyShortName: String){
        this.id = id
        this.facultyId = facultyId
        this.code = code
        this.chairName = chairName
        this.chairShortName = chairShortName
        this.facultyShortName = facultyShortName
    }
}