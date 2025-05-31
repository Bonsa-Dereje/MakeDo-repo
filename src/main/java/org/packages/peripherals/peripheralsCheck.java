package org.packages.peripherals;

import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.UsbDevice;

import java.util.ArrayList;
import java.util.List;

public class peripheralsCheck {

    public static class PeripheralInfo {
        public String name;
        public boolean isWorking;

        public PeripheralInfo(String name, boolean isWorking) {
            this.name = name;
            this.isWorking = isWorking;
        }
    }

    public static class PeripheralSummary {
        public int totalUsbDevices;
        public List<PeripheralInfo> devices;

        public PeripheralSummary(int totalUsbDevices, List<PeripheralInfo> devices) {
            this.totalUsbDevices = totalUsbDevices;
            this.devices = devices;
        }
    }

    /**
     * Gathers the peripheral summary (total count and list of devices with status).
     */
    public static PeripheralSummary getPeripheralSummary() {
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();

        List<UsbDevice> usbDevices = hal.getUsbDevices(false); // top-level only
        int totalUsb = usbDevices.size();

        List<PeripheralInfo> deviceList = new ArrayList<>();
        for (UsbDevice device : usbDevices) {
            String name = device.getName();
            boolean isWorking = device.getVendorId() != null && device.getProductId() != null;
            deviceList.add(new PeripheralInfo(name, isWorking));
        }

        return new PeripheralSummary(totalUsb, deviceList);
    }

    private static void printPeripheralSummary(PeripheralSummary summary) {
        System.out.println("=== Peripheral Summary ===");
        System.out.println("Estimated Physical USB Ports in Use: " + summary.totalUsbDevices);
        System.out.println();

        for (PeripheralInfo device : summary.devices) {
            System.out.println("  - " + device.name + " [" + (device.isWorking ? "Working" : "Unknown Status") + "]");
        }
    }

    public static void main(String[] args) {
        PeripheralSummary summary = getPeripheralSummary();
        printPeripheralSummary(summary);
    }
}
