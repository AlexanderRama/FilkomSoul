package com.example.filkomsoul.ui.screening

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.filkomsoul.R
import com.example.filkomsoul.databinding.FragmentScreeningBinding
import com.example.filkomsoul.databinding.PopScreeningBinding
import com.example.filkomsoul.ui.WebView
import com.example.filkomsoul.ui.screening.questionnaire.QuestionnaireActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlin.system.exitProcess

class ScreeningFragment : Fragment() {

    private var _binding: FragmentScreeningBinding? = null
    private val binding get() = _binding!!
    private var _binding2: PopScreeningBinding? = null
    private val binding2 get() = _binding2!!

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
        _binding = FragmentScreeningBinding.inflate(inflater, container, false)
        _binding2 = PopScreeningBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnMulai.setOnClickListener {
            showBottomSheetDialog()
        }
        binding.btnLinear1.setOnClickListener {
            val intent = Intent(activity, WebView::class.java)
            intent.putExtra(URLID, "https://www.who.int/health-topics/depression")
            startActivity(intent)
        }
        binding.btnLinear2.setOnClickListener {
            val intent = Intent(activity, WebView::class.java)
            intent.putExtra(URLID, "https://www.sciencedirect.com/science/article/pii/S016503271300222X#:~:text=The%20University%20Student%20Depression%20Inventory,rates%20of%20depressive%20symptoms%20among")
            startActivity(intent)
        }
        return root
    }

    private fun showBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(binding2.root, null)
        bottomSheetDialog.show()
        binding2.btnMulai.setOnClickListener {
            val intent = Intent(activity, QuestionnaireActivity::class.java)
            startActivity(intent)
        }
        bottomSheetDialog.setOnDismissListener {
            (binding2.root.parent as? ViewGroup)?.removeView(binding2.root)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val URLID = "urlID"
    }
}