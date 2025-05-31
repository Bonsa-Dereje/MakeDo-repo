package org.packages.battery;

import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.PowerSource;

import java.util.ArrayList;
import java.util.List;

public class batteryCheck {

    public static class BatteryInfo {
        public String name;
        public double voltage;
        public double amperage;
        public boolean isCharging;
        public double remainingCapacityPercent;
        public double timeRemainingSeconds;
        public double powerUsageRate;
        public double designCapacityWh;
        public double maxCapacityWh;
        public double currentCapacityWh;
        public double designCapacitymAh;
        public double maxCapacitymAh;
        public double currentCapacitymAh;
        public int cycleCount;
    }

    public static List<BatteryInfo> getBatteryInfo() {
        List<BatteryInfo> batteryList = new ArrayList<>();
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        List<PowerSource> batteries = hal.getPowerSources();

        for (PowerSource battery : batteries) {
            BatteryInfo info = new BatteryInfo();
            double voltage = battery.getVoltage();

            info.name = battery.getName();
            info.voltage = voltage;
            info.amperage = battery.getAmperage();
            info.isCharging = battery.isCharging();
            info.remainingCapacityPercent = battery.getRemainingCapacityPercent() * 100;
            info.timeRemainingSeconds = battery.getTimeRemainingEstimated();
            info.powerUsageRate = battery.getPowerUsageRate();
            info.designCapacityWh = battery.getDesignCapacity();
            info.maxCapacityWh = battery.getMaxCapacity();
            info.currentCapacityWh = battery.getCurrentCapacity();
            info.designCapacitymAh = voltage > 0 ? (info.designCapacityWh * 1000) / voltage : 0;
            info.maxCapacitymAh = voltage > 0 ? (info.maxCapacityWh * 1000) / voltage : 0;
            info.currentCapacitymAh = voltage > 0 ? (info.currentCapacityWh * 1000) / voltage : 0;
            info.cycleCount = battery.getCycleCount();

            batteryList.add(info);
        }

        return batteryList;
    }

    public static void main(String[] args) {
        List<BatteryInfo> batteryStats = getBatteryInfo();

        if (batteryStats.isEmpty()) {
            System.out.println("No battery detected.");
            return;
        }

        for (int i = 0; i < batteryStats.size(); i++) {
            BatteryInfo b = batteryStats.get(i);
            System.out.printf("Battery %d:%n", i + 1);
            System.out.println("  Name: " + b.name);
            System.out.printf("  Remaining Capacity: %.2f%%%n", b.remainingCapacityPercent);
            System.out.println("  Time Remaining (s): " + b.timeRemainingSeconds);
            System.out.println("  Power Usage Rate (W): " + b.powerUsageRate);
            System.out.println("  Voltage (V): " + b.voltage);
            System.out.println("  Amperage (A): " + b.amperage);
            System.out.println("  Is Charging: " + b.isCharging);
            System.out.printf("  Design Capacity (Wh): %.2f (%.0f mAh)%n", b.designCapacityWh, b.designCapacitymAh);
            System.out.printf("  Max Capacity (Wh): %.2f (%.0f mAh)%n", b.maxCapacityWh, b.maxCapacitymAh);
            System.out.printf("  Current Capacity (Wh): %.2f (%.0f mAh)%n", b.currentCapacityWh, b.currentCapacitymAh);
            System.out.println("  Cycle Count: " + b.cycleCount);
            System.out.println("------------------------------");
        }
    }
}
