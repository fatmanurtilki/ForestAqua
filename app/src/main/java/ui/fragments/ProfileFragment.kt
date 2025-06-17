package ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.forestapp.LoginActivity
import com.example.forestapp.R
import com.example.forestapp.UserRepository
import com.example.forestapp.util.SharedPreferencesUtils

class ProfileFragment : Fragment() {

    private lateinit var tvName: TextView
    private lateinit var tvStats: TextView
    private lateinit var btnLogout: Button
    private lateinit var userRepository: UserRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        tvName = view.findViewById(R.id.tvName)
        tvStats = view.findViewById(R.id.tvStats)
        btnLogout = view.findViewById(R.id.btnLogout)
        userRepository = UserRepository(requireContext())

<<<<<<< Updated upstream
        val user = userRepository.getUser()
        user?.let {
            tvName.text = it.name
            tvStats.text = """
                Odak Süresi: ${it.totalFocusTime} dk
                Toplam Balık: ${it.treesPlanted}
                Gerçek Balık: ${it.realTreesPlanted}
                Günlük Hedef: ${it.dailyGoal} dk
            """.trimIndent()
=======
        applyBackgroundColor(view)

        val userId = SharedPreferencesUtils.getUserId(requireContext())
        userRepo.getUserById(userId) { user ->
            user?.let { updateUI(it) }
>>>>>>> Stashed changes
        }

        btnLogout.setOnClickListener {
            SharedPreferencesUtils.clearPreferences(requireContext())
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        return view
    }
<<<<<<< Updated upstream
}
=======

    private fun updateUI(user: User) {
        tvName.text = user.name
        tvStats.text = """
            Odak Süresi: ${user.totalFocusTime} dk
            Toplam Balık: ${user.treesPlanted}
            Günlük Hedef: ${user.dailyGoal} dk
        """.trimIndent()
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
}
>>>>>>> Stashed changes
