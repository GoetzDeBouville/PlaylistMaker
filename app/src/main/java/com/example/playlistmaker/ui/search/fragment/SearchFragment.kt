package com.example.playlistmaker.ui.search.fragment

import android.content.Context.INPUT_METHOD_SERVICE
import android.content.IntentFilter
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.domain.search.models.SearchState
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.ui.search.adapters.TrackAdapter
import com.example.playlistmaker.ui.search.viewmodel.SearchViewModel
import com.example.playlistmaker.utils.NetworkStatusReciever
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModel()

    private val historyTracklist = ArrayList<Track>()
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var trackHistoryAdapter: TrackAdapter

    private val networkStatusReciever = NetworkStatusReciever()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapters()
        setupRecyclerViews()
        viewModel.showHistory()
        setupSearchEditText()
        setupBtnClearSearchClickListener()
        binding.btnRefresh.setOnClickListener {
            val searchRequest = binding.inputEditText.text.toString()
            if (searchRequest.isNotBlank()) {
                viewModel.searchRequest(searchRequest)
            }
        }

        if (historyTracklist.isNotEmpty()) {
            binding.searchHistory.visibility = View.VISIBLE
        }
        setupBtnClearHistoryClickListener()
        viewModel.state.observe(viewLifecycleOwner) {
            renderState(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        ContextCompat.registerReceiver(
            requireContext(),
            networkStatusReciever,
            IntentFilter(NetworkStatusReciever.ACTION),
            ContextCompat.RECEIVER_EXPORTED
        )
    }

    override fun onPause() {
        super.onPause()
        requireActivity().unregisterReceiver(networkStatusReciever)
    }

    private fun initAdapters() {
        trackAdapter = TrackAdapter {
            if (viewModel.clickDebounce()) {
                viewModel.saveTrack(it)
                val gson = Gson()
                val trackJson = gson.toJson(it)
                val bundle = Bundle().apply {
                    putString(TRACK_KEY, trackJson)
                }
                findNavController().navigate(R.id.action_global_to_playerFragment, bundle)
            }
        }
        trackHistoryAdapter = TrackAdapter {
            if (viewModel.clickDebounce()) {
                viewModel.saveTrack(it)
                viewModel.showHistory()
                val gson = Gson()
                val trackJson = gson.toJson(it)
                val bundle = Bundle().apply {
                    putString(TRACK_KEY, trackJson)
                }
                findNavController().navigate(R.id.action_global_to_playerFragment, bundle)
            }
        }
    }

    private fun updateUI(
        placeholderErrorVisible: Int,
        progressBarVisible: Int,
        tracklistRecyclerVisible: Int
    ) {
        binding.searchHistory.visibility = View.GONE
        binding.llPlaceholderError.visibility = placeholderErrorVisible
        binding.progressBar.visibility = progressBarVisible
        binding.rvTracklist.visibility = tracklistRecyclerVisible
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) View.GONE
        else View.VISIBLE
    }

    private fun renderState(state: SearchState) {
        when (state) {
            is SearchState.SearchHistory -> showHistory(state.trackList)
            is SearchState.Content -> showFoundTracks(state.trackList)
            is SearchState.Loading -> showLoading()
            is SearchState.Empty -> showEmpty(getString(R.string.nothing_found))
            is SearchState.ConnectionError -> showError(getString(R.string.check_connection))
        }
    }

    private fun search() {
        val searchText = binding.inputEditText.text.toString()
        if (searchText.isNotBlank()) viewModel.searchDebounce(binding.inputEditText.text.toString())
        else viewModel.showHistory()
    }

    private fun setupBtnClearHistoryClickListener() {
        binding.btnClearHistory.setOnClickListener {
            viewModel.clearHistory()
            binding.searchHistory.visibility = View.GONE
            trackHistoryAdapter.notifyDataSetChanged()
        }
    }

    private fun setupBtnClearSearchClickListener() {
        binding.ivClear.setOnClickListener {
            viewModel.showHistory()
            binding.inputEditText.setText("")
            viewModel.savedSearchRequest = ""
            val keyboard =
                requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.hideSoftInputFromWindow(
                binding.inputEditText.windowToken, 0
            )
            binding.inputEditText.clearFocus()
            trackAdapter.notifyDataSetChanged()
            viewModel.showHistory()
        }
    }

    private fun setSearchEditorActionListener() {
        binding.inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val searchRequest = binding.inputEditText.text.toString()
                if (searchRequest.isNotBlank()) viewModel.searchRequest(searchRequest)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun setupHistoryRecycler() {
        binding.rvHistory.layoutManager = LinearLayoutManager(requireContext())
        trackHistoryAdapter.trackList = historyTracklist
        binding.rvHistory.adapter = trackHistoryAdapter
    }

    private fun setupFocusChangeListener() {
        binding.inputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.inputEditText.text.isEmpty() && historyTracklist.isNotEmpty()) {
                binding.searchHistory.visibility = View.VISIBLE
            } else {
                binding.searchHistory.visibility = View.GONE
            }
        }
    }

    private fun setupRecyclerViews() {
        setupTracklistRecycler()
        setupHistoryRecycler()
    }

    private fun setupSearchEditText() {
        binding.inputEditText.setText(viewModel.savedSearchRequest)
        setupFocusChangeListener()
        setupTextChangedListener()
        setSearchEditorActionListener()
    }

    private fun setupTextChangedListener() {
        binding.inputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.ivClear.visibility = clearButtonVisibility(s)
                if (s != null) search()
            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.savedSearchRequest = s.toString()
            }
        })
    }

    private fun setupTracklistRecycler() {
        binding.rvTracklist.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTracklist.adapter = trackAdapter
    }

    private fun showEmpty(message: String) {
        updateUI(View.VISIBLE, View.GONE, View.GONE)
        binding.tvPlaceholderMessage.text = message
        binding.btnRefresh.visibility = View.GONE
        binding.imgPlaceholder.setImageResource(R.drawable.empty_list_img)
    }

    private fun showError(errorMessage: String) {
        updateUI(View.VISIBLE, View.GONE, View.GONE)
        binding.tvPlaceholderMessage.text = errorMessage
        binding.btnRefresh.visibility = View.VISIBLE
        binding.imgPlaceholder.setImageResource(R.drawable.no_internet_img)
    }

    private fun showFoundTracks(foundTracks: List<Track>) {
        updateUI(View.GONE, View.GONE, View.VISIBLE)
        trackAdapter.trackList = foundTracks as ArrayList<Track>
        trackAdapter.notifyDataSetChanged()
    }

    private fun showHistory(tracks: List<Track>) {
        historyTracklist.clear()
        historyTracklist.addAll(tracks)
        binding.llPlaceholderError.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.rvTracklist.visibility = View.GONE
        if (historyTracklist.isNotEmpty()) {
            binding.searchHistory.visibility = View.VISIBLE
            binding.historyLayout.visibility = View.VISIBLE
            trackHistoryAdapter.notifyDataSetChanged()
        }
    }

    private fun showLoading() {
        updateUI(View.GONE, View.VISIBLE, View.GONE)
    }
    companion object {
        const val TRACK_KEY = "track"
    }
}
