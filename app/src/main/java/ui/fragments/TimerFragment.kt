package ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import android.view.ViewGroup
import android.animation.ValueAnimator
import android.view.animation.LinearInterpolator
import androidx.fragment.app.Fragment
import com.example.forestapp.*
import com.example.forestapp.databinding.FragmentTimerBinding
import com.example.forestapp.util.SharedPreferencesUtils
import java.util.*

class TimerFragment : Fragment() {

    private var _binding: FragmentTimerBinding? = null
    private val binding get() = _binding!!

    private lateinit var userRepository: UserRepository
    private lateinit var sessionRepository: SessionRepository

    private var timer: CountDownTimer? = null
    private var isTimerRunning = false
    private var timeLeftInMillis: Long = 25 * 60 * 1000 // 25 dk
    private var currentTreeType = TreeType.PALYACO
    private var elapsedMillis: Long = 0L

    private lateinit var floatAnimator: ValueAnimator
    private val FLOAT_AMPLITUDE = 15f
    private val FLOAT_DURATION = 4000L

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
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
        binding.ivJar.setImageResource(R.drawable.glass_jar)
        startFloatingAnimation()
    }

    private fun setupUI() {
        updateTimerText()

        binding.btnStart.setOnClickListener {
            if (isTimerRunning) pauseTimer() else startTimer()
        }

        binding.btnPause.setOnClickListener {
            stopTimerAndSaveSession()
        }

        binding.btnTreeType.setOnClickListener {
            showTreeSelectionDialog()
        }
    }

    private fun startTimer() {
      //  val startTime = System.currentTimeMillis()

        timer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateTimerText()
                elapsedMillis = 25 * 60 * 1000 - millisUntilFinished
            }

            override fun onFinish() {
                onTimerComplete()
            }
        }.start()

        isTimerRunning = true
        binding.btnStart.text = getString(R.string.pause)
        binding.ivTree.setImageResource(getTreeImage(false))
    }

    private fun pauseTimer() {
        timer?.cancel()
        isTimerRunning = false
        binding.btnStart.text = getString(R.string.start)
        floatAnimator.pause()
    }

    private fun stopTimerAndSaveSession() {
        timer?.cancel()
        isTimerRunning = false
        val minutes = (elapsedMillis / 1000) / 60
        if (minutes >= 1) {
            val userId = SharedPreferencesUtils.getUserId(requireContext())
            val session = Session(
                duration = minutes.toInt() * 60,
                treeType = currentTreeType,
                date = Date(),
                successful = false,
                userId = userId
            )
            sessionRepository.insertSession(session)

            val earnedCoins = minutes.toInt() * 2
            userRepository.addCoinsForUser(userId, earnedCoins)
            userRepository.addFocusTime(userId, minutes.toInt())

            updateUserInfo()
            Toast.makeText(requireContext(), "$earnedCoins coin kazanıldı!", Toast.LENGTH_SHORT).show()
        }

        resetTimer()
    }

    private fun onTimerComplete() {
        val userId = SharedPreferencesUtils.getUserId(requireContext())
        val session = Session(
            duration = 25 * 60,
            treeType = currentTreeType,
            date = Date(),
            successful = true,
            userId = userId
        )
        sessionRepository.insertSession(session)
        userRepository.addCoins(TreeType.getCoinValue(currentTreeType))
        updateUserInfo()
        binding.ivTree.setImageResource(getTreeImage(true))
        resetTimer()
        growTree()
    }

    private fun resetTimer() {
        timeLeftInMillis = 25 * 60 * 1000
        updateTimerText()
        binding.btnStart.text = getString(R.string.start)
    }

    private fun getTreeImage(isAdult: Boolean): Int {
        return TreeType.treeDrawables[currentTreeType] ?: R.drawable.palyaco_balik
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

    private fun startFloatingAnimation() {
        floatAnimator = ValueAnimator.ofFloat(-FLOAT_AMPLITUDE, FLOAT_AMPLITUDE).apply {
            duration = FLOAT_DURATION
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            interpolator = LinearInterpolator()

            addUpdateListener { animation ->
                val value = animation.animatedValue as Float
                binding.ivTree.translationY = value
                binding.ivTree.translationX = value * 0.9f
                binding.ivTree.rotation = value * 0.5f
            }
        }
        floatAnimator.start()
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

    private fun showTreeSelectionDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Balık Seç")
            .setItems(TreeType.getAllTypes().toTypedArray()) { _, which ->
                currentTreeType = TreeType.getAllTypes()[which]
                updateTreeSelectionUI()
            }
            .setNegativeButton("İptal", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timer?.cancel()
        floatAnimator.cancel()
        _binding = null
    }
}