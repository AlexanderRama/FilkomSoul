package com.example.filkomsoul.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.filkomsoul.R
import com.example.filkomsoul.databinding.FragmentHomeBinding
import com.example.filkomsoul.databinding.PopScreeningBinding
import com.example.filkomsoul.ui.ViewModelFactory
import com.example.filkomsoul.ui.WebView
import com.example.filkomsoul.ui.home.article.ArticleAdapter
import com.example.filkomsoul.ui.home.article.ArticleViewModel
import com.example.filkomsoul.ui.home.article.NewsActivity
import com.example.filkomsoul.ui.screening.ScreeningFragment
import com.example.filkomsoul.ui.screening.questionnaire.QuestionnaireActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlin.math.roundToInt


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var _binding2: PopScreeningBinding? = null
    private val binding2 get() = _binding2!!
    private val homeViewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private val newsViewModel by viewModels<ArticleViewModel>()
    private lateinit var articleAdapter: ArticleAdapter

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
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        _binding2 = PopScreeningBinding.inflate(inflater, container, false)
        articleAdapter = ArticleAdapter()
        binding.listarchive.adapter = articleAdapter
        val layoutManager = LinearLayoutManager(requireContext())
        binding.listarchive.layoutManager = layoutManager
        binding.listarchive.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)

        newsViewModel.listNews.observe(viewLifecycleOwner) { listnews ->
            articleAdapter.submitList(listnews)
        }

        binding.image.shapeAppearanceModel = binding.image.shapeAppearanceModel
            .toBuilder()
            .setAllCornerSizes(24f)
            .build()

        binding.btnWeb.setOnClickListener {
            val intent = Intent(activity, WebView::class.java)
            intent.putExtra(ScreeningFragment.URLID, "https://filkom.ub.ac.id/kemahasiswaan/bimbingan-dan-konseling-di-filkom/")
            startActivity(intent)
        }

        binding.btnMulai.setOnClickListener {
            showBottomSheetDialog()
        }

        binding.more.setOnClickListener{
            val intent = Intent(activity, NewsActivity::class.java)
            startActivity(intent)
        }

        homeViewModel.getAll().observe(viewLifecycleOwner){
            if(it.isNotEmpty()) {
                binding.progress.alpha = 1f
                binding.title2.alpha = 1f
                binding.title21.alpha = 1f

                binding.img.alpha = 0f
                binding.title1.alpha = 0f
                binding.title11.alpha = 0f
                binding.title12.alpha = 0f

                binding.result.text = it.last().total.toString()
                binding.progressbar.progress = (it.last().total*100/150f).roundToInt()
                binding.summary.text = it.last().result
            }
        }
        return binding.root
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
}