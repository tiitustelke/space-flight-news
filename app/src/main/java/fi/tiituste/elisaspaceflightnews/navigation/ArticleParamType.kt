package fi.tiituste.elisaspaceflightnews.navigation

import android.os.Bundle
import androidx.navigation.NavType
import com.google.gson.Gson
import fi.tiituste.elisaspaceflightnews.model.ArticleModel

class ArticleParamType : NavType<ArticleModel>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): ArticleModel? {
        return bundle.getParcelable(key)
    }

    override fun parseValue(value: String): ArticleModel {
        return Gson().fromJson(value, ArticleModel::class.java)
    }

    override fun put(bundle: Bundle, key: String, value: ArticleModel) {
        bundle.putParcelable(key, value)
    }
}