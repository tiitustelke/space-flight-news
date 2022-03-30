package fi.tiituste.elisaspaceflightnews.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.annotation.ExperimentalCoilApi
import fi.tiituste.elisaspaceflightnews.model.ArticleModel
import fi.tiituste.elisaspaceflightnews.view.articlelist.ArticleListView
import fi.tiituste.elisaspaceflightnews.view.articleview.ArticleView

@ExperimentalCoilApi
@Composable
fun Navigation() {
    val navController = rememberNavController()
    Scaffold(
        content = { padding ->
            Column(modifier = Modifier.padding(padding)) {
                NavHost(
                    navController = navController,
                    startDestination = NavigationRoutes.ARTICLES
                ) {
                    composable(NavigationRoutes.ARTICLES) { ArticleListView(navController = navController) }
                    composable(
                        route = NavigationRoutes.ARTICLE,
                        arguments = listOf(navArgument("article") {
                            type = ArticleParamType()
                        })
                    ) {
                        val article = it.arguments?.getParcelable<ArticleModel>("article")
                        ArticleView(
                            navController = navController,
                            article
                        )
                    }
                }
            }
        })
}

object NavigationRoutes {
    const val ARTICLES = "articles"
    const val ARTICLE = "article/{article}"
}