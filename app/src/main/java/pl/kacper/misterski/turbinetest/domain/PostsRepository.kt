package pl.kacper.misterski.turbinetest.domain

import kotlinx.coroutines.flow.Flow
import pl.kacper.misterski.turbinetest.domain.api.Post

interface PostsRepository {
   fun getPosts() : Flow<List<Post>>
}


