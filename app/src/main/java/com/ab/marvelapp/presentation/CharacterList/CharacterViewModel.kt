package com.ab.marvelapp.presentation.CharacterList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ab.marvelapp.commons.Response
import com.ab.marvelapp.domain.use_cases.CharacterUseCase
import com.ab.marvelapp.domain.use_cases.SearchCharacterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterViewModel @Inject constructor(
    private val characterUseCase: CharacterUseCase,
    private val searchCharacterUseCase: SearchCharacterUseCase
) : ViewModel() {

    private val marvelValue = MutableStateFlow(MarvelListState())
    var _marvelValue: StateFlow<MarvelListState> = marvelValue
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default

    fun getAllCharactersData(offset: Int) = viewModelScope.launch(dispatcher) {
        characterUseCase.invoke(offset = offset).collect {
            when (it) {
                is Response.Success -> {
                    marvelValue.value = MarvelListState(character = it.data ?: emptyList())
                }
                is Response.Error -> {
                    marvelValue.value = MarvelListState(isLoading = true)
                }
                is Response.Loading -> {
                    marvelValue.value =
                        MarvelListState(error = it.message ?: "An Unexpected error occurred")
                }
            }
        }
    }

    fun getSearchedCharacters(search:String) = viewModelScope.launch(dispatcher) {
        searchCharacterUseCase.invoke(search=search).collect {
            when (it) {
                is Response.Success -> {
                    marvelValue.value = MarvelListState(character = it.data ?: emptyList())
                }
                is Response.Error -> {
                    marvelValue.value = MarvelListState(isLoading = true)
                }
                is Response.Loading -> {
                    marvelValue.value =
                        MarvelListState(error = it.message ?: "An Unexpected error occurred")
                }
            }
        }
    }
}