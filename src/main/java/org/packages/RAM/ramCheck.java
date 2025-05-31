package org.packages.RAM;

import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.PhysicalMemory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ramCheck {

    public static class RAMInfo {
        public double totalMemoryGB;
        public double availableMemoryGB;
        public List<Module> modules = new ArrayList<>();
        public int estimatedSlots;

        public static class Module {
            public String bankLabel;
            public double capacityGB;
            public String manufacturer;
            public String memoryType;
            public String clockSpeedMHz;
            public String transferRateMTps;
        }
    }

    public static RAMInfo getRAMInfo() {
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hal = systemInfo.getHardware();
        GlobalMemory memory = hal.getMemory();
        List<PhysicalMemory> physicalMemories = memory.getPhysicalMemory();

        RAMInfo info = new RAMInfo();
        info.totalMemoryGB = memory.getTotal() / 1e9;
        info.availableMemoryGB = memory.getAvailable() / 1e9;

        Set<String> bankLabels = new HashSet<>();

        for (PhysicalMemory mem : physicalMemories) {
            bankLabels.add(mem.getBankLabel());

            RAMInfo.Module module = new RAMInfo.Module();
            module.bankLabel = mem.getBankLabel();
            module.capacityGB = mem.getCapacity() / 1e9;
            module.manufacturer = mem.getManufacturer();
            module.memoryType = mem.getMemoryType();
            module.clockSpeedMHz = mem.getClockSpeed() > 0
                    ? (mem.getClockSpeed() / 1_000_000) + " MHz"
                    : "Unknown";
            module.transferRateMTps = mem.getClockSpeed() > 0
                    ? ((mem.getClockSpeed() / 1_000_000) * 2) + " MT/s"
                    : "Unknown";

            info.modules.add(module);
        }

        info.estimatedSlots = bankLabels.size();
        return info;
    }

    public static void main(String[] args) {
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hal = systemInfo.getHardware();
        GlobalMemory memory = hal.getMemory();
        List<PhysicalMemory> physicalMemories = memory.getPhysicalMemory();

        System.out.printf("Total Memory: %.2f GB%n", memory.getTotal() / 1e9);
        System.out.printf("Available Memory: %.2f GB%n", memory.getAvailable() / 1e9);

        Set<String> bankLabels = new HashSet<>();
        for (PhysicalMemory mem : physicalMemories) {
            bankLabels.add(mem.getBankLabel());
        }

        System.out.println("\nPhysical Memory Modules:");
        for (int i = 0; i < physicalMemories.size(); i++) {
            PhysicalMemory mem = physicalMemories.get(i);
            long clockHz = mem.getClockSpeed();
            long mtPerSec = (clockHz > 0) ? (clockHz / 1_000_000) * 2 : -1;

            System.out.printf("Module %d:%n", i + 1);
            System.out.println("  Bank Label: " + mem.getBankLabel());
            System.out.printf("  Capacity: %.2f GB%n", mem.getCapacity() / 1e9);
            System.out.println("  Manufacturer: " + mem.getManufacturer());
            System.out.println("  Memory Type: " + mem.getMemoryType());
            System.out.println("  Clock Speed: " + (clockHz > 0 ? (clockHz / 1_000_000) + " MHz" : "Unknown"));
            System.out.println("  Estimated Mega Transfers/sec (MT/s): " + (mtPerSec > 0 ? mtPerSec + " MT/s" : "Unknown"));
            System.out.println();
        }

        System.out.println("RAM Slots Used: " + physicalMemories.size());
        System.out.println("Estimated Total RAM Slots: " + bankLabels.size());
    }
}
