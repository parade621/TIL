package com.example.android.unscramble.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.unscramble.ui.game.MAX_NO_OF_WORDS
import com.example.android.unscramble.ui.game.SCORE_INCREASE
import com.example.android.unscramble.ui.game.allWordsList

class GameViewModel: ViewModel() {

    private val _score = MutableLiveData(0)
    private val _currentWordCount = MutableLiveData(0)
    private val _currentScrambledWord = MutableLiveData<String>()
    private var wordList: MutableList<String> = mutableListOf()
    private lateinit var currentWord: String
    val currentScrambledWord: LiveData<String> get() = _currentScrambledWord
    val score:LiveData<Int> get()= _score
    val currentWordCount: LiveData<Int> get() = _currentWordCount

    init{
        getNextWord()
    }

    private fun getNextWord(){
        currentWord = allWordsList.random()
        val tempWord = currentWord.toCharArray()
        tempWord.shuffle()
        while (String(tempWord).equals(currentWord, false)){
            tempWord.shuffle()
        }
        if(wordList.contains(currentWord)){
            getNextWord()
        }else{
            _currentScrambledWord.value = String(tempWord)
            _currentWordCount.value = (_currentWordCount.value)?.inc()
            wordList.add(currentWord)
        }
    }
    private fun increaseScore(){
        _score.value = (_score.value)?.plus(SCORE_INCREASE)
    }
    fun isUserWordCorrect(playerWord : String): Boolean{
        if (playerWord.equals(currentWord,true)){
            increaseScore()
            return true
        }else{
            return false
        }
    }

    //  ViewModel 내의 데이터를 처리하고 수정하는 도우미미 메서
    fun nextWord(): Boolean{
        return if(_currentWordCount.value!! < MAX_NO_OF_WORDS){
            getNextWord()
            true
        }else false
    }

    fun reinitializeData(){
        _score.value = 0
        _currentWordCount.value=0
        wordList.clear()
        getNextWord()
    }

}