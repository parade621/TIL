package com.example.app.nav_and_sharedviewmodel.ui
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.app.R
import com.example.app.databinding.FragmentCoffeBinding
import com.example.app.nav_and_sharedviewmodel.ui.dialog.ConfirmDialog
import com.example.app.nav_and_sharedviewmodel.viewmodel.MyViewModel

class CoffeFragment :Fragment(){

    private var _binding : FragmentCoffeBinding? = null
    private val binding get() = _binding!!
    private val myViewModel : MyViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCoffeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply{
            viewModel = myViewModel
            coffeFragment = this@CoffeFragment
            lifecycleOwner = viewLifecycleOwner
        }
    }

    fun showConfirmDialog(beverage: String){
        ConfirmDialog().show(childFragmentManager, "")
        myViewModel.setBeverage(beverage)
    }

    fun moveToDessert(){
        findNavController().navigate(R.id.action_coffeFragment_to_dessertFragment)
    }

    fun makeToastMessage(){
        Toast.makeText(binding.root.context, "현재 화면입니다.", Toast.LENGTH_SHORT).show()
    }

    fun moveToSummary(){
        findNavController().navigate(R.id.action_coffeFragment_to_summaryFragment)
    }

    fun cancleOrder(){
        myViewModel.resetValue()
    }

    override fun onDestroyView() {
        _binding=null
        super.onDestroyView()
    }

}