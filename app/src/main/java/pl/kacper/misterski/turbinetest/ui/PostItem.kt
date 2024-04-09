package pl.kacper.misterski.turbinetest.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import pl.kacper.misterski.turbinetest.domain.api.Post
import pl.kacper.misterski.turbinetest.ui.theme.TurbineTestTheme

@Composable
fun PostItem(
    modifier: Modifier,
    post: Post,
) {
    Column(modifier) {
        Text(text = post.title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(text = post.body)
    }
}

@Preview
@Composable
private fun PostItemPreview() {
    TurbineTestTheme {
        PostItem(
            modifier = Modifier,
            post =
                Post(
                    userId = 0,
                    id = 0,
                    title = "title",
                    body = "message",
                ),
        )
    }
}
