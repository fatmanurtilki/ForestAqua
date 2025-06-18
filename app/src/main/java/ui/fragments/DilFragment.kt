package ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.forestapp.R
import com.example.forestapp.databinding.FragmentDilBinding
import com.example.forestapp.LanguageHelper
import com.example.forestapp.util.SharedPreferencesUtils

class DilFragment : Fragment() {

    private var _binding: FragmentDilBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDilBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        applySavedBackgroundColor()

        binding.backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.langTurkish.setOnClickListener {
            changeLanguage("tr")
        }

        binding.langEnglish.setOnClickListener {
            changeLanguage("en")
        }
    }

    private fun changeLanguage(langCode: String) {
        val currentLang = SharedPreferencesUtils.getAppLanguage(requireContext())
        if (currentLang != langCode) {
            SharedPreferencesUtils.setAppLanguage(requireContext(), langCode)
            Toast.makeText(requireContext(), getString(R.string.language_updated), Toast.LENGTH_SHORT).show()
            requireActivity().recreate()
        } else {
            Toast.makeText(requireContext(), getString(R.string.language_already_selected), Toast.LENGTH_SHORT).show()
        }
    }

    private fun applySavedBackgroundColor() {
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
    override fun onAttach(context: Context) {
        val lang = SharedPreferencesUtils.getAppLanguage(context)
        super.onAttach(LanguageHelper.setLocale(context, lang))
    }

}