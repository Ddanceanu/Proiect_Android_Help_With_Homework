package com.example.proiecthelpwithhomework

data class Homework(
    var title: String,
    var subject: String,
    var description: String,
    var duration: String,
    var isSolved: Boolean = false,
    var postedBy: String = "Utilizator",
    var solvedBy: String? = null
)