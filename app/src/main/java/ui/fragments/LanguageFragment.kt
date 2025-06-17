package ui.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.forestapp.R
import com.example.forestapp.databinding.FragmentLanguageBinding
import com.example.forestapp.util.LocaleHelper
import com.example.forestapp.util.SharedPreferencesUtils

class LanguageFragment : Fragment() {

    private var _binding: FragmentLanguageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLanguageBinding.inflate(inflater, container, false)

        applyBackgroundColor()

        binding.backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.langTurkish.setOnClickListener {
            updateLanguage("tr")
        }

        binding.langEnglish.setOnClickListener {
            updateLanguage("en")
        }

        return binding.root
    }

    private fun updateLanguage(langCode: String) {
        SharedPreferencesUtils.setAppLanguage(requireContext(), langCode)
        LocaleHelper.setLocale(requireContext(), langCode)
        Toast.makeText(requireContext(), "Language updated", Toast.LENGTH_SHORT).show()
        requireActivity().recreate()
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