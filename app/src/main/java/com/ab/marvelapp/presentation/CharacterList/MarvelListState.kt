package com.ab.marvelapp.presentation.CharacterList

import com.ab.marvelapp.domain.model.Character

data class MarvelListState(
    val isLoading: Boolean = false,
    val character: List<Character> = emptyList(),
    val error: String = ""
)
