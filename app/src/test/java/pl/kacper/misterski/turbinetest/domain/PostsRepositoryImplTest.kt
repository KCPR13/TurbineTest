package pl.kacper.misterski.turbinetest.domain

import app.cash.turbine.test
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.mockito.Mockito.mock
import pl.kacper.misterski.turbinetest.domain.api.Post
import pl.kacper.misterski.turbinetest.domain.api.PostService
import retrofit2.Response
import kotlin.time.Duration.Companion.seconds

class PostsRepositoryImplTest {
    @Test
    fun getPostsSuccessTest() =
        runBlocking {
            // GIVEN
            val returnedValue = listOf(Post(0, 0, "", ""))
            val postService =
                object : PostService {
                    override suspend fun fetchPosts(): Response<List<Post>> {
                        return Response.success(returnedValue)
                    }
                }
            val postsRepositoryImpl = PostsRepositoryImpl(postService)

            // THEN
            postsRepositoryImpl.getPosts().test {
                Assertions.assertEquals(returnedValue, awaitItem())
                awaitComplete()
            }
        }

    @Test
    fun getPostsFailureTest() =
        runBlocking {
            // GIVEN
            val mockResponseBody = mock(ResponseBody::class.java)
            val postService =
                object : PostService {
                    override suspend fun fetchPosts(): Response<List<Post>> {
                        return Response.error(404, mockResponseBody)
                    }
                }
            val postsRepositoryImpl = PostsRepositoryImpl(postService)

            // THEN
            postsRepositoryImpl.getPosts().test {
                awaitError()
            }
        }

    @Test
    fun getPostsCombineSuccessTest() =
        runBlocking {
            // GIVEN
            val returnedValue = listOf(Post(0, 0, "", ""))
            val postService =
                object : PostService {
                    override suspend fun fetchPosts(): Response<List<Post>> {
                        return Response.success(returnedValue)
                    }
                }
            val postsRepositoryImpl = PostsRepositoryImpl(postService)
            val result =
                buildList {
                    add(returnedValue)
                    add(returnedValue)
                }

            // THEN
            postsRepositoryImpl.getPostsCombine().test {
                Assertions.assertEquals(result, awaitItem())
                awaitComplete()
            }
        }

    @Test
    fun getPostsCombineFailureTest() =
        runBlocking {
            // GIVEN
            val mockResponseBody = mock(ResponseBody::class.java)
            val postService =
                object : PostService {
                    override suspend fun fetchPosts(): Response<List<Post>> {
                        return Response.error(404, mockResponseBody)
                    }
                }
            val postsRepositoryImpl = PostsRepositoryImpl(postService)

            // THEN
            postsRepositoryImpl.getPosts().test {
                awaitError()
            }
        }

    @Test
    fun getPostsShareFlowTest() =
        runBlocking {
            // GIVEN
            val mockResponseBody = mock(ResponseBody::class.java)
            val postService =
                object : PostService {
                    override suspend fun fetchPosts(): Response<List<Post>> {
                        return Response.error(404, mockResponseBody)
                    }
                }
            val postsRepositoryImpl = PostsRepositoryImpl(postService)
            val post = Post(0, 0, "", "")

            // THEN

            postsRepositoryImpl.postSharedFlow.test(timeout = 2.seconds) {
                postsRepositoryImpl.setPostToSharedFlow(post)
                Assertions.assertEquals(post, awaitItem())
            }
        }
}
