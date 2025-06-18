package ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.forestapp.R
import com.example.forestapp.databinding.FragmentStatisticsBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.example.forestapp.TreeType

class StatisticsFragment : Fragment() {

    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rbDay.setOnClickListener { drawStaticChart(FilterType.DAY) }
        binding.rbWeek.setOnClickListener { drawStaticChart(FilterType.WEEK) }
        binding.rbMonth.setOnClickListener { drawStaticChart(FilterType.MONTH) }
        binding.rbYear.setOnClickListener { drawStaticChart(FilterType.YEAR) }

        binding.rbDay.performClick()
    }

    private fun drawStaticChart(filter: FilterType) {
        val labels: List<String>
        val values: List<Int>

        var dayTotal = 0
        var weekTotal = 0
        var monthTotal = 0
        var yearTotal = 0

        var dayFishCount = 0
        var weekFishCount = 0
        var monthFishCount = 0
        var yearFishCount = 0

        var currentFishCount = 0

        when (filter) {
            FilterType.DAY -> {
                labels = (0..23).map { "$it:00" }
                values = listOf(
                    0, 0, 0, 0, 0, 0, 0, 0,   // 00-07
                    0, 0, 1, 0, 2, 0, 3, 0,   // 08-15
                    2, 0, 0, 0, 0, 0, 0, 0    // 16-23
                )
                dayTotal = values.sum()
                dayFishCount = dayTotal / 3
                currentFishCount = dayFishCount
            }

            FilterType.WEEK -> {
                labels = listOf("Pzt", "Sal", "Çar", "Per", "Cum", "Cmt", "Paz")
                values = listOf(1, 3, 0, 4, 2, 3, 1)   // zaten sabitti
                weekTotal = values.sum()
                weekFishCount = weekTotal / 3
                currentFishCount = weekFishCount + dayFishCount
            }

            FilterType.MONTH -> {
                labels = (1..30).map { it.toString() }
                values = listOf(
                    0, 0, 0, 1, 0, 2, 4, 3, 2, 1,
                    2, 2, 0, 0, 1, 1, 0, 1, 1, 0,
                    1, 1, 2, 1, 0, 2, 0, 0, 0, 2
                )
                monthTotal = values.sum()
                monthFishCount = monthTotal / 5
                currentFishCount = monthFishCount + weekFishCount
            }

            FilterType.YEAR -> {
                labels = listOf("Oca", "Şub", "Mar", "Nis", "May", "Haz", "Tem", "Ağu",
                    "Eyl", "Eki", "Kas", "Ara")
                values = listOf(2, 1, 3, 0, 2, 4, 1, 2, 3, 1, 2, 4)  // zaten sabitti
                yearTotal = values.sum()
                yearFishCount = yearTotal / 3
                currentFishCount = yearFishCount + monthFishCount
            }
        }

        displayFishes(currentFishCount)

        val entries = values.mapIndexed { index, value ->
            BarEntry(index.toFloat() + 0.5f, value.toFloat())
        }

        val dataSet = BarDataSet(entries, "Odak Süresi (dk)").apply {
            color = Color.parseColor("#008577")
        }

        val barData = BarData(dataSet).apply {
            barWidth = 0.7f
            setValueTextSize(10f)
        }

        binding.barChart.apply {
            data = barData
            setFitBars(true)
            description.isEnabled = false
            setScaleEnabled(false)
            setPinchZoom(false)
            setDrawBarShadow(false)
            setDrawValueAboveBar(true)

            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                granularity = 1f
                setGranularityEnabled(true)
                valueFormatter = IndexAxisValueFormatter(labels)
                setDrawGridLines(false)
                axisMinimum = 0f
                axisMaximum = labels.size.toFloat()
                setCenterAxisLabels(false)
                setLabelCount(labels.size, false)
                labelRotationAngle = -45f
                textSize = 10f
                yOffset = 10f
            }

            axisLeft.apply {
                axisMinimum = 0f
                setDrawGridLines(false)
                granularity = 1f
                textSize = 10f
                axisLineWidth = 1f
            }

            axisRight.isEnabled = false
            legend.isEnabled = false

            extraBottomOffset = 10f
            setVisibleXRangeMaximum(labels.size.toFloat())
            moveViewToX(0f)
            animateY(500)
            invalidate()
        }
    }

    private fun displayFishes(fishCount: Int) {
        val container = binding.flAquariumContainer
        container.post {
            container.removeViews(1, container.childCount - 1)

            val fishSize = 80
            val spacing = 24
            val totalWidth = fishCount * fishSize + (fishCount - 1) * spacing
            val startX = (container.width - totalWidth) / 2
            val baseY = (container.height - fishSize) / 2

            val allTypes = TreeType.getAllTypes()

            for (i in 0 until fishCount) {
                val fishType = allTypes.random()
                val drawableId = TreeType.treeDrawables[fishType] ?: R.drawable.beta_balik

                val fish = ImageView(requireContext()).apply {
                    setImageResource(drawableId)
                    layoutParams = FrameLayout.LayoutParams(fishSize, fishSize).apply {
                        leftMargin = startX + i * (fishSize + spacing)
                        topMargin = baseY + if (i % 2 == 0) 0 else 30
                    }
                    alpha = 0f
                    scaleX = 0f
                    scaleY = 0f
                }

                fish.animate()
                    .alpha(1f)
                    .scaleX(1f)
                    .scaleY(1f)
                    .setStartDelay((i * 100).toLong())
                    .setDuration(500)
                    .start()

                container.addView(fish)
            }
        }
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    enum class FilterType {
        DAY, WEEK, MONTH, YEAR
    }
}
