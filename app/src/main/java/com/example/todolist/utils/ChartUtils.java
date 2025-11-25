package com.example.todolist.utils;

import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

public class ChartUtils {

    private static final SimpleDateFormat sdfFull = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat sdfShort = new SimpleDateFormat("dd/MM");

    // -------------------------------------------------------------
    //  LINE CHART
    // -------------------------------------------------------------
    public static void setupLineChart(LineChart lineChart, LinkedHashMap<String, Integer> data) {

        if (data == null || data.isEmpty()) {
            lineChart.clear();
            lineChart.setNoDataText("Không có dữ liệu để hiển thị");
            return;
        }

        // --- Sort theo ngày ---
        List<String> sortedDates = new ArrayList<>(data.keySet());
        Collections.sort(sortedDates); // yyyy-MM-dd

        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        int index = 0;
        for (String date : sortedDates) {
            Integer value = data.get(date);

            // ❗ Chỉ vẽ khi NGÀY CÓ DỮ LIỆU
            if (value == null) continue;

            entries.add(new Entry(index, value));
            labels.add(date);
            index++;
        }

        // Nếu không còn entry ⇒ không hiển thị
        if (entries.isEmpty()) {
            lineChart.clear();
            lineChart.setNoDataText("Không có dữ liệu để hiển thị");
            return;
        }

        // --- Dataset ---
        LineDataSet dataSet = new LineDataSet(entries, "Nhiệm vụ hoàn thành");
        dataSet.setColor(Color.rgb(66, 133, 244));
        dataSet.setCircleColor(Color.RED);
        dataSet.setLineWidth(2.5f);
        dataSet.setCircleRadius(4.5f);
        dataSet.setValueTextSize(10f);

        lineChart.setData(new LineData(dataSet));

        // --- Trục X ---
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(labels.size(), true);
        xAxis.setLabelRotationAngle(-30f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int i = (int) value;
                if (i >= 0 && i < labels.size()) {
                    try {
                        Date d = new SimpleDateFormat("yyyy-MM-dd").parse(labels.get(i));
                        return new SimpleDateFormat("dd/MM").format(d);
                    } catch (Exception e) {
                        return labels.get(i);
                    }
                }
                return "";
            }
        });

        // --- Y axis ---
        lineChart.getAxisLeft().setAxisMinimum(0);
        lineChart.getAxisRight().setEnabled(false);

        lineChart.getDescription().setText("Thống kê theo ngày");
        lineChart.animateY(800);
        lineChart.invalidate();
    }


    // -------------------------------------------------------------
    //  PIE CHART
    // -------------------------------------------------------------
    public static void setupPieChart(PieChart pieChart, int completed, int notCompleted) {

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(completed, "Đã hoàn thành"));
        entries.add(new PieEntry(notCompleted, "Chưa hoàn thành"));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(
                Color.rgb(66, 133, 244),
                Color.rgb(255, 202, 40),
                Color.GRAY,
                Color.RED
        );

        int total = completed + notCompleted;

        PieData pieData = new PieData(dataSet);
        pieData.setValueTextSize(14f);
        pieData.setValueTextColor(Color.WHITE);

        // Hiển thị % đúng tỷ lệ
        pieData.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if (total == 0) return "0%";
                return String.format("%.0f%%", (value / total) * 100f);
            }
        });

        pieChart.setUsePercentValues(false); // Tắt % mặc định của thư viện
        pieChart.setData(pieData);
        pieChart.invalidate();
    }

}
