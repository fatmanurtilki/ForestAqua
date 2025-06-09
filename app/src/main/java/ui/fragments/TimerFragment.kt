package ui.fragments

import android.animation.ValueAnimator
import android.app.AlertDialog
import android.os.Bundle
import android.os.CountDownTimer
import android.view.*
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.forestapp.R
import com.example.forestapp.databinding.FragmentTimerBinding
import com.example.forestapp.model.Session
import com.example.forestapp.repository.SessionRepository
import com.example.forestapp.repository.UserRepository
import com.example.forestapp.util.SharedPreferencesUtils
import com.example.forestapp.TreeType
import java.util.*

class TimerFragment : Fragment() {

    private var _binding: FragmentTimerBinding? = null
    private val binding get() = _binding!!

    private lateinit var userRepo: UserRepository
    private lateinit var sessionRepo: SessionRepository

    private var timer: CountDownTimer? = null
    private var isTimerRunning = false
    private var isPaused = false
    private var timeLeftInMillis = 25 * 60 * 1000L
    private var currentTreeType = TreeType.PALYACO
    private var elapsedMillis = 0L

    private lateinit var floatAnimator: ValueAnimator

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTimerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userRepo = UserRepository()
        sessionRepo = SessionRepository()
        setupUI()
        updateTreeSelectionUI()
        startFloatingAnimation()
    }

    private fun setupUI() {
        updateTimerText()

        binding.btnStart.setOnClickListener {
            if (isPaused) startTimer()
            else if (isTimerRunning) pauseTimer()
            else startTimer()
        }

        binding.btnPause.setOnClickListener {
            stopTimerAndSaveSession()
        }

        binding.btnTreeType.setOnClickListener {
            showTreeSelectionDialog()
        }

        binding.ivJar.setImageResource(R.drawable.glass_jar)
    }

    private fun startTimer() {
        timer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                elapsedMillis = 25 * 60 * 1000 - millisUntilFinished
                updateTimerText()
            }

            override fun onFinish() {
                onTimerComplete()
            }
        }.start()

        isTimerRunning = true
        isPaused = false
        binding.btnStart.text = getString(R.string.pause)
        floatAnimator.resume()
    }

    private fun pauseTimer() {
        timer?.cancel()
        isTimerRunning = false
        isPaused = true
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
            sessionRepo.insertSession(session)
            userRepo.addCoins(userId, minutes.toInt() * 2)
            userRepo.addFocusTime(userId, minutes.toInt())
            Toast.makeText(requireContext(), "${minutes * 2} coin kazandınız!", Toast.LENGTH_SHORT).show()
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
        sessionRepo.insertSession(session)
        userRepo.addCoins(userId, TreeType.getCoinValue(currentTreeType))
        binding.ivTree.setImageResource(getTreeImage(true))
        resetTimer()
        growTree()
    }

    private fun resetTimer() {
        timeLeftInMillis = 25 * 60 * 1000
        updateTimerText()
        binding.btnStart.text = getString(R.string.start)
        isPaused = false
    }

    private fun getTreeImage(isAdult: Boolean): Int {
        return TreeType.treeDrawables[currentTreeType] ?: R.drawable.palyaco_balik
    }

    private fun updateTimerText() {
        val minutes = (timeLeftInMillis / 1000) / 60
        val seconds = (timeLeftInMillis / 1000) % 60
        binding.tvTimer.text = String.format("%02d:%02d", minutes, seconds)
    }

    private fun updateTreeSelectionUI() {
        binding.btnTreeType.text = currentTreeType
    }

    private fun startFloatingAnimation() {
        floatAnimator = ValueAnimator.ofFloat(-15f, 15f).apply {
            duration = 4000
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
            addUpdateListener {
                val scale = it.animatedValue as Float
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