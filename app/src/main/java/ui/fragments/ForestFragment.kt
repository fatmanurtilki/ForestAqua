package ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.forestapp.R
import com.example.forestapp.RozetActivity
import com.example.forestapp.ShopActivity
import com.example.forestapp.databinding.FragmentForestBinding


class ForestFragment : Fragment() {

    private var _binding: FragmentForestBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

         binding.menuAchievements.setOnClickListener {
             startActivity(Intent(requireContext(), RozetActivity::class.java))
         }

         binding.menuShop.setOnClickListener {
             startActivity(Intent(requireContext(), ShopActivity::class.java))
         }

        binding.menuStatistics.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, StatisticsFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.menuUser.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ProfileFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
