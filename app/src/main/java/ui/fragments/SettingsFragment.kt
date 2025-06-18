package ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.forestapp.R
import com.example.forestapp.databinding.FragmentSettingsBinding
import com.example.forestapp.util.SharedPreferencesUtils
import com.example.forestapp.LanguageHelper

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        applyBackgroundColor(view)

        binding.buttonBackground.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ArkaPlanRenkFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.buttonAccess.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ErisimListFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.buttonLanguage.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, DilFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.buttonSound.setOnClickListener {
            // Ses ayarları fragmenti buraya eklenebilir.
        }
    }

    private fun applyBackgroundColor(view: View) {
        val colorName = SharedPreferencesUtils.getBackgroundColor(requireContext())
        val colorResId = when (colorName) {
            "gereken_pembe" -> R.color.gereken_pembe
            "gereken_sari" -> R.color.gereken_sari
            else -> R.color.gereken_mavi
        }
        view.setBackgroundColor(ContextCompat.getColor(requireContext(), colorResId))
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