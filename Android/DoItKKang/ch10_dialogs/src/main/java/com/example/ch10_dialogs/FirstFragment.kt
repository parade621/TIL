package com.example.ch10_dialogs

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.ch10_dialogs.databinding.FragmentFirstBinding

class FirstFragment : Fragment(){

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            firstFragment=this@FirstFragment
        }
    }

    fun showDatePicker(){
        val dlg = DatePickerDialog(binding.root.context, object : DatePickerDialog.OnDateSetListener{
            override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
                Log.d("FirstFragment", "year:$p1, month: $p2, dayOfMonth: $p3")
            }
        }, 2023, 3,13).show()
    }

    fun showTimePicker(){
        TimePickerDialog(binding.root.context, object : TimePickerDialog.OnTimeSetListener {
            override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
                Log.d("FirstFragment","time: $p1, minute: $p2")
            }
        }, 15, 0, true).show()
    }

    fun showAlert(){
        AlertDialog.Builder(binding.root.context).run{
            setTitle("Test AlertDialog")
            setIcon(android.R.drawable.ic_dialog_alert)
            setMessage("알림입니다.")
            setPositiveButton("OK", null)
            setNegativeButton("Cancel", null)
            setNeutralButton("More", null)
            show()
        }
    }

    // 다이얼로그 버튼 클릭시 이벤트 지정.
    fun showEvent(){
        val eventHandler = object: DialogInterface.OnClickListener{
            override fun onClick(p0: DialogInterface?, p1: Int) {
                if(p1 == DialogInterface.BUTTON_POSITIVE){
                    Toast.makeText(binding.root.context,"긍정",Toast.LENGTH_SHORT).show()
                }
                else if(p1 == DialogInterface.BUTTON_NEGATIVE){
                    Toast.makeText(binding.root.context,"부정",Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(binding.root.context,"더보기",Toast.LENGTH_SHORT).show()
                }
            }
        }

            AlertDialog.Builder(binding.root.context).run{
                setTitle("Menu EventHandler")
                setIcon(android.R.drawable.ic_dialog_alert)
                setMessage("메뉴 이벤트 확인")
                setPositiveButton("확인", eventHandler)
                setNegativeButton("취소",eventHandler)
                setNeutralButton("더보기", eventHandler)
                show()
            }
    }

    fun showMenu(){
        val items = arrayOf<String>("사과","복숭아","수박","딸기")
        AlertDialog.Builder(binding.root.context).run{
            setTitle("items test")
            setIcon(android.R.drawable.ic_dialog_info)
            setItems(items, object : DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    Log.d("FirstFragment","선택한 과일: ${items[p1]}")
                }
            })
            setPositiveButton("닫기",null)
            show()
        }
    }
    fun showMenu2(){
        val items = arrayOf<String>("사과","복숭아","수박","딸기")
        AlertDialog.Builder(binding.root.context).run{
            setTitle("items test")
            setIcon(android.R.drawable.ic_dialog_info)
            setMultiChoiceItems(items, booleanArrayOf(true, false, false, false), object: DialogInterface.OnMultiChoiceClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int, p2: Boolean) {
                    Log.d("FirstFragment","${items[p1]}이 ${if(p2) "선택되었습니다" else "선택 해제되었습니다."}")
                }
            })
            setPositiveButton("닫기",null)
            show()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}