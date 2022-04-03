package fi.tiituste.elisaspaceflightnews.view.articleview

import android.content.ClipData
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import fi.tiituste.elisaspaceflightnews.R
import fi.tiituste.elisaspaceflightnews.model.ArticleModel
import fi.tiituste.elisaspaceflightnews.util.DateUtil
import java.time.format.FormatStyle

@ExperimentalCoilApi
@Composable
fun ArticleView(navController: NavController, article: ArticleModel?) {
    val configuration = LocalConfiguration.current
    val uriHandler = LocalUriHandler.current

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    text = stringResource(id = R.string.app_name),
                    modifier = Modifier.semantics { heading() })
            },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                })
        },
        content = {
            if (article != null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .scrollable(rememberScrollState(), orientation = Orientation.Vertical)
                        .padding(12.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .fillMaxWidth()
                            .height((configuration.screenHeightDp * 0.3).dp)
                    ) {
                        Image(
                            painter = rememberImagePainter(article.imageUrl),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .fillMaxWidth()
                        )
                    }
                    Column(
                        verticalArrangement = Arrangement.Top,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = article.title ?: "",
                            style = MaterialTheme.typography.h4,
                            modifier = Modifier
                                .padding(top = 12.dp)
                                .clickable { article.url?.let { url -> uriHandler.openUri(url) } }
                        )
                        Text(
                            text = "${stringResource(id = R.string.published)}: ${
                                DateUtil.getFormattedDate(
                                    article.publishedAt,
                                    FormatStyle.MEDIUM
                                )
                            }",
                            style = MaterialTheme.typography.caption,
                            modifier = Modifier.padding(8.dp, top = 16.dp)
                        )
                        Text(
                            text = "${stringResource(id = R.string.updated)} ${
                                DateUtil.getFormattedDate(
                                    article.updatedAt,
                                    FormatStyle.MEDIUM
                                )
                            }",
                            style = MaterialTheme.typography.caption,
                            modifier = Modifier.padding(8.dp)
                        )
                        Text(
                            text = article.summary ?: "",
                            style = MaterialTheme.typography.body1,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                        Text(
                            text = article.newsSite ?: "",
                            style = MaterialTheme.typography.subtitle1,
                            modifier = Modifier.padding(top = 32.dp)
                        )
                    }
                }
            }
        }
    )
}