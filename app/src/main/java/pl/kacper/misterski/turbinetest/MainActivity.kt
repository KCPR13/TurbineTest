package pl.kacper.misterski.turbinetest

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import pl.kacper.misterski.turbinetest.ui.PostItem
import pl.kacper.misterski.turbinetest.ui.theme.TurbineTestTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TurbineTestTheme {
                val state by viewModel.state.collectAsStateWithLifecycle()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    Box {
                        when (state) {
                            is MainState.Loading ->
                                CircularProgressIndicator(
                                    Modifier
                                        .size(40.dp)
                                        .align(
                                            Alignment.Center,
                                        ),
                                )

                            is MainState.Success -> {
                                LazyColumn(Modifier.fillMaxWidth()) {
                                    items((state as MainState.Success).posts) { item ->
                                        PostItem(modifier = Modifier.padding(16.dp), post = item)
                                    }
                                }
                            }

                            is MainState.Error -> {
                                Toast.makeText(
                                    LocalContext.current,
                                    (state as MainState.Error).errorMessage,
                                    Toast.LENGTH_SHORT,
                                ).show()
                            }
                        }
                    }
                }
            }
        }
        viewModel.fetchPosts()
    }
}
