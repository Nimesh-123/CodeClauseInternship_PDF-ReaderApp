package com.example.codeclauseinternship.pdfreader.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.codeclauseinternship.pdfreader.R
import com.example.codeclauseinternship.pdfreader.databinding.FragmentBookMarkBinding

class BookMarkFragment : Fragment() {

    lateinit var binding: FragmentBookMarkBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentBookMarkBinding.inflate(layoutInflater,container,false)






        return binding.root
    }

}