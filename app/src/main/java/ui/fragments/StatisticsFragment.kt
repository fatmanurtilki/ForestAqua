package ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.forestapp.R
import com.example.forestapp.SessionRepository
import com.example.forestapp.TreeType
import com.example.forestapp.util.SharedPreferencesUtils
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.util.*

class StatisticsFragment : Fragment() {

    private lateinit var barChart: BarChart
    private lateinit var radioGroup: RadioGroup
    private lateinit var rbDay: RadioButton
    private lateinit var rbWeek: RadioButton
    private lateinit var rbMonth: RadioButton
    private lateinit var rbYear: RadioButton
    private lateinit var sessionRepository: SessionRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_statistics, container, false)

        sessionRepository = SessionRepository(requireContext())

        radioGroup = view.findViewById(R.id.rgFilter)
        rbDay = view.findViewById(R.id.rbDay)
        rbWeek = view.findViewById(R.id.rbWeek)
        rbMonth = view.findViewById(R.id.rbMonth)
        rbYear = view.findViewById(R.id.rbYear)
        barChart = view.findViewById(R.id.barChart)

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbDay -> loadData(Calendar.HOUR_OF_DAY, 24) { cal -> cal.get(Calendar.HOUR_OF_DAY) }
                R.id.rbWeek -> loadData(Calendar.DAY_OF_WEEK, 7) { cal -> (cal.get(Calendar.DAY_OF_WEEK) + 5) % 7 }
                R.id.rbMonth -> loadData(Calendar.DAY_OF_MONTH, 30) { cal -> cal.get(Calendar.DAY_OF_MONTH) - 1 }
                R.id.rbYear -> loadData(Calendar.MONTH, 12) { cal -> cal.get(Calendar.MONTH) }
            }
        }

        rbDay.isChecked = true
        return view
    }

    private fun loadData(field: Int, range: Int, getKey: (Calendar) -> Int) {
        val userId = SharedPreferencesUtils.getUserId(requireContext())
        if (userId == -1) return

        val sessions = sessionRepository.getSessionsForUser(userId)
        val dataMap = mutableMapOf<Int, Int>()
        for (i in 0 until range) dataMap[i] = 0

        sessions.forEach { session ->
            val cal = Calendar.getInstance().apply { time = session.date }
            val key = getKey(cal)
            val coin = TreeType.getCoinValue(session.treeType)
            dataMap[key] = dataMap[key]?.plus(coin) ?: coin
        }

        val entries = dataMap.map { BarEntry(it.key.toFloat(), it.value.toFloat()) }

        val dataSet = BarDataSet(entries, "Kazanç")
        dataSet.color = ContextCompat.getColor(requireContext(), R.color.purple_500)
        val barData = BarData(dataSet)
        barData.barWidth = 0.9f

        barChart.data = barData
        barChart.description.isEnabled = false
        barChart.setFitBars(true)

        val xAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.setDrawGridLines(false)
        xAxis.valueFormatter = IndexAxisValueFormatter(
            when (field) {
                Calendar.HOUR_OF_DAY -> (0..23).map { "$it" }
                Calendar.DAY_OF_WEEK -> listOf("Pzt", "Sal", "Çar", "Per", "Cum", "Cmt", "Paz")
                Calendar.DAY_OF_MONTH -> (1..30).map { "$it" }
                Calendar.MONTH -> listOf("Oca", "Şub", "Mar", "Nis", "May", "Haz", "Tem", "Ağu", "Eyl", "Eki", "Kas", "Ara")
                else -> emptyList()
            }
        )

        barChart.axisLeft.axisMinimum = 0f
        barChart.axisRight.isEnabled = false
        barChart.invalidate()
    }
}
