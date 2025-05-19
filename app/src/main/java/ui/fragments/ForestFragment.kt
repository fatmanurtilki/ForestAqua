package ui.fragments

//burası aslında menu fragment için

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.forestapp.R
import com.example.forestapp.databinding.FragmentForestBinding
import com.example.forestapp.RozetActivity
import com.example.forestapp.ShopActivity
// Diğer aktiviteler için aynı şekilde
import ui.fragments.ForestFragment
import ui.fragments.ProfileFragment


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

        // Menü öğelerini ayarla
        binding.menuAchievements.setOnClickListener {
            startActivity(Intent(requireActivity(), RozetActivity::class.java))
        }

        binding.menuShop.setOnClickListener {
            startActivity(Intent(requireContext(), ShopActivity::class.java))
        }

        /*binding.menuStatistics.setOnClickListener {
            startActivity(Intent(requireContext(), StatisticsActivity::class.java))
        }
        binding.menuSettings.setOnClickListener {
            startActivity(Intent(requireContext(), SettingsActivity::class.java))
        }*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}