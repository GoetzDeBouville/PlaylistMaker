package com.example.playlistmaker

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.itunesApi.ItunesResponce
import com.example.playlistmaker.itunesApi.ItunesService
import com.example.playlistmaker.track.Track
import com.example.playlistmaker.track.TrackAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences(SHARED_PREFERERNCES, Context.MODE_PRIVATE)
        historyTrackList = SearchHistory(sharedPreferences)

        setupHistoryTrackList()
        setupRecyclerViews()
        setSearchInputText()
        setupBackButton()
        setTextChangedListener()
        setupRefreshButton()
        setupTrackClickListener()
        setupSearchListeners()
        sharedPrefsChangeListener()
        setupHistoryClickListener()
        clearHistoryClickListener()
        updateTrackListFromSharedPreferences()
        setClearIconButton()
    }

    private fun updateTrackListFromSharedPreferences() {
        historyTracklist.clear()
        historyTracklist.addAll(historyTrackList.savedInHistoryTracks.take(10))
    }


    private fun setupHistoryTrackList() {
        historyTracklist.addAll(historyTrackList.savedInHistoryTracks)
        if (historyTracklist.isNotEmpty()) {
            binding.historyLayout.visibility = View.VISIBLE
        }
    }

    private fun setupRecyclerViews() {
        setupTracklistRecycler()
        setupHistoryRecycler()
    }

    private fun setupTracklistRecycler() {
        binding.tracklistRecycler.layoutManager = LinearLayoutManager(this)
        trackAdapter.trackList = tracksList
        binding.tracklistRecycler.adapter = trackAdapter
    }

    private fun setupHistoryRecycler() {
        binding.historyRecyclerView.layoutManager = LinearLayoutManager(this)
        trackHistoryAdapter.trackList = historyTracklist
        binding.historyRecyclerView.adapter = trackHistoryAdapter
    }

    private fun setSearchInputText() {
        binding.inputEditText.setText(savedSearchRequest)
    }

    private fun setupBackButton() {
        binding.arrowBack.setOnClickListener {
            finish()
        }
    }

    private fun setupHistoryClickListener() {
        trackHistoryAdapter.onClickedTrack = { track: Track ->
            historyTrackList.addTrack(track)
            startActivity(PlayerActivity.newIntent(this, track))
        }
    }

    private fun setTextChangedListener() {
        binding.inputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearIcon.visibility = clearButtonVisibility(s)
                if (s != null) {
                    binding.searchHistory.visibility =
                        if (binding.inputEditText.hasFocus() && s.isEmpty() && historyTracklist.isNotEmpty()) View.VISIBLE
                        else View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {
                savedSearchRequest = s.toString()
            }
        })
    }

    private fun setupRefreshButton() {
        binding.refreshButton.setOnClickListener {
            search()
        }
    }

    private fun setClearIconButton() {
        binding.clearIcon.setOnClickListener {
            clearUserInput()
//            if (historyTracklist.isNotEmpty()) {
//                binding.historyLayout.visibility = View.VISIBLE
//            }
        }
    }


    private fun setupTrackClickListener() {
        trackAdapter.onClickedTrack = { track: Track ->
            historyTrackList.addTrack(track)
            startActivity(PlayerActivity.newIntent(this, track))
        }
    }

    private fun setupSearchListeners() {
        setSearchFocusChangeListener()
        setSearchTextChangedListener()
        setSearchEditorActionListener()
    }

    private fun setSearchFocusChangeListener() {
        binding.inputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.inputEditText.text.isEmpty() && historyTracklist.isNotEmpty()) {
                binding.historyLayout.visibility = View.VISIBLE
            } else {
                binding.historyLayout.visibility = View.GONE
            }
        }
    }

    private fun setSearchTextChangedListener() {
        binding.inputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                handleTextChanged(s)
            }

            override fun afterTextChanged(s: Editable?) {
                savedSearchRequest = s.toString()
            }
        })
    }

    private fun sharedPrefsChangeListener() {
        observer = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
            if (key == HISTORY_LIST_KEY) {
                val history = sharedPreferences?.getString(HISTORY_LIST_KEY, null)
                if (history != null) {
                    historyTracklist.clear()
                    historyTracklist.addAll(historyTrackList.fromJsonToTracklist(history))
                    trackHistoryAdapter.notifyDataSetChanged()
                }
            }
        }
        sharedPreferences.registerOnSharedPreferenceChangeListener(observer)
    }

    private fun handleTextChanged(s: CharSequence?) {
        binding.clearIcon.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
        binding.buttonClearHistory.visibility = clearButtonVisibility(s)
        if (s != null) {
            binding.historyLayout.visibility =
                if (binding.inputEditText.hasFocus() && s.isEmpty() && historyTracklist.isNotEmpty())
                    View.VISIBLE
                else
                    View.GONE
        }
    }

    private fun setSearchEditorActionListener() {
        binding.inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun clearUserInput() {
        binding.inputEditText.setText("")
        savedSearchRequest = ""
        val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        keyboard.hideSoftInputFromWindow(
            binding.inputEditText.windowToken,
            0
        )
        binding.inputEditText.clearFocus()
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun showPlaceholderError(show: Boolean) {
        binding.placeholderError.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun showSearchHistory() {
        if (historyTracklist.isNotEmpty()) {
            binding.searchHistory.visibility = View.VISIBLE
        }
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

    private fun isNightMode(): Boolean {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES
    }

    private fun clearHistoryClickListener() {
        binding.buttonClearHistory.setOnClickListener {
            historyTrackList.clearHistory()
            historyTracklist.clear()
            binding.searchHistory.visibility = View.GONE
            trackHistoryAdapter.notifyDataSetChanged()
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

    companion object {
        const val SEARCH_KEY = "search_key"
        const val SHARED_PREFERERNCES = "playlist_maker_preferences"
        const val HISTORY_LIST_KEY = "history_list_key"
    }
}