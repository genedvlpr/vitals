package com.orbitsoftlabs.vitals.DiseaseUtils.TabUtils;

import com.anychart.chart.common.dataentry.ValueDataEntry;

public class CustomDataEntry extends ValueDataEntry {
    public CustomDataEntry(String x, Number value, Number jumpLine) {
        super(x, value);
        setValue("jumpLine", jumpLine);
    }
}