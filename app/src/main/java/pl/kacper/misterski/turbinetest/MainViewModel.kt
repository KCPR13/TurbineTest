package pl.kacper.misterski.turbinetest

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import pl.kacper.misterski.turbinetest.domain.PostsRepositoryImpl
import pl.kacper.misterski.turbinetest.domain.api.Post
import javax.inject.Inject

sealed class MainState {
    data object Loading : MainState()

    data class Success(val posts: List<Post>) : MainState()

    data class Error(val errorMessage: String) : MainState()
}

@HiltViewModel
class MainViewModel
    @Inject
    constructor(private val postsRepositoryImpl: PostsRepositoryImpl) :
    ViewModel() {
        private val _state = MutableStateFlow<MainState>(MainState.Loading)
        val state = _state.asStateFlow()

        fun fetchPosts() {
            viewModelScope.launch {
                delay(500)
                postsRepositoryImpl.getPosts()
                    .onEach { list ->
                        _state.emit(MainState.Success(list))
                    }
                    .catch { error ->
                        Log.e(TAG, "fetchPosts error", error)
                        _state.emit(MainState.Error("Something went wrong"))
                    }
                    .collect()
            }
        }

        companion object {
            const val TAG = "MainViewModel"
        }
    }
