package ui.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.forestapp.LanguageHelper
import com.example.forestapp.R
import com.example.forestapp.TreeType
import com.example.forestapp.databinding.FragmentStatisticsBinding
import com.example.forestapp.util.SharedPreferencesUtils
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class StatisticsFragment : Fragment() {

    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!

    enum class FilterType {
        DAY, WEEK, MONTH, YEAR
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyBackgroundColor()

        binding.rbDay.setOnClickListener { drawStaticChart(FilterType.DAY) }
        binding.rbWeek.setOnClickListener { drawStaticChart(FilterType.WEEK) }
        binding.rbMonth.setOnClickListener { drawStaticChart(FilterType.MONTH) }
        binding.rbYear.setOnClickListener { drawStaticChart(FilterType.YEAR) }

        binding.rbDay.performClick()
    }

    private fun drawStaticChart(filter: FilterType) {
        val (labels, values, fishCount) = when (filter) {
            FilterType.DAY -> {
                val vals = listOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 2, 0, 3, 0, 2, 0, 0, 0, 0, 0, 0, 0)
                Triple((0..23).map { "$it:00" }, vals, vals.sum() / 3)
            }
            FilterType.WEEK -> {
                val vals = listOf(1, 3, 0, 4, 2, 3, 1)
                Triple(listOf("Pzt", "Sal", "Çar", "Per", "Cum", "Cmt", "Paz"), vals, vals.sum() / 3)
            }
            FilterType.MONTH -> {
                val vals = listOf(
                    0, 0, 0, 1, 0, 2, 4, 3, 2, 1,
                    2, 2, 0, 0, 1, 1, 0, 1, 1, 0,
                    1, 1, 2, 1, 0, 2, 0, 0, 0, 2
                )
                Triple((1..30).map { it.toString() }, vals, vals.sum() / 5)
            }
            FilterType.YEAR -> {
                val vals = listOf(2, 1, 3, 0, 2, 4, 1, 2, 3, 1, 2, 4)
                Triple(
                    listOf("Oca", "Şub", "Mar", "Nis", "May", "Haz", "Tem", "Ağu", "Eyl", "Eki", "Kas", "Ara"),
                    vals, vals.sum() / 3
                )
            }
        }

        displayFishes(fishCount)

        val entries = values.mapIndexed { index, value -> BarEntry(index.toFloat() + 0.5f, value.toFloat()) }
        val labelText = getString(R.string.focus_time_label)

        val barData = BarData(
            BarDataSet(entries, labelText).apply { color = Color.parseColor("#008577") }
        ).apply {
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
                valueFormatter = IndexAxisValueFormatter(labels)
                setDrawGridLines(false)
                axisMinimum = 0f
                axisMaximum = labels.size.toFloat()
                labelRotationAngle = -45f
                textSize = 10f
            }

            axisLeft.apply {
                axisMinimum = 0f
                granularity = 1f
                textSize = 10f
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
                val drawableId = TreeType.treeDrawables[allTypes.random()] ?: R.drawable.beta_balik

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

    private fun applyBackgroundColor() {
        val colorName = SharedPreferencesUtils.getBackgroundColor(requireContext())
        val colorResId = when (colorName) {
            "gereken_pembe" -> R.color.gereken_pembe
            "gereken_sari" -> R.color.gereken_sari
            else -> R.color.gereken_mavi
        }
        binding.root.setBackgroundColor(ContextCompat.getColor(requireContext(), colorResId))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onAttach(context: Context) {
        val lang = SharedPreferencesUtils.getAppLanguage(context)
        super.onAttach(LanguageHelper.setLocale(context, lang))
    }
}