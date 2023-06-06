package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.itunesApi.ItunesResponce
import com.example.playlistmaker.itunesApi.ItunesService
import com.example.playlistmaker.track.Track
import com.example.playlistmaker.track.TrackAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val SEARCH_KEY = "search_key"
const val SHARED_PREFERERNCES = "playlist_maker_preferences"

@Suppress("UNUSED_EXPRESSION")
class SearchActivity : AppCompatActivity() {
    private val itunesService = ItunesService.itunesService
    private val tracksList = ArrayList<Track>()
    private val trackAdapter = TrackAdapter()
    private val trackHistoryAdapter = TrackAdapter()
    private val historyTracklist = ArrayList<Track>()
    private var savedSearchRequest = ""

    private lateinit var binding: ActivitySearchBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var observer: SharedPreferences.OnSharedPreferenceChangeListener
    private lateinit var historyTrackList: SearchHistory

    enum class LoadingStatus {
        FAILED_SEARCH,
        SUCCESS,
        NO_INTERNET
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences(SHARED_PREFERERNCES, MODE_PRIVATE)
        historyTrackList = SearchHistory(sharedPreferences)
        historyTracklist.addAll(historyTrackList.historyTrackList)

        buildRecyclerView()
        buildHIstoryRecyclerView()

        binding.inputEditText.setText(savedSearchRequest)

        binding.inputEditText.setOnFocusChangeListener { view, hasFocus ->
            binding.historyLayout.visibility =
                if (hasFocus && binding.inputEditText.text.isEmpty() && historyTracklist.isNotEmpty())
                    View.VISIBLE
                else
                    View.GONE
        }

        binding.inputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.buttonClearHistory.visibility = clearButtonVisibility(s)
                if (s != null) {
                    binding.historyLayout.visibility =
                        if (binding.inputEditText.hasFocus() && s.isEmpty() && historyTracklist.isNotEmpty())
                            View.VISIBLE
                        else
                            View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {
                savedSearchRequest = s.toString()
            }
        })

        binding.inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
                true
            }
            false
        }

        binding.clearIcon.setOnClickListener {
            binding.inputEditText.setText("")
            savedSearchRequest = ""

            val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.hideSoftInputFromWindow(
                binding.inputEditText.windowToken,
                0
            )
            binding.inputEditText.clearFocus()

            tracksList.clear()
            trackAdapter.notifyDataSetChanged()
            binding.placeholderError.visibility = View.GONE
        }

        binding.refreshButton.setOnClickListener {
            search()
        }

        binding.arrowBack.setOnClickListener {
            finish()
        }

        if (historyTracklist.isNotEmpty()) {
            binding.historyLayout.visibility = View.VISIBLE
        }

        observer =
            SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
                if (key == HISTORY_TRACKLIST) {
                    val history = sharedPreferences?.getString(HISTORY_TRACKLIST, null)
                    if (history != null) {
                        historyTracklist.clear()
                        historyTracklist.addAll(historyTrackList.fromJsonToTracklist(history))
                        trackHistoryAdapter.notifyDataSetChanged()
                    }
                }
            }



        sharedPreferences.registerOnSharedPreferenceChangeListener(observer)

        binding.buttonClearHistory.setOnClickListener {
            historyTrackList.clearHistory()
            historyTracklist.clear()
            binding.historyLayout.visibility = View.GONE
            trackHistoryAdapter.notifyDataSetChanged()
        }
    }

    private fun buildRecyclerView() {
        binding.tracklistRecycler.layoutManager = LinearLayoutManager(this)
        trackAdapter.trackList = tracksList
        trackAdapter.onClickedTrack = { track: Track -> historyTrackList.addTrack(track) }
        binding.tracklistRecycler.adapter = trackAdapter
    }

    private fun buildHIstoryRecyclerView() {
        binding.historyRecyclerView.layoutManager = LinearLayoutManager(this)
        trackHistoryAdapter.trackList = historyTracklist
        binding.historyRecyclerView.adapter = trackHistoryAdapter
    }

    private fun search() {
        itunesService.search(binding.inputEditText.text.toString())
            .enqueue(object : Callback<ItunesResponce> {
                override fun onResponse(
                    call: Call<ItunesResponce>,
                    response: Response<ItunesResponce>
                ) {
                    if (response.code() == 200) {
                        tracksList.clear()
                        if (response.body()?.tracks?.isNotEmpty() == true) {
                            tracksList.addAll(response.body()?.tracks!!)
                            showResult(LoadingStatus.SUCCESS)
                        }
                        if (tracksList.isEmpty()) {
                            showResult(LoadingStatus.FAILED_SEARCH)
                        }
                    } else {
                        showResult(LoadingStatus.NO_INTERNET)
                    }
                }

                override fun onFailure(call: Call<ItunesResponce>, t: Throwable) {
                    showResult(LoadingStatus.NO_INTERNET)
                }
            })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showResult(state: LoadingStatus) {
        if (state == LoadingStatus.SUCCESS) {
            trackAdapter.notifyDataSetChanged()
            binding.placeholderError.visibility = View.GONE
        } else {
            binding.placeholderError.visibility = View.VISIBLE
            if (state == LoadingStatus.FAILED_SEARCH) {
                binding.refreshButton.visibility = View.INVISIBLE
                binding.placeholderMessage.text = getString(R.string.nothing_found)
            } else if (state == LoadingStatus.NO_INTERNET) {
                binding.placeholderMessage.text = getString(R.string.check_connection)
            }
            tracksList.clear()
            trackAdapter.notifyDataSetChanged()
            setPlaceHolderImage(state)
        }
    }

    private fun isNightMode(): Boolean {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES
    }

    private fun setPlaceHolderImage(state: LoadingStatus) {
        if (state == LoadingStatus.FAILED_SEARCH) {
            binding.placeholderImage.setImageResource(
                if (isNightMode()) R.drawable.emtpty_dark
                else R.drawable.empty_light
            )
        } else if (state == LoadingStatus.NO_INTERNET) {
            binding.placeholderImage.setImageResource(
                if (isNightMode()) {
                    R.drawable.no_internet_dark
                } else {
                    R.drawable.no_internet_light
                }
            )
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedSearchRequest = savedInstanceState.getString(SEARCH_KEY, "")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_KEY, savedSearchRequest)
    }
}