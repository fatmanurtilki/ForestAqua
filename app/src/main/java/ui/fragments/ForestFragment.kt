package ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.forestapp.*
import com.example.forestapp.databinding.FragmentForestBinding

class ForestFragment : Fragment() {

    private var _binding: FragmentForestBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentForestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.menuAchievements.setOnClickListener {
            startActivity(Intent(requireContext(), RozetActivity::class.java))
        }

        binding.menugidis.setOnClickListener {
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