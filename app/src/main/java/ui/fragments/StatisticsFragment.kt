// StatisticsFragment.kt (güncellenmiş hali)
package ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.forestapp.SessionRepository
import com.example.forestapp.UserRepository
import com.example.forestapp.databinding.FragmentStatisticsBinding
import com.example.forestapp.util.SharedPreferencesUtils
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.text.SimpleDateFormat
import java.util.*

class StatisticsFragment : Fragment() {

    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!

    private lateinit var sessionRepository: SessionRepository
    private lateinit var userRepository: UserRepository
    private var userId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userId = SharedPreferencesUtils.getUserId(requireContext())
        sessionRepository = SessionRepository(requireContext())
        userRepository = UserRepository(requireContext())

        val user = userRepository.getUserById(userId)
        binding.tvCoinCount.text = "${user?.coins ?: 0} Coin"

        binding.rbDay.setOnClickListener { loadData(FilterType.DAY) }
        binding.rbWeek.setOnClickListener { loadData(FilterType.WEEK) }
        binding.rbMonth.setOnClickListener { loadData(FilterType.MONTH) }
        binding.rbYear.setOnClickListener { loadData(FilterType.YEAR) }

        binding.rbDay.performClick()
    }

    private fun loadData(filter: FilterType) {
        val sessions = sessionRepository.getSessionsForUser(userId)
        val grouped = when (filter) {
            FilterType.DAY -> groupByHour(sessions)
            FilterType.WEEK -> groupByDayOfWeek(sessions)
            FilterType.MONTH -> groupByDayOfMonth(sessions)
            FilterType.YEAR -> groupByMonth(sessions)
        }

        val entries = grouped.mapIndexed { index, pair ->
            BarEntry(index.toFloat(), pair.second.toFloat())
        }

        val dataSet = BarDataSet(entries, "Odak Süresi (dk)")
        dataSet.color = Color.parseColor("#008577")

        val data = BarData(dataSet)
        data.barWidth = 0.9f

        binding.barChart.apply {
            this.data = data
            setFitBars(true)
            description.isEnabled = false
            xAxis.valueFormatter = IndexAxisValueFormatter(grouped.map { it.first })
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.granularity = 1f
            xAxis.setDrawGridLines(false)
            axisLeft.axisMinimum = 0f
            axisRight.isEnabled = false
            axisLeft.setDrawGridLines(false)
            setVisibleXRangeMaximum(6f)
            isDragEnabled = true
            setScaleEnabled(false)
            invalidate()
        }
    }

    private fun groupByHour(sessions: List<com.example.forestapp.Session>): List<Pair<String, Int>> {
        val result = IntArray(24)
        val formatter = SimpleDateFormat("HH", Locale.getDefault())
        sessions.forEach {
            val hour = formatter.format(it.date).toInt()
            result[hour] += it.duration / 60
        }
        return result.mapIndexed { i, v -> Pair("$i:00", v) }
    }

    private fun groupByDayOfWeek(sessions: List<com.example.forestapp.Session>): List<Pair<String, Int>> {
        val days = listOf("Pzt", "Sal", "Çar", "Per", "Cum", "Cmt", "Paz")
        val result = IntArray(7)
        val cal = Calendar.getInstance()
        sessions.forEach {
            cal.time = it.date
            val index = (cal.get(Calendar.DAY_OF_WEEK) + 5) % 7
            result[index] += it.duration / 60
        }
        return days.mapIndexed { i, name -> Pair(name, result[i]) }
    }

    private fun groupByDayOfMonth(sessions: List<com.example.forestapp.Session>): List<Pair<String, Int>> {
        val result = IntArray(31)
        val cal = Calendar.getInstance()
        sessions.forEach {
            cal.time = it.date
            val day = cal.get(Calendar.DAY_OF_MONTH) - 1
            result[day] += it.duration / 60
        }
        return result.mapIndexed { i, v -> Pair("${i + 1}", v) }
    }

    private fun groupByMonth(sessions: List<com.example.forestapp.Session>): List<Pair<String, Int>> {
        val months = listOf("Oca", "Şub", "Mar", "Nis", "May", "Haz", "Tem", "Ağu", "Eyl", "Eki", "Kas", "Ara")
        val result = IntArray(12)
        val cal = Calendar.getInstance()
        sessions.forEach {
            cal.time = it.date
            val month = cal.get(Calendar.MONTH)
            result[month] += it.duration / 60
        }
        return months.mapIndexed { i, name -> Pair(name, result[i]) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    enum class FilterType {
        DAY, WEEK, MONTH, YEAR
    }
}
