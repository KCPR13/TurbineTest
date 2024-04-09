package pl.kacper.misterski.turbinetest.domain.api

import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.GET

interface PostService {
    @GET("posts")
    suspend fun fetchPosts(): Response<List<Post>>
}