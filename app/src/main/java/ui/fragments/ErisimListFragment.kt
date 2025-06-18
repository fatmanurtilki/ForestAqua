package ui.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.widget.CheckBox
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.forestapp.R
import com.example.forestapp.databinding.FragmentErisimlistBinding
import com.example.forestapp.util.SharedPreferencesUtils

class ErisimListFragment : Fragment() {

    private var _binding: FragmentErisimlistBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var appCheckBoxes: List<CheckBox>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentErisimlistBinding.inflate(inflater, container, false)
        sharedPrefs = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        applyBackgroundColor()

        binding.backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        appCheckBoxes = listOf(
            binding.cbTiktok, binding.cbInstagram, binding.cbFacebook,
            binding.cbSnapchat, binding.cbPinterest, binding.cbTwitter,
            binding.cbDiscord, binding.cbTwitch, binding.cbLinkedin
        )

        val blocked = sharedPrefs.getStringSet("blocked_apps", emptySet()) ?: emptySet()

        appCheckBoxes.forEach { checkbox ->
            val key = checkbox.tag?.toString() ?: checkbox.text.toString()
            checkbox.isChecked = blocked.contains(key)
            checkbox.setOnCheckedChangeListener { _, _ -> saveSelections() }
        }
    }

    private fun saveSelections() {
        val selected = appCheckBoxes
            .filter { it.isChecked }
            .map { it.tag?.toString() ?: it.text.toString() }
            .toSet()
        sharedPrefs.edit().putStringSet("blocked_apps", selected).apply()
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