<<<<<<< Updated upstream

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
=======
    package ui.fragments

    import android.animation.ValueAnimator
    import android.app.AlertDialog
    import android.content.Context
    import android.content.SharedPreferences
    import android.media.MediaPlayer
    import android.os.Bundle
    import android.os.CountDownTimer
    import android.os.Handler
    import android.text.InputType
    import android.view.*
    import android.view.animation.LinearInterpolator
    import android.view.inputmethod.EditorInfo
    import android.widget.AdapterView
    import android.widget.ArrayAdapter
    import android.widget.EditText
    import android.widget.Toast
    import androidx.core.content.ContextCompat
    import androidx.fragment.app.Fragment
    import com.example.forestapp.*
    import com.example.forestapp.databinding.FragmentTimerBinding
    import com.example.forestapp.model.Session
    import com.example.forestapp.repository.SessionRepository
    import com.example.forestapp.repository.UserRepository
    import com.example.forestapp.util.SharedPreferencesUtils
    import java.util.*
>>>>>>> Stashed changes

    class TimerFragment : Fragment() {

        private var _binding: FragmentTimerBinding? = null
        private val binding get() = _binding!!

<<<<<<< Updated upstream
    private lateinit var userRepository: UserRepository
    private lateinit var sessionRepository: SessionRepository

    private var timer: CountDownTimer? = null
    private var isTimerRunning = false
    private var timeLeftInMillis: Long = 25 * 60 * 1000 // 25 dk
    private var currentTreeType = TreeType.PALYACO
    private var elapsedMillis: Long = 0L
=======
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
>>>>>>> Stashed changes

        private lateinit var floatAnimator: ValueAnimator
        private val FLOAT_AMPLITUDE = 15f
        private val FLOAT_DURATION = 4000L

<<<<<<< Updated upstream
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
=======
        // ✅ MOTİVASYONLAR
        private val motivasyonListesi = listOf(
            "Her yeni gün yeni bir başlangıçtır.",
            "Hayallerine bir adım daha yaklaşmak için bugünü kullan.",
            "Küçük adımlar büyük değişimler getirir.",
            "Şu an başlamak için en iyi zamandır.",
            "Bugün odaklan, yarın gurur duy.",
            "Sadece denemeye devam et.",
            "İstikrar başarıyı getirir.",
            "Sen yapabilirsin!"
        )
        private val motivasyonHandler = Handler()
        private lateinit var motivasyonRunnable: Runnable

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
            _binding = FragmentTimerBinding.inflate(inflater, container, false)
            return binding.root
>>>>>>> Stashed changes
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            userRepository = UserRepository()
            sessionRepository = SessionRepository()
            sharedPreferences = requireContext().getSharedPreferences("ForestPrefs", Context.MODE_PRIVATE)
            selectedSound = sharedPreferences.getString("selectedSound", "forest") ?: "forest"

            setupUI()
            setupSoundSpinner()
            setupTimerEditText()
            updateTreeSelectionUI()
            updateUserInfo()
            startFloatingAnimation()
            binding.ivJar.setImageResource(R.drawable.glass_jar)

            startMotivasyonDegisimi()
        }

        private fun setupUI() {
            updateTimerText()

<<<<<<< Updated upstream
    private fun startTimer() {
        val startTime = System.currentTimeMillis()

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
=======
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

        private fun setupTimerEditText() {
            binding.tvTimer.setOnClickListener {
                showTimeInputDialog()
            }

            binding.tvTimer.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    validateAndSetTime(binding.tvTimer.text.toString())
                    true
                } else {
                    false
                }
            }
        }

        private fun showTimeInputDialog() {
            val input = EditText(requireContext()).apply {
                inputType = InputType.TYPE_CLASS_NUMBER
                setText((timeLeftInMillis / 60000).toString())
            }

            AlertDialog.Builder(requireContext())
                .setTitle("Süreyi Ayarla (Dakika)")
                .setView(input)
                .setPositiveButton("Tamam") { _, _ ->
                    validateAndSetTime(input.text.toString())
                }
                .setNegativeButton("İptal", null)
                .show()
        }

        private fun validateAndSetTime(input: String) {
            val minutes = input.toIntOrNull()?.coerceIn(1, 120) ?: 25
            timeLeftInMillis = minutes * 60 * 1000L
            updateTimerText()

            if (isTimerRunning) {
                timer?.cancel()
                startTimer()
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
                    elapsedMillis = (timeLeftInMillis / 60000 + 1) * 60 * 1000 - millisUntilFinished
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
                userRepository.addCoins(userId, minutes.toInt() * 20)
                userRepository.addFocusTime(userId, minutes.toInt())
                Toast.makeText(requireContext(), "${minutes * 20} coin kazandınız!", Toast.LENGTH_SHORT).show()
            }

            resetTimer()
        }

        private fun onTimerComplete() {
>>>>>>> Stashed changes
            val userId = SharedPreferencesUtils.getUserId(requireContext())
            val session = Session(
                duration = (timeLeftInMillis / 60000).toInt() * 60,
                treeType = currentTreeType,
                date = Date(),
                successful = true,
                userId = userId
            )
            sessionRepository.insertSession(session)
<<<<<<< Updated upstream

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
=======
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
                "forest" -> R.raw.huzur
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
>>>>>>> Stashed changes

        private fun updateTimerText() {
            val minutes = (timeLeftInMillis / 1000) / 60
            val seconds = (timeLeftInMillis / 1000) % 60
            binding.tvTimer.setText(String.format("%02d:%02d", minutes, seconds))
        }

<<<<<<< Updated upstream
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
=======
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
>>>>>>> Stashed changes
            }
        }

<<<<<<< Updated upstream
    private fun growTree() {
        ValueAnimator.ofFloat(1f, 1.3f).apply {
            duration = 1500
            addUpdateListener { animation ->
                val scale = animation.animatedValue as Float
                binding.ivTree.scaleX = scale
                binding.ivTree.scaleY = scale
            }
            start()
=======
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
>>>>>>> Stashed changes
        }

<<<<<<< Updated upstream
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
=======
        // motivasyo loop
        private fun startMotivasyonDegisimi() {
            motivasyonRunnable = object : Runnable {
                override fun run() {
                    val rastgele = motivasyonListesi.random()
                    binding.tvMotivasyon.text = rastgele
                    motivasyonHandler.postDelayed(this, 10000)
                }
            }
            motivasyonHandler.post(motivasyonRunnable)
        }

        override fun onDestroyView() {
            super.onDestroyView()
            timer?.cancel()
            floatAnimator.cancel()
            stopSound()
            motivasyonHandler.removeCallbacks(motivasyonRunnable)
            _binding = null
        }
        override fun onResume() {
            super.onResume()
            applyBackgroundColor()
        }

        private fun applyBackgroundColor() {
            val colorName = SharedPreferencesUtils.getBackgroundColor(requireContext())
            val colorResId = when (colorName) {
                "gereken_pembe" -> R.color.gereken_pembe
                "gereken_sari" -> R.color.gereken_sari
                else -> R.color.gereken_mavi
            }
            view?.setBackgroundColor(ContextCompat.getColor(requireContext(), colorResId))
        }

    }
>>>>>>> Stashed changes
