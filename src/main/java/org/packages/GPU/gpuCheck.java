package org.packages.GPU;

import oshi.SystemInfo;
import oshi.hardware.GraphicsCard;
import oshi.hardware.HardwareAbstractionLayer;

import java.util.ArrayList;
import java.util.List;

public class gpuCheck {

    public static class GPUInfo {
        public String name;
        public String vendor;
        public long vramBytes;
        public String deviceId;
        public String versionInfo;
    }

    public static List<GPUInfo> getGPUInfo() {
        List<GPUInfo> gpuList = new ArrayList<>();
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        List<GraphicsCard> gpus = hal.getGraphicsCards();

        for (GraphicsCard gpu : gpus) {
            GPUInfo info = new GPUInfo();
            info.name = gpu.getName();
            info.vendor = gpu.getVendor();
            info.vramBytes = gpu.getVRam();
            info.deviceId = gpu.getDeviceId();
            info.versionInfo = gpu.getVersionInfo();

            gpuList.add(info);
        }

        return gpuList;
    }

    public static void main(String[] args) {
        List<GPUInfo> gpuStats = getGPUInfo();

        if (gpuStats.isEmpty()) {
            System.out.println("No GPU detected.");
            return;
        }

        for (int i = 0; i < gpuStats.size(); i++) {
            GPUInfo g = gpuStats.get(i);
            System.out.printf("GPU %d:%n", i + 1);
            System.out.println("  Name        : " + g.name);
            System.out.println("  Vendor      : " + g.vendor);
            System.out.println("  VRAM        : " + (g.vramBytes / (1024 * 1024)) + " MB");
            System.out.println("  Device ID   : " + g.deviceId);
            System.out.println("  Version Info: " + g.versionInfo);
            System.out.println("----------------------------------");
        }
    }
}
