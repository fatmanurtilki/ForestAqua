package ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.forestapp.R
import com.example.forestapp.databinding.FragmentArkaplanrenkBinding
import com.example.forestapp.util.SharedPreferencesUtils
import com.example.forestapp.LanguageHelper

class ArkaPlanRenkFragment : Fragment() {

    private var _binding: FragmentArkaplanrenkBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArkaplanrenkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        applySavedBackgroundColor()

        binding.backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.colorMavi.setOnClickListener {
            changeBackground("gereken_mavi")
        }

        binding.colorPembe.setOnClickListener {
            changeBackground("gereken_pembe")
        }

        binding.colorSari.setOnClickListener {
            changeBackground("gereken_sari")
        }
    }
    private fun changeBackground(colorKey: String) {
        SharedPreferencesUtils.setBackgroundColor(requireContext(), colorKey)
        requireActivity().recreate()
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