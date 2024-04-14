package pl.kacper.misterski.turbinetest.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import pl.kacper.misterski.turbinetest.domain.api.Post
import pl.kacper.misterski.turbinetest.domain.api.PostService
import javax.inject.Inject

class PostsRepositoryImpl
    @Inject
    constructor(private val postService: PostService) : PostsRepository {
        private val _postSharedFlow = MutableSharedFlow<Post>(replay = 0)
        val postSharedFlow = _postSharedFlow.asSharedFlow()

        override fun getPosts() =
            flow {
                val response = postService.fetchPosts()

                if (response.isSuccessful) {
                    emit(response.body().orEmpty())
                } else {
                    throw Exception("getPosts failure: ${response.code()}")
                }
            }.flowOn(Dispatchers.IO)

        fun getPostsCombine() =
            kotlinx.coroutines.flow.combine(
                getPosts(),
                getPosts(),
            ) { postA, postB ->
                buildList {
                    add(postA)
                    add(postB)
                }
            }

        suspend fun setPostToSharedFlow(post: Post) = _postSharedFlow.emit(post)
    }
