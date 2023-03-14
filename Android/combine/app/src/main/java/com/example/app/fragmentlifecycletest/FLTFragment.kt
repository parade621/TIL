package com.example.app.fragmentlifecycletest

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.app.R

class FLTFragment : Fragment() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.e("Frament TAG ", "onAttach(), FirstFragment")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("Frament TAG ", "onCreate(), FirstFragment")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e("Frament TAG ", "onCreateView(), FirstFragment")

        val view = inflater.inflate(R.layout.fragment_first, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("Frament TAG ", "onViewCreate(), FirstFragment")
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Log.e("Frament TAG ", "onViewStateRestored(), FirstFragment")
    }

    override fun onStart() {
        super.onStart()
        Log.e("Frament TAG ", "onStart(), FirstFragment")
    }

    override fun onResume() {
        super.onResume()
        Log.e("Frament TAG ", "onResume(), FirstFragment")
    }

    override fun onPause() {
        super.onPause()
        Log.e("Frament TAG ", "onPause(), FirstFragment")
    }

    override fun onStop() {
        super.onStop()
        Log.e("Frament TAG ", "onStop(), FirstFragment")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.e("Frament TAG ", "onDestroyView(), FirstFragment")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("Frament TAG ", "onDestroy(), FirstFragment")
    }

    override fun onDetach() {
        super.onDetach()
        Log.e("Frament TAG ", "onDetach(), FirstFragment")
    }
}