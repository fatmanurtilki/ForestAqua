package ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.forestapp.R
import com.example.forestapp.Session
import com.example.forestapp.SessionRepository
import com.example.forestapp.TreeType
import com.example.forestapp.UserRepository
import com.example.forestapp.databinding.FragmentTimerBinding
import java.util.Date
import android.animation.ValueAnimator
import android.view.animation.LinearInterpolator

class TimerFragment : Fragment() {
    private var _binding: FragmentTimerBinding? = null
    private val binding get() = _binding!!
    private lateinit var userRepository: UserRepository
    private lateinit var sessionRepository: SessionRepository

    private var timer: CountDownTimer? = null
    private var isTimerRunning = false
    private var timeLeftInMillis: Long = 1 * 60 * 1000 // 25 dakika
    private var currentTreeType = TreeType.PALYACO

    private lateinit var floatAnimator: ValueAnimator
    private val FLOAT_AMPLITUDE = 15f // Yükselme miktarı (dp)
    private val FLOAT_DURATION = 4000L // Bir döngü süresi

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTimerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userRepository = UserRepository(requireContext())
        sessionRepository = SessionRepository(requireContext())

        setupUI()
        updateUserInfo()
        updateTreeSelectionUI()

        // Fanus görselini ayarla
        binding.ivJar.setImageResource(R.drawable.glass_jar)

        // Animasyonu başlat
        startFloatingAnimation()
    }

    private fun setupUI() {
        updateTimerText()

        binding.btnStart.setOnClickListener {
            if (isTimerRunning) pauseTimer() else startTimer()
        }

        binding.btnTreeType.setOnClickListener {
            showTreeSelectionDialog()
        }
    }

    private fun startTimer() {
        timer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateTimerText()
            }

            override fun onFinish() {
                onTimerComplete()
            }
        }.start()

        isTimerRunning = true
        binding.btnStart.text = getString(R.string.pause)
        binding.ivTree.setImageResource(getTreeImage(false))
    }

    private fun onTimerComplete() {
        // Session kaydet
        val session = Session(
            duration = (timeLeftInMillis / 1000).toInt(),
            treeType = currentTreeType,
            date = Date(),
            successful = true
        )
        sessionRepository.insertSession(session)

        // Kullanıcıya jeton ekle
        userRepository.addCoins(TreeType.getCoinValue(currentTreeType))

        // UI güncelle
        binding.ivTree.setImageResource(getTreeImage(true))
        updateUserInfo()
        resetTimer()

        growTree()
    }

    private fun getTreeImage(isAdult: Boolean): Int {
        return when (currentTreeType) {
            TreeType.ASTRONOT -> if (isAdult) R.drawable.astronot_balik else R.drawable.astronot_balik
            TreeType.PALYACO -> if (isAdult) R.drawable.palyaco_balik else R.drawable.palyaco_balik
            TreeType.DISKUS -> if (isAdult) R.drawable.diskus_balik else R.drawable.diskus_balik
            TreeType.JAPON -> if (isAdult) R.drawable.japon_balik else R.drawable.japon_balik
            TreeType.MOLI -> if (isAdult) R.drawable.moli_balik else R.drawable.moli_balik
            TreeType.MELEK -> if (isAdult) R.drawable.melek_balik else R.drawable.melek_balik
            TreeType.BETA -> if (isAdult) R.drawable.beta_balik else R.drawable.beta_balik
            TreeType.PAPAGAN -> if (isAdult) R.drawable.papagan_balik else R.drawable.papagan_balik
            else -> R.drawable.papagan_balik
        }
    }

    private fun updateTimerText() {
        val minutes = (timeLeftInMillis / 1000) / 60
        val seconds = (timeLeftInMillis / 1000) % 60
        binding.tvTimer.text = String.format("%02d:%02d", minutes, seconds)
    }

    private fun updateUserInfo() {
        val user = userRepository.getUser()
        user?.let {
            binding.tvCoins.text = "Coins: ${it.coins}"
        }
    }

    private fun updateTreeSelectionUI() {
        binding.btnTreeType.text = currentTreeType

    }

    private fun getTreeIcon(treeType: String): Int {
        return when (treeType) {
            TreeType.ASTRONOT -> R.drawable.astronot_balik
            TreeType.PALYACO -> R.drawable.palyaco_balik
            TreeType.DISKUS -> R.drawable.diskus_balik
            TreeType.JAPON -> R.drawable.japon_balik
            TreeType.MOLI -> R.drawable.moli_balik
            TreeType.MELEK-> R.drawable.melek_balik
            TreeType.BETA -> R.drawable.beta_balik
            TreeType.PAPAGAN -> R.drawable.papagan_balik
            else -> R.drawable.palyaco_balik
        }
    }

    private fun startFloatingAnimation() {
        floatAnimator = ValueAnimator.ofFloat(-FLOAT_AMPLITUDE, FLOAT_AMPLITUDE).apply {
            duration = FLOAT_DURATION
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            interpolator = LinearInterpolator()

            addUpdateListener { animation ->
                val value = animation.animatedValue as Float
                binding.ivTree.translationY = value

                // Hafif yatay salınım (isteğe bağlı)
                binding.ivTree.translationX = value * 0.9f

                // Hafif dönme efekti (isteğe bağlı)
                binding.ivTree.rotation = value * 0.5f
            }
        }
        floatAnimator.start()
    }

    private fun pauseTimer() {
        timer?.cancel()
        isTimerRunning = false
        binding.btnStart.text = getString(R.string.start)
        floatAnimator.pause()
    }


    private fun growTree() {
        ValueAnimator.ofFloat(1f, 1.3f).apply {
            duration = 1500
            addUpdateListener { animation ->
                val scale = animation.animatedValue as Float
                binding.ivTree.scaleX = scale
                binding.ivTree.scaleY = scale
            }
            start()
        }
    }

    private fun resetTimer() {
        timeLeftInMillis = 25 * 60 * 1000
        updateTimerText()
        binding.btnStart.text = getString(R.string.start)
        isTimerRunning = false
    }

    private fun showTreeSelectionDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Select Fish Type")
            .setItems(TreeType.getAllTypes().toTypedArray()) { _, which ->
                currentTreeType = TreeType.getAllTypes()[which]
                updateTreeSelectionUI()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timer?.cancel()
        _binding = null
        floatAnimator.cancel()
    }
}