package fi.tiituste.elisaspaceflightnews.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import fi.tiituste.elisaspaceflightnews.R
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@ExperimentalCoilApi
@Composable
fun ArticleListView(articleListViewModel: ArticleListViewModel = viewModel()) {
    val isRefreshing by articleListViewModel.isRefreshing.collectAsState()
    val articles = articleListViewModel.articles
    articleListViewModel.getArticles()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        modifier = Modifier.semantics { heading() })
                },
            )
        },
        content = {
            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing),
                onRefresh = { articleListViewModel.getArticles() },
            ) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(articles.value ?: listOf()) { article ->
                        if (article.title != null) {
                            Row(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .fillMaxWidth()
                                    .height(156.dp)
                            ) {
                                Image(
                                    painter = rememberImagePainter(article.imageUrl),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(156.dp)
                                        .padding(4.dp)
                                )
                                Column(
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .fillMaxSize(),
                                    verticalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    Text(article.title!!)
                                    Text(article.updatedAt!!)
                                    Icon(
                                        imageVector = Icons.Filled.ArrowForward,
                                        contentDescription = null,
                                        modifier = Modifier.align(CenterHorizontally)
                                    )
                                }
                            }
                        }
                        Divider()
                    }
                }
            }
        }
    )
    LaunchedEffect(Unit) {
        articleListViewModel.getArticles()
    }
}