package com.ab.marvelapp

import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ab.marvelapp.databinding.ActivityMainBinding
import com.ab.marvelapp.domain.model.Character
import com.ab.marvelapp.presentation.CharacterList.CharacterListAdapter
import com.ab.marvelapp.presentation.CharacterList.CharacterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var searchTerm:String
    var flag = 3
    var paginatedValue = 0
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CharacterListAdapter
    private lateinit var layoutManager: GridLayoutManager
    private val tempList = arrayListOf<Character>()

    private val viewModel: CharacterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        StrictMode.setVmPolicy(
            VmPolicy.Builder(StrictMode.getVmPolicy())
                .detectLeakedClosableObjects()
                .build()
        )
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        recyclerView = binding.charactersRecyclerView
        layoutManager = GridLayoutManager(this, 2)
        recyclerViewCharacters()

        binding.btSort.setOnClickListener {
            tempList.sortWith { o1,o2->
                o1.name.compareTo(o2.name)
            }
            adapter.setData(tempList)
        }
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (layoutManager.findLastVisibleItemPosition() == layoutManager.itemCount - 1) {
                    paginatedValue += 20
                    viewModel.getAllCharactersData(paginatedValue)
                    callApi()
                }
            }
        })
    }

    private fun callApi() {
        CoroutineScope(Main).launch {
            repeat(flag) {
                viewModel._marvelValue.collect {
                    when {
                        it.isLoading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        it.error.isNotBlank() -> {
                            binding.progressBar.visibility = View.GONE
                            flag = 0
                        }
                        it.character.isNotEmpty() -> {
                            binding.progressBar.visibility = View.GONE
                            flag = 0
                            tempList.addAll(it.character)
                            adapter.setData(it.character as ArrayList<Character> /* = java.util.ArrayList<com.ab.marvelapp.domain.model.Character> */)
                        }
                    }
                    delay(1000)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val search = menu?.findItem(R.id.menuSearch)
        val searchView = search?.actionView as SearchView
        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(this)
        return true
    }

    private fun recyclerViewCharacters() {
        adapter = CharacterListAdapter(this, ArrayList())
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(query!=null){
            searchTerm = query
        }
        if (searchTerm.isNotEmpty()){
            search()
        }
        return true
    }

    private fun search() {
        viewModel.getSearchedCharacters(searchTerm)
        CoroutineScope(Main).launch {
            viewModel._marvelValue.collect{
                when{
                    it.isLoading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    it.error.isNotBlank() -> {
                        binding.progressBar.visibility = View.GONE
                    }
                    it.character.isNotEmpty() -> {
                        binding.progressBar.visibility = View.GONE
                        adapter.setData(it.character as ArrayList<Character> /* = java.util.ArrayList<com.ab.marvelapp.domain.model.Character> */)
                    }
                }
            }
        }
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if(newText!=null){
            searchTerm = newText
        }
        if (searchTerm.isNotEmpty()){
            search()
        }
        return true
    }

    override fun onStart() {
        super.onStart()
        viewModel.getAllCharactersData(paginatedValue)
        callApi()
    }
}