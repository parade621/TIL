package com.example.app.retrofit_ex.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.activityViewModels
import com.example.app.databinding.FragmentLottoBinding
import com.example.app.retrofit_ex.viewmodel.LottoViewModel

class LottoFragment : Fragment() {
    private var _binding: FragmentLottoBinding? = null
    private val binding get() = _binding!!
    private val myViewModel: LottoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLottoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            viewModel = myViewModel
            lottoFragment = this@LottoFragment
            lifecycleOwner = viewLifecycleOwner
        }

        // 소프트 키보드에서 확인 버튼을 클릭하면, 동작을 수행하고 키보드를 숨깁니다.
        binding.EtInputDrwNo.setOnEditorActionListener({ textView, action, keyEvent ->
            var handled = false
            if (action == EditorInfo.IME_ACTION_DONE) {
                requestGETtoAPI()
                softKeyboardHide()
                handled = true
            }
            handled
        })
    }

    fun requestGETtoAPI() {
        val txt: String = binding.EtInputDrwNo.text.toString()
        if (!myViewModel.getDrwno(txt)) {
            Log.d("LottoFragment Log", "false right?")
            Toast.makeText(
                binding.root.context,
                "유효한 숫자를 입력해주세요( 1 ~${myViewModel.latest_drwno} )",
                Toast.LENGTH_SHORT
            ).show()
            binding.EtInputDrwNo.setText("")
        } else {
            Log.d("LottoFragment Log", "true right?")
        }
    }

    // 소프트 키보드 숨김을 위한 함수
    private fun softKeyboardHide() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.EtInputDrwNo.windowToken, 0)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}