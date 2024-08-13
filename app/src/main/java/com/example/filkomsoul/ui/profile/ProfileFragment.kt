package com.example.filkomsoul.ui.profile

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.filkomsoul.R
import com.example.filkomsoul.databinding.FragmentProfileBinding
import com.example.filkomsoul.ui.ViewModelFactory
import com.example.filkomsoul.ui.profile.archive.ArchiveActivity
import kotlin.system.exitProcess

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                AlertDialog.Builder(requireContext()).apply {
                    setMessage(getString(R.string.exit))
                    setPositiveButton("Yes") { _, _ ->
                        activity?.moveTaskToBack(true)
                        activity?.finish()
                    }
                    setNegativeButton("No", null)
                }.show()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        viewModel.getMahasiswa().observe(viewLifecycleOwner) {
            binding.name.text = it[0].parent.name
            binding.nim.text = it[0].parent.nim.toString()
            binding.major.text = resources.getString(R.string.major, it[0].parent.major, it[0].parent.year)
        }
        binding.btn1.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
        binding.btn2.setOnClickListener {
            startActivity(Intent(activity, ArchiveActivity::class.java))
        }
        binding.btn3.setOnClickListener {
            startActivity(Intent(activity, AboutAppActivity::class.java))
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}