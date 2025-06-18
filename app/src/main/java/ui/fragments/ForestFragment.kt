package ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.forestapp.*
import com.example.forestapp.databinding.FragmentForestBinding
import com.example.forestapp.util.SharedPreferencesUtils

class ForestFragment : Fragment() {

    private var _binding: FragmentForestBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentForestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        applyBackgroundColor()

        binding.menuAchievements.setOnClickListener {
            startActivity(Intent(requireContext(), RozetActivity::class.java))
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

    private fun applyBackgroundColor() {
        val colorName = SharedPreferencesUtils.getBackgroundColor(requireContext())
        val colorResId = when (colorName) {
            "gereken_pembe" -> R.color.gereken_pembe
            "gereken_sari" -> R.color.gereken_sari
            else -> R.color.gereken_mavi
        }
        binding.root.setBackgroundColor(ContextCompat.getColor(requireContext(), colorResId))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}