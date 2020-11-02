package com.example.movieapp.data

data class Movie (
    val id: String,
    var nume: String,
    var regizor: String,
    var releasedate: String,
    var released: String
) {
    override fun toString(): String {
        return "'$nume', '$regizor', '$releasedate', '$released')"
    }
}