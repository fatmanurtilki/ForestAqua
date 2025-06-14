package ui.fragments

import android.animation.ValueAnimator
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.*
import android.view.animation.LinearInterpolator
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.forestapp.*
import com.example.forestapp.databinding.FragmentTimerBinding
import com.example.forestapp.model.Session
import com.example.forestapp.repository.SessionRepository
import com.example.forestapp.repository.UserRepository
import com.example.forestapp.util.SharedPreferencesUtils
import java.util.*

class TimerFragment : Fragment() {

    private var _binding: FragmentTimerBinding? = null
    private val binding get() = _binding!!

    private lateinit var userRepository: UserRepository
    private lateinit var sessionRepository: SessionRepository
    private lateinit var sharedPreferences: SharedPreferences
    private var mediaPlayer: MediaPlayer? = null

    private var timer: CountDownTimer? = null
    private var isTimerRunning = false
    private var isPaused = false
    private var timeLeftInMillis: Long = 25 * 60 * 1000
    private var currentTreeType = TreeType.PALYACO
    private var elapsedMillis: Long = 0L
    private var selectedSound = "forest"

    private lateinit var floatAnimator: ValueAnimator
    private val FLOAT_AMPLITUDE = 15f
    private val FLOAT_DURATION = 4000L

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTimerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userRepository = UserRepository()
        sessionRepository = SessionRepository()
        sharedPreferences = requireContext().getSharedPreferences("ForestPrefs", Context.MODE_PRIVATE)
        selectedSound = sharedPreferences.getString("selectedSound", "forest") ?: "forest"

        setupUI()
        setupSoundSpinner()
        updateTreeSelectionUI()
        updateUserInfo()
        startFloatingAnimation()
        binding.ivJar.setImageResource(R.drawable.glass_jar)
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
    }

    private fun setupSoundSpinner() {
        val soundOptions = arrayOf("forest", "rain", "sea")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, soundOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.soundSpinner.adapter = adapter

        binding.soundSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedSound = soundOptions[position]
                sharedPreferences.edit().putString("selectedSound", selectedSound).apply()
                if (isTimerRunning) startSound()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        val selectedIndex = soundOptions.indexOf(selectedSound)
        if (selectedIndex != -1) {
            binding.soundSpinner.setSelection(selectedIndex)
        }
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
        startSound()
    }

    private fun pauseTimer() {
        timer?.cancel()
        isTimerRunning = false
        isPaused = true
        binding.btnStart.text = getString(R.string.start)
        floatAnimator.pause()
        stopSound()
    }

    private fun stopTimerAndSaveSession() {
        timer?.cancel()
        isTimerRunning = false
        stopSound()

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
            userRepository.addCoins(userId, minutes.toInt() * 2)
            userRepository.addFocusTime(userId, minutes.toInt())
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
        sessionRepository.insertSession(session)
        userRepository.addCoins(userId, TreeType.getCoinValue(currentTreeType))
        binding.ivTree.setImageResource(getTreeImage(true))
        growTree()
        resetTimer()
        stopSound()
    }

    private fun resetTimer() {
        timeLeftInMillis = 25 * 60 * 1000
        updateTimerText()
        binding.btnStart.text = getString(R.string.start)
        isPaused = false
    }

    private fun startSound() {
        val soundResId = when (selectedSound) {
            "forest" -> R.raw.forest
            "rain" -> R.raw.rainforest
            "sea" -> R.raw.ocean_waves
            else -> R.raw.cricket
        }

        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(requireContext(), soundResId)
        mediaPlayer?.isLooping = true
        mediaPlayer?.start()
    }

    private fun stopSound() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun updateTimerText() {
        val minutes = (timeLeftInMillis / 1000) / 60
        val seconds = (timeLeftInMillis / 1000) % 60
        binding.tvTimer.text = String.format("%02d:%02d", minutes, seconds)
    }

    private fun updateTreeSelectionUI() {
        binding.btnTreeType.text = currentTreeType
    }

    private fun updateUserInfo() {
        val userId = SharedPreferencesUtils.getUserId(requireContext())
        userRepository.getUserById(userId) { user ->
            user?.let {
                binding.tvCoins.text = "Coins: ${it.coins}"
            }
        }
    }

    private fun getTreeImage(isAdult: Boolean): Int {
        return TreeType.treeDrawables[currentTreeType] ?: R.drawable.palyaco_balik
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
                binding.ivTree.setImageResource(getTreeImage(false))
            }
            .setNegativeButton("İptal", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timer?.cancel()
        floatAnimator.cancel()
        stopSound()
        _binding = null
    }
}
