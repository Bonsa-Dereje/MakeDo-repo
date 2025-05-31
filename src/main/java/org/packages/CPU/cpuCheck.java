package org.packages.CPU;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;

public class cpuCheck {

    public static class CPUInfo {
        public String processorName;
        public String vendor;
        public String identifier;
        public String microarchitecture;
        public int physicalCores;
        public int logicalCores;
        public long maxFreqHz;
        public long[] currentFreqsHz;
        public double systemCpuLoad; // 0.0 to 1.0
        public long uptimeSeconds;
    }

    public static CPUInfo getCPUInfo() {
        SystemInfo si = new SystemInfo();
        CentralProcessor cpu = si.getHardware().getProcessor();

        long[] oldTicks = cpu.getSystemCpuLoadTicks();

        try {
            Thread.sleep(1000); // wait 1 second to measure load
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        double load = cpu.getSystemCpuLoadBetweenTicks(oldTicks);
        if (load < 0) {
            load = 0;
        }

        CPUInfo info = new CPUInfo();
        info.processorName = cpu.getProcessorIdentifier().getName();
        info.vendor = cpu.getProcessorIdentifier().getVendor();
        info.identifier = cpu.getProcessorIdentifier().getIdentifier();
        info.microarchitecture = cpu.getProcessorIdentifier().getMicroarchitecture();
        info.physicalCores = cpu.getPhysicalProcessorCount();
        info.logicalCores = cpu.getLogicalProcessorCount();

        info.maxFreqHz = cpu.getMaxFreq();
        if (info.maxFreqHz <= 0) {
            info.maxFreqHz = cpu.getProcessorIdentifier().getVendorFreq();
        }

        info.systemCpuLoad = load;
        info.uptimeSeconds = si.getOperatingSystem().getSystemUptime();
        info.currentFreqsHz = cpu.getCurrentFreq();

        return info;
    }

    public static void main(String[] args) {
        CPUInfo info = getCPUInfo();

        System.out.println("CPU Model: " + info.processorName);
        System.out.println("Vendor: " + info.vendor);
        System.out.println("Identifier: " + info.identifier);
        System.out.println("Microarchitecture: " + info.microarchitecture);
        System.out.println("Physical cores: " + info.physicalCores);
        System.out.println("Logical cores: " + info.logicalCores);
        System.out.println("Max Frequency (Hz): " + info.maxFreqHz);

        System.out.printf("Current Frequencies (Hz): ");
        if (info.currentFreqsHz != null && info.currentFreqsHz.length > 0) {
            for (long freq : info.currentFreqsHz) {
                System.out.printf("%d ", freq);
            }
        } else {
            System.out.print("N/A");
        }
        System.out.println();

        System.out.printf("System CPU Load: %.2f%%\n", info.systemCpuLoad * 100);
        System.out.println("System Uptime (seconds): " + info.uptimeSeconds);
    }
}
