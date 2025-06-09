package ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.forestapp.LoginActivity
import com.example.forestapp.R
import com.example.forestapp.model.User
import com.example.forestapp.repository.UserRepository
import com.example.forestapp.util.SharedPreferencesUtils

class ProfileFragment : Fragment() {

    private lateinit var tvName: TextView
    private lateinit var tvStats: TextView
    private lateinit var btnLogout: Button
    private val userRepo = UserRepository()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        tvName = view.findViewById(R.id.tvName)
        tvStats = view.findViewById(R.id.tvStats)
        btnLogout = view.findViewById(R.id.btnLogout)

        val userId = SharedPreferencesUtils.getUserId(requireContext())
        userRepo.getUserById(userId) { user ->
            user?.let { updateUI(it) }
        }

        btnLogout.setOnClickListener {
            SharedPreferencesUtils.clearPreferences(requireContext())
            startActivity(Intent(requireContext(), LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
        }

        return view
    }

    private fun updateUI(user: User) {
        tvName.text = user.name
        tvStats.text = """
            Odak Süresi: ${user.totalFocusTime} dk
            Toplam Balık: ${user.treesPlanted}
            Gerçek Balık: ${user.realTreesPlanted}
            Günlük Hedef: ${user.dailyGoal} dk
        """.trimIndent()
    }
}