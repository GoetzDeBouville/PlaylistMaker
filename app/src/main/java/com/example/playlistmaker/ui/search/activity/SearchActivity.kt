package com.example.playlistmaker.ui.search.activity

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.search.models.SearchActivityState
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.ui.player.activity.PlayerActivity
import com.example.playlistmaker.ui.search.adapters.TrackAdapter
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.example.playlistmaker.ui.search.view_model.SearchViewModelFactory

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var viewModel: SearchViewModel

    private val historyTracklist = ArrayList<Track>()
    private val tracksList = ArrayList<Track>()
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var trackHistoryAdapter: TrackAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel =
            ViewModelProvider(this, SearchViewModelFactory(this)).get(SearchViewModel::class.java)

        initAdapters()
        setupRecyclerViews()
        viewModel.showHistory()
        setupSearchEditText()
        setupBtnClearSearchClickListener()
        binding.refreshButton.setOnClickListener {
            search()
        }
        binding.llArrowBack.setOnClickListener {
            finish()
        }
        if (historyTracklist.isNotEmpty()) {
            binding.searchHistory.visibility = View.VISIBLE
        }
        setupBtnClearHistoryClickListener()
        viewModel.state.observe(this) {
            renderState(it)
        }
    }

    private fun isNightMode(): Boolean {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES
    }

    private fun initAdapters() {
        trackAdapter = TrackAdapter {
            if (viewModel.clickDebounce()) {
                viewModel.saveTrack(it)
                PlayerActivity.newIntent(this, it).apply { startActivity(this) }
            }
        }
        trackHistoryAdapter = TrackAdapter {
            if (viewModel.clickDebounce()) {
                PlayerActivity.newIntent(this, it).apply {
                    startActivity(this)
                }
            }
        }
    }

    private fun updateUI(
        method: String,
        searchHistoryVisible: Int,
        placeholderErrorVisible: Int,
        progressBarVisible: Int,
        tracklistRecyclerVisible: Int
    ) {
        Log.e(
            "SearchActivity",
            "updateUI $method, $searchHistoryVisible, $placeholderErrorVisible, $progressBarVisible, $tracklistRecyclerVisible"
        )
        binding.searchHistory.visibility = searchHistoryVisible
        binding.placeholderError.visibility = placeholderErrorVisible
        binding.progressBar.visibility = progressBarVisible
        binding.tracklistRecycler.visibility = tracklistRecyclerVisible
    }

    private fun showEmpty(message: String) {
        updateUI("showEmpty", View.GONE, View.VISIBLE, View.GONE, View.GONE)
        binding.placeholderMessage.text = message
        binding.placeholderImage.setImageResource(
            if (isNightMode()) R.drawable.emtpty_dark
            else R.drawable.empty_light
        )
    }

    private fun showFoundTracks(foundTracks: List<Track>) {
        Log.e("SearchActivity", "foundTracks $foundTracks")
        updateUI("showFoundTracks", View.GONE, View.GONE, View.GONE, View.VISIBLE)
        tracksList.clear()
        tracksList.addAll(foundTracks)
        trackAdapter.notifyDataSetChanged()
    }

    private fun showLoading() {
        updateUI("showLoading", View.GONE, View.GONE, View.VISIBLE, View.GONE)
    }

    private fun showError(errorMessage: String) {
        updateUI("showError", View.GONE, View.VISIBLE, View.GONE, View.GONE)
        binding.placeholderMessage.text = errorMessage
        binding.placeholderImage.setImageResource(
            if (isNightMode()) R.drawable.no_internet_dark
            else R.drawable.no_internet_light
        )
    }

    private fun showHistory(tracks: List<Track>) {
        Log.e("SearchActivity", "History tracks: $tracks")
        historyTracklist.clear()
        historyTracklist.addAll(tracks)
        binding.placeholderError.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.tracklistRecycler.visibility = View.GONE
        if (historyTracklist.isNotEmpty()) {
            binding.searchHistory.visibility = View.VISIBLE
            binding.historyLayout.visibility = View.VISIBLE
            trackHistoryAdapter.notifyDataSetChanged()
        }
    }

    private fun renderState(state: SearchActivityState) {
        when (state) {
            is SearchActivityState.SearchHistory -> showHistory(state.trackList)
            is SearchActivityState.Content -> showFoundTracks(state.trackList)
            is SearchActivityState.Loading -> showLoading()
            is SearchActivityState.Empty -> showEmpty(state.emptyMessage)
            is SearchActivityState.Error -> showError(state.errorMessage)
        }
    }

    private fun setupBtnClearHistoryClickListener() {
        binding.buttonClearHistory.setOnClickListener {
            viewModel.clearHistory()
            binding.searchHistory.visibility = View.GONE
            trackHistoryAdapter.notifyDataSetChanged()
        }
    }

    private fun setupBtnClearSearchClickListener() {
        binding.clearIcon.setOnClickListener {
            viewModel.stopSearch()
            binding.inputEditText.setText("")
            viewModel.savedSearchRequest = ""
            val keyboard = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.hideSoftInputFromWindow(
                binding.inputEditText.windowToken, 0
            )
            binding.inputEditText.clearFocus()
            tracksList.clear()
            trackAdapter.notifyDataSetChanged()
            viewModel.showHistory()
        }
    }

    private fun setupSearchEditText() {
        binding.inputEditText.setText(viewModel.savedSearchRequest)
        setupFocusChangeListener()
        setupTextChangedListener()
        setupEditorActionListener()
    }

    private fun setupEditorActionListener() {
        binding.inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
            }
            false
        }
    }

    private fun setupTextChangedListener() {
        binding.inputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearIcon.visibility = clearButtonVisibility(s)
                if (s != null) {
                    search()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.savedSearchRequest = s.toString()
            }
        })
    }

    private fun setupFocusChangeListener() {
        binding.inputEditText.setOnFocusChangeListener { _, hasFocus ->
            binding.searchHistory.visibility =
                if (hasFocus && binding.inputEditText.text.isEmpty() && historyTracklist.isNotEmpty()) View.VISIBLE
                else View.GONE
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

    private fun search() {
        val searchText = binding.inputEditText.text.toString()
        Log.d("SearchActivity", "Searching for: $searchText")
        if (searchText.isNotBlank()) {
            viewModel.searchDebounce(binding.inputEditText.text.toString())
        } else {
            viewModel.stopSearch()
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }
}
