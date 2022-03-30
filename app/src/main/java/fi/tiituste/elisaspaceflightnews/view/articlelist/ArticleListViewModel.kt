package fi.tiituste.elisaspaceflightnews.view.articlelist

import android.app.Application
import androidx.lifecycle.*
import fi.tiituste.elisaspaceflightnews.model.ArticleModel
import fi.tiituste.elisaspaceflightnews.repository.SpaceFlightNewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ArticleListViewModel(application: Application) : AndroidViewModel(application) {
    private val spaceFlightNewsRepository = SpaceFlightNewsRepository()

    private val _articles = MutableLiveData<List<ArticleModel>?>(null)

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()
    val articles: LiveData<List<ArticleModel>?>
        get() = _articles

    fun getArticles() {
        viewModelScope.launch {
            _isRefreshing.emit(true)
            val articlesResp = withContext(Dispatchers.IO) {
                spaceFlightNewsRepository.getArticles()
            }
            _articles.postValue(articlesResp)
            _isRefreshing.emit(false)
        }
    }
}