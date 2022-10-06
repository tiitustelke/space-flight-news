package fi.tiituste.elisaspaceflightnews.view.articleview

import androidx.lifecycle.ViewModel
import fi.tiituste.elisaspaceflightnews.model.ArticleModel
import fi.tiituste.elisaspaceflightnews.repository.ShareRepository

class ArticleViewModel: ViewModel() {
    private val shareRepository = ShareRepository()

    fun shareArticle(article: ArticleModel) = shareRepository.shareArticle(article)
}