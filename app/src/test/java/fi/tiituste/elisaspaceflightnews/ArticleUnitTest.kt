package fi.tiituste.elisaspaceflightnews

import android.app.Application
import android.content.Context
import android.os.Build.VERSION_CODES.Q
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import fi.tiituste.elisaspaceflightnews.api.SpaceFlightApi
import fi.tiituste.elisaspaceflightnews.model.ArticleModel
import fi.tiituste.elisaspaceflightnews.model.Launch
import fi.tiituste.elisaspaceflightnews.repository.SpaceFlightNewsRepository
import fi.tiituste.elisaspaceflightnews.view.articlelist.ArticleListViewModel
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.annotation.Config
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.InputStream
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
@Config(sdk = [Q])
class ExampleUnitTest {
    private lateinit var context: Context
    private lateinit var testList: List<ArticleModel>

    private val mockWebServer = MockWebServer()

    private val client = OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.SECONDS)
        .readTimeout(1, TimeUnit.SECONDS)
        .writeTimeout(1, TimeUnit.SECONDS)
        .build()

    private val api = Retrofit.Builder()
        .baseUrl(mockWebServer.url("/"))
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val service: SpaceFlightApi.Service = api.create(SpaceFlightApi.Service::class.java)

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        //instrumentationContext = InstrumentationRegistry.getInstrumentation().targetContext
        testList = listOf(
            ArticleModel(
                14525,
                "Spacewalks, crewed missions, and science: March in orbit aboard the ISS",
                "https://www.nasaspaceflight.com/2022/04/march-iss/",
                "https://www.nasaspaceflight.com/wp-content/uploads/2022/04/51891661660_d59a90b7a1_o-1170x780.jpg",
                "NASA Spaceflight",
                "The International Space Station (ISS) and its astronauts had a busy March as teams performed spacewalks, welcomed new crewmembers, said goodbye to those returning to Earth, and prepared for the arrival of the first private mission to the iconic orbital outpost. All the while, groundbreaking science research was being performed using the space station’s many labs and instruments.",
                "2022-04-02T18:25:57.000Z",
                "2022-04-02T18:29:37.210Z",
                false,
                listOf(Launch(null)),
                null
            ),
            ArticleModel(
                14524,
                "Commercial BlackSky imaging satellites ride with Rocket Lab",
                "https://spaceflightnow.com/2022/04/02/commercial-blacksky-imaging-satellites-ride-with-rocket-lab/",
                "https://spaceflightnow.com/wp-content/uploads/2022/04/rl25streak-1.jpg",
                "Spaceflight Now",
                "Two microsatellites for BlackSky launched Saturday from New Zealand, riding a Rocket Lab launch vehicle into orbit to join a fleet of commercial eyes supplying imagery to military and civilian users.",
                "2022-04-02T17:39:22.000Z",
                "2022-04-02T17:58:33.370Z",
                false,
                listOf(Launch("d1976b9e-46e5-41d2-9d0d-aa76f6c19ef8", "Launch Library 2")),
                null
            )
        )
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun testConverter() {
        val inputStream: InputStream? = javaClass.classLoader?.getResourceAsStream("api")

        if (inputStream != null) {
            val responseStr = inputStream.bufferedReader().use { it.readText() }
            val response = MockResponse().setBody(responseStr).setResponseCode(200)

            mockWebServer.enqueue(response)

            runBlocking {
                val actual = service.getArticles()

                assertEquals(testList, actual)
            }
        }
    }

    fun testViewModel() {
        val articleListViewModel = ArticleListViewModel(context as Application)
        val spaceFlightNewsRepository = SpaceFlightNewsRepository()
        runBlocking {
            Mockito.`when`(spaceFlightNewsRepository.getArticles())
                .thenReturn(testList)
            articleListViewModel.getArticles()
            val result = articleListViewModel.articles.getOrAwaitValue()
            assertEquals(testList, result)
        }

        articleListViewModel.articles
    }
}

//from https://howtodoandroid.com/mvvm_unit_testing_android/ by Velmurugan
@VisibleForTesting(otherwise = VisibleForTesting.NONE)
fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS,
    afterObserve: () -> Unit = {}
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(o: T?) {
            data = o
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }
    this.observeForever(observer)
    try {
        afterObserve.invoke()
        if (!latch.await(time, timeUnit)) {
            throw TimeoutException("LiveData value was never set.")
        }
    } finally {
        this.removeObserver(observer)
    }
    @Suppress("UNCHECKED_CAST")
    return data as T
}