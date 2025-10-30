package com.example.pruebatecnicaraven.ui.articles

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.pruebatecnicaraven.R
import com.example.pruebatecnicaraven.databinding.FragmentArticlesBinding
import com.example.pruebatecnicaraven.ui.articles.adapter.ArticlesAdapter
import com.example.pruebatecnicaraven.ui.articles.adapter.SwipeToDeleteCallback
import com.example.pruebatecnicaraven.util.SortType
import com.example.pruebatecnicaraven.util.gone
import com.example.pruebatecnicaraven.util.showToast
import com.example.pruebatecnicaraven.util.visible
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ArticlesFragment : Fragment() {

    private var _binding: FragmentArticlesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ArticlesViewModel by viewModels()
    private lateinit var articlesAdapter: ArticlesAdapter

    private var connectionSnackbar: Snackbar? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArticlesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMenu()
        setupRecyclerView()
        setupSwipeToDelete()
        setupSwipeRefresh()
        observeState()
    }

    private fun setupMenu() {
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu)

                val searchItem = menu.findItem(R.id.action_search)
                val searchView = searchItem.actionView as SearchView

                searchView.queryHint = "Buscar artículo..."
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        viewModel.searchArticles(newText ?: "")
                        return true
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.sort_recent -> {
                        if (!menuItem.isChecked) {
                            menuItem.isChecked = true
                            viewModel.sortArticles(SortType.RECENT)
                            showToast("Ordenado: Más recientes")
                        }
                        true
                    }
                    R.id.sort_oldest -> {
                        if (!menuItem.isChecked) {
                            menuItem.isChecked = true
                            viewModel.sortArticles(SortType.OLDEST)
                            showToast("Ordenado: Más antiguos")
                        }
                        true
                    }
                    R.id.sort_author -> {
                        if (!menuItem.isChecked) {
                            menuItem.isChecked = true
                            viewModel.sortArticles(SortType.AUTHOR)
                            showToast("Ordenado: Por autor")
                        }
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }


    private fun setupRecyclerView() {
        articlesAdapter = ArticlesAdapter(
            onItemClick = { article ->
                article.getDisplayUrl()?.let { url ->
                    val action = ArticlesFragmentDirections
                        .actionArticlesFragmentToArticleDetailFragment(
                            articleUrl = url,
                            articleTitle = article.getDisplayTitle()
                        )
                    findNavController().navigate(action)
                } ?: run {
                    showToast("Este artículo no tiene URL disponible")
                }
            }
        )
        binding.recyclerViewArticles.adapter = articlesAdapter
    }

    private fun setupSwipeToDelete() {
        val swipeCallback = SwipeToDeleteCallback(
            context = requireContext(),
            onSwiped = { position ->
                val article = articlesAdapter.currentList[position]
                viewModel.deleteArticle(article.objectID)
                showToast("Artículo eliminado")
            }
        )
        val itemTouchHelper = ItemTouchHelper(swipeCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewArticles)
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshArticles()
        }

        binding.swipeRefreshLayout.setColorSchemeResources(
            R.color.orange_primary,
            R.color.orange_primary_dark
        )
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    updateUI(state)
                }
            }
        }


    }

    private fun updateUI(state: ArticlesState) {

        if (state.isLoading && state.articles.isEmpty()) {
            binding.loadingContainer.visible()
            binding.recyclerViewArticles.gone()
            binding.emptyStateContainer.gone()
        } else {
            binding.loadingContainer.gone()
        }

        binding.swipeRefreshLayout.isRefreshing = state.isRefreshing

        if (state.articles.isNotEmpty()) {
            binding.recyclerViewArticles.visible()
            binding.emptyStateContainer.gone()
            articlesAdapter.submitList(state.articles)
        } else if (!state.isLoading && state.error == null) {
            binding.recyclerViewArticles.gone()
            binding.emptyStateContainer.visible()
        }


        state.error?.let { error ->
            if (state.articles.isEmpty()) {
                binding.recyclerViewArticles.gone()
                binding.emptyStateContainer.visible()
                binding.textViewError.text = error
            } else {
                showToast(error)
                viewModel.clearError()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        connectionSnackbar?.dismiss()
        _binding = null
    }
}
