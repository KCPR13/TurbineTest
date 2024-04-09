package pl.kacper.misterski.turbinetest.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import pl.kacper.misterski.turbinetest.domain.api.PostService
import javax.inject.Inject

class PostsRepositoryImpl
    @Inject
    constructor(private val postService: PostService) : PostsRepository {
        override fun getPosts() =
            flow {
                val response = postService.fetchPosts()

                if (response.isSuccessful) {
                    emit(response.body().orEmpty())
                } else {
                    throw Exception("getPosts failure: ${response.code()}")
                }
            }.flowOn(Dispatchers.IO)
    }
