package pl.kacper.misterski.turbinetest.domain.api


data class Post(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
)