package com.maths.robostick

data class StudentCredentials(val name : String , val emailOrUsername : String,
                              val password :String , val fatherName :String , val phoneNumber :String,
                              val schoolName :String , val grade :String , val address :String , val imgUrl :String) {
    constructor(): this("" , "" , "" ,"" , "" , "" , "" ,"","")

}