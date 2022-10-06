package fi.tiituste.elisaspaceflightnews.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import fi.tiituste.elisaspaceflightnews.model.ArticleModel
import kotlinx.coroutines.suspendCancellableCoroutine

class ShareRepository {
    private val db = Firebase.firestore

    private val _sharedArticles = MutableLiveData<List<ArticleModel>?>(null)
    val sharedArticles: LiveData<List<ArticleModel>?>
        get() = _sharedArticles

    private val collectionRef = db.collection(COLLECTIONPATH)

    private val valueListener =
        EventListener<QuerySnapshot> { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@EventListener
            }

            if (snapshot != null) {
                Log.d(TAG, "Current data: ${snapshot.documents}")
                _sharedArticles.postValue(snapshot.toObjects())
            } else {
                Log.d(TAG, "Current data: null")
            }
        }


    init {
        collectionRef.addSnapshotListener(valueListener)
    }

    fun shareArticle(article: ArticleModel) =
        collectionRef
            .add(article)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }

    suspend fun isArticleShared(id: String): Boolean =
        suspendCancellableCoroutine { continuation ->
            collectionRef
                .orderBy("id")
                .whereEqualTo("id", id)
                .get()
                .addOnSuccessListener {
                    if (continuation.isActive) {
                        continuation.resume(!it.isEmpty) { continuation.cancel() }
                    }
                }
                .addOnFailureListener {
                    Log.e(ShareRepository::class.java.name, it.toString())
                    continuation.cancel(it.cause)
                }
        }

    companion object {
        private val TAG = Companion::class.java.name
        private const val COLLECTIONPATH = "sharedArticles"
    }
}