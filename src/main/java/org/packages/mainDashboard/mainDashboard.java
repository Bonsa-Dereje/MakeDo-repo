package org.packages.mainDashboard;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import java.awt.*;
import java.util.List;

import org.packages.diskDrives.diskCheck;
import org.packages.CPU.cpuCheck;
import org.packages.GPU.gpuCheck;
import org.packages.RAM.ramCheck;
import org.packages.battery.batteryCheck;
import org.packages.screen.screenCheck;
import org.packages.OS.osCheck; 
import org.packages.peripherals.peripheralsCheck;// <-- Added
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.UsbDevice;
import oshi.SystemInfo;

class mainDashboard {

    static class RoundedPanel extends JPanel {
        private int radius;

        public RoundedPanel(int radius) {
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    public void showMainDashboard() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf.");
        }

        JFrame mainDashboardWindow = new JFrame("MakeDo - Dashboard");
        mainDashboardWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainDashboardWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainDashboardWindow.setLayout(new FlowLayout());

        JPanel mainDash = new JPanel(new FlowLayout(FlowLayout.LEFT, 7, 5));
        JPanel mainSubPanel = new JPanel();
        mainSubPanel.setLayout(new BoxLayout(mainSubPanel, BoxLayout.Y_AXIS));
        mainSubPanel.add(Box.createVerticalStrut(15));

        // --- Hard Disk Panel ---
        JPanel hrSubPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 3));
        JPanel hddDash = new RoundedPanel(20);
        hddDash.setLayout(new BoxLayout(hddDash, BoxLayout.Y_AXIS));
        hddDash.setBackground(Color.decode("#B0C4DE"));
        hddDash.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel harddiskTtl = new JLabel("Hard Disk");
        harddiskTtl.setFont(new Font("Roboto", Font.BOLD, 15));
        hddDash.add(Box.createVerticalStrut(2));

        ImageIcon hddImg = new ImageIcon("G:\\Dev Softwares\\MakeDo\\assets\\hard-disk-drive.png");
        Image hddScaledImage = hddImg.getImage().getScaledInstance(23, 23, Image.SCALE_SMOOTH);
        JLabel hdd_imgLabel = new JLabel(new ImageIcon(hddScaledImage));

        hrSubPanel.add(harddiskTtl);
        hrSubPanel.add(hdd_imgLabel);
        hrSubPanel.setOpaque(false);
        hddDash.add(hrSubPanel);

        UIManager.put("Label.font", new Font("Roboto", Font.PLAIN, 13));

        List<diskCheck.DriveInfo> drives = diskCheck.scanAllDrives();
        for (int i = 0; i < drives.size(); i++) {
            diskCheck.DriveInfo drive = drives.get(i);
            hddDash.add(Box.createVerticalStrut(5));

            hddDash.add(new JLabel("Drive: " + drive.name));
            hddDash.add(new JLabel("Health: " + drive.health + " / 100"));
            hddDash.add(new JLabel("Bad Sectors: " + drive.badSectors));

            if (!drive.issues.isEmpty()) {
                for (String issue : drive.issues) {
                    String cleaned = issue.replaceAll("\\s*\\(.*?\\)", "").trim();
                    hddDash.add(new JLabel(cleaned));
                }
            }

            if (!drive.partitions.isEmpty()) {
                hddDash.add(new JLabel("Partitions:"));
                for (diskCheck.PartitionInfo p : drive.partitions) {
                    String partInfo = String.format(" - %s: %.2f GB used / %.2f GB total (%.2f GB free)",
                            p.name, p.usedGB, p.totalGB, p.freeGB);
                    hddDash.add(new JLabel(partInfo));
                }
            }

            if (i < drives.size() - 1) {
                hddDash.add(Box.createVerticalStrut(10));
                hddDash.add(new JSeparator(SwingConstants.HORIZONTAL));
            }
        }



        // ---------- CPU Panel ----------
        JPanel cpuDash = new RoundedPanel(20);
        cpuDash.setLayout(new BoxLayout(cpuDash, BoxLayout.Y_AXIS));
        cpuDash.setBackground(Color.decode("#B0C4DE"));
        cpuDash.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel cpuTtlSub = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 3));
        cpuTtlSub.setOpaque(false);

        JLabel cpuTtl = new JLabel("CPU");
        cpuTtl.setFont(new Font("Roboto", Font.BOLD, 15));

        ImageIcon cpuImg = new ImageIcon("C:\\Users\\boni\\Desktop\\Files\\MakeDo\\assets\\processor.png");
        Image cpuScaledImage = cpuImg.getImage().getScaledInstance(23, 23, Image.SCALE_SMOOTH);
        JLabel cpu_imgLabel = new JLabel(new ImageIcon(cpuScaledImage));

        cpuTtlSub.add(cpuTtl);
        cpuTtlSub.add(cpu_imgLabel);
        cpuDash.add(cpuTtlSub);

        cpuCheck.CPUInfo cpuInfo = cpuCheck.getCPUInfo();

        cpuDash.add(Box.createVerticalStrut(5));
        cpuDash.add(new JLabel("Model: " + cpuInfo.processorName));
        cpuDash.add(new JLabel("Vendor: " + cpuInfo.vendor));
        cpuDash.add(new JLabel("Identifier: " + cpuInfo.identifier));
        cpuDash.add(new JLabel("Microarchitecture: " + cpuInfo.microarchitecture));
        cpuDash.add(new JLabel("Physical Cores: " + cpuInfo.physicalCores));
        cpuDash.add(new JLabel("Logical Cores: " + cpuInfo.logicalCores));
        cpuDash.add(new JLabel(String.format("Max Frequency: %.2f GHz", cpuInfo.maxFreqHz / 1_000_000_000.0)));

        // ---------- RAM Panel ----------
        JPanel ramDash = new RoundedPanel(20);
        ramDash.setLayout(new BoxLayout(ramDash, BoxLayout.Y_AXIS));
        ramDash.setBackground(Color.decode("#B0C4DE"));
        ramDash.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel ramTtlSub = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 3));
        ramTtlSub.setOpaque(false);

        JLabel ramTtl = new JLabel("RAM");
        ramTtl.setFont(new Font("Roboto", Font.BOLD, 15));

        ImageIcon ramImg = new ImageIcon("C:\\Users\\boni\\Desktop\\Files\\MakeDo\\assets\\ram.png");
        Image ramScaledImage = ramImg.getImage().getScaledInstance(23, 23, Image.SCALE_SMOOTH);
        JLabel ram_imgLabel = new JLabel(new ImageIcon(ramScaledImage));

        ramTtlSub.add(ramTtl);
        ramTtlSub.add(ram_imgLabel);
        ramDash.add(ramTtlSub);

        ramCheck.RAMInfo ramInfo = ramCheck.getRAMInfo();
        ramDash.add(Box.createVerticalStrut(5));
        ramDash.add(new JLabel(String.format("Total Memory: %.2f GB", ramInfo.totalMemoryGB)));
        ramDash.add(new JLabel(String.format("Available Memory: %.2f GB", ramInfo.availableMemoryGB)));
        ramDash.add(new JLabel("RAM Slots Used: " + ramInfo.modules.size()));
        ramDash.add(new JLabel("Estimated Vacant Slots: " + ramInfo.estimatedSlots));

        for (int i = 0; i < ramInfo.modules.size(); i++) {
            ramDash.add(Box.createVerticalStrut(5));
            ramCheck.RAMInfo.Module m = ramInfo.modules.get(i);
            ramDash.add(new JLabel("Module " + (i + 1) + ":"));
            ramDash.add(new JLabel("  Bank Label: " + m.bankLabel));
            ramDash.add(new JLabel(String.format("  Capacity: %.2f GB", m.capacityGB)));
            ramDash.add(new JLabel("  Manufacturer: " + m.manufacturer));
            ramDash.add(new JLabel("  Memory Type: " + m.memoryType));
            ramDash.add(new JLabel("  Clock Speed: " + m.clockSpeedMHz));
            ramDash.add(new JLabel("  Transfer Rate: " + m.transferRateMTps));
        }

        // ---------- Battery Panel ----------
        JPanel batteryDash = new RoundedPanel(20);
        batteryDash.setLayout(new BoxLayout(batteryDash, BoxLayout.Y_AXIS));
        batteryDash.setBackground(Color.decode("#B0C4DE"));
        batteryDash.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel batteryTtlSub = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 3));
        batteryTtlSub.setOpaque(false);

        JLabel batteryTtl = new JLabel("Battery");
        batteryTtl.setFont(new Font("Roboto", Font.BOLD, 15));

        ImageIcon batteryImg = new ImageIcon("C:\\Users\\boni\\Desktop\\Files\\MakeDo\\assets\\battery.png");
        Image batteryScaledImage = batteryImg.getImage().getScaledInstance(23, 23, Image.SCALE_SMOOTH);
        JLabel battery_imgLabel = new JLabel(new ImageIcon(batteryScaledImage));

        batteryTtlSub.add(batteryTtl);
        batteryTtlSub.add(battery_imgLabel);
        batteryDash.add(batteryTtlSub);

        List<batteryCheck.BatteryInfo> batteryInfoList = batteryCheck.getBatteryInfo();
        if (!batteryInfoList.isEmpty()) {
            batteryCheck.BatteryInfo batteryInfo = batteryInfoList.get(0);
            batteryDash.add(Box.createVerticalStrut(5));
            batteryDash.add(new JLabel("Name: " + batteryInfo.name));
            batteryDash.add(new JLabel("Voltage: " + batteryInfo.voltage + " V"));
            batteryDash.add(new JLabel("Design Capacity (Wh): " + batteryInfo.designCapacityWh));
            batteryDash.add(new JLabel("Max Capacity (Wh): " + batteryInfo.maxCapacityWh));
            batteryDash.add(new JLabel("Current Capacity (Wh): " + batteryInfo.currentCapacityWh));
        } else {
            batteryDash.add(Box.createVerticalStrut(5));
            batteryDash.add(new JLabel("No battery detected."));
        }

        // ---------- Screen Panel ----------
        JPanel screenDash = new RoundedPanel(20);
        screenDash.setLayout(new BoxLayout(screenDash, BoxLayout.Y_AXIS));
        screenDash.setBackground(Color.decode("#B0C4DE"));
        screenDash.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel screenTtlSub = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 3));
        screenTtlSub.setOpaque(false);

        JLabel screenTtl = new JLabel("Screen");
        screenTtl.setFont(new Font("Roboto", Font.BOLD, 15));

        ImageIcon screenImg = new ImageIcon("C:\\Users\\boni\\Desktop\\Files\\MakeDo\\assets\\screen.png");
        Image screenScaledImage = screenImg.getImage().getScaledInstance(23, 23, Image.SCALE_SMOOTH);
        JLabel screen_imgLabel = new JLabel(new ImageIcon(screenScaledImage));

        screenTtlSub.add(screenTtl);
        screenTtlSub.add(screen_imgLabel);
        screenDash.add(screenTtlSub);

        screenCheck.ScreenInfo[] screens = screenCheck.getAllScreenInfo();
        if (screens.length == 0) {
            screenDash.add(Box.createVerticalStrut(5));
            screenDash.add(new JLabel("No screens detected."));
        } else {
            for (screenCheck.ScreenInfo screen : screens) {
                screenDash.add(Box.createVerticalStrut(5));
                screenDash.add(new JLabel("Display " + screen.displayNumber + ":"));
                screenDash.add(new JLabel("  Resolution: " + screen.width + "x" + screen.height));
                screenDash.add(new JLabel("  Refresh Rate: " + screen.refreshRate + " Hz"));
                screenDash.add(new JLabel("  Bit Depth: " + screen.bitDepth));
                screenDash.add(new JLabel("  Pixel Size: " + screen.pixelSize + " bits"));
                screenDash.add(new JLabel("  Color Space: " + screen.colorSpaceName));
                screenDash.add(new JSeparator(SwingConstants.HORIZONTAL));
            }
        }

        // ---------- OS Panel ----------
        JPanel osDash = new RoundedPanel(20);
        osDash.setLayout(new BoxLayout(osDash, BoxLayout.Y_AXIS));
        osDash.setBackground(Color.decode("#B0C4DE"));
        osDash.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel osTtlSub = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 3));
        osTtlSub.setOpaque(false);

        JLabel osTtl = new JLabel("OS");
        osTtl.setFont(new Font("Roboto", Font.BOLD, 15));

        ImageIcon osImg = new ImageIcon("C:\\Users\\boni\\Desktop\\Files\\MakeDo\\assets\\os.png");
        Image osScaledImage = osImg.getImage().getScaledInstance(23, 23, Image.SCALE_SMOOTH);
        JLabel os_imgLabel = new JLabel(new ImageIcon(osScaledImage));

        osTtlSub.add(osTtl);
        osTtlSub.add(os_imgLabel);
        osDash.add(osTtlSub);

        osCheck.OSInfo osInfo = osCheck.getOSInfo();
        osDash.add(Box.createVerticalStrut(5));
        osDash.add(new JLabel("Name: " + osInfo.name));
        osDash.add(new JLabel("Version: " + osInfo.version));
        osDash.add(new JLabel("Architecture: " + osInfo.architecture));
        osDash.add(new JLabel("Build Number: " + osInfo.buildNumber));


        JPanel gpuDash = new RoundedPanel(20);
        gpuDash.setLayout(new BoxLayout(gpuDash, BoxLayout.Y_AXIS));
        gpuDash.setBackground(Color.decode("#B0C4DE"));
        gpuDash.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel gpuTtlSub = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 3));
        gpuTtlSub.setOpaque(false);

        JLabel gpuTtl = new JLabel("GPU");
        gpuTtl.setFont(new Font("Roboto", Font.BOLD, 15));

        ImageIcon gpuImg = new ImageIcon("C:\\Users\\boni\\Desktop\\Files\\MakeDo\\assets\\gpu.png");
        Image gpuScaledImage = osImg.getImage().getScaledInstance(23, 23, Image.SCALE_SMOOTH);
        JLabel gpu_imgLabel = new JLabel(new ImageIcon(gpuScaledImage));

        gpuTtlSub.add(gpuTtl);
        gpuTtlSub.add(gpu_imgLabel);
        gpuDash.add(gpuTtlSub);
        
        List<gpuCheck.GPUInfo> gpuInfoList = gpuCheck.getGPUInfo();

if (gpuInfoList.isEmpty()) {
    gpuDash.add(new JLabel("No GPU detected."));
} else {
    for (int i = 0; i < gpuInfoList.size(); i++) {
        gpuCheck.GPUInfo gpu = gpuInfoList.get(i);
        gpuDash.add(new JLabel("GPU " + (i + 1) + ":"));
        gpuDash.add(new JLabel("  Name        : " + gpu.name));
        gpuDash.add(new JLabel("  Vendor      : " + gpu.vendor));
        gpuDash.add(new JLabel("  VRAM        : " + (gpu.vramBytes / (1024 * 1024)) + " MB"));
        gpuDash.add(new JLabel("  Device ID   : " + gpu.deviceId));
        gpuDash.add(new JLabel("  Version Info: " + gpu.versionInfo));
        gpuDash.add(new JSeparator(SwingConstants.HORIZONTAL));
    }
}

        JPanel prfDash = new RoundedPanel(20);
        prfDash.setLayout(new BoxLayout(prfDash, BoxLayout.Y_AXIS));
        prfDash.setBackground(Color.decode("#B0C4DE"));
        prfDash.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel prfTtlSub = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 3));
        prfTtlSub.setOpaque(false);

        JLabel prfTtl = new JLabel("Peripherals");
        prfTtl.setFont(new Font("Roboto", Font.BOLD, 15));

        ImageIcon prfImg = new ImageIcon("C:\\Users\\boni\\Desktop\\Files\\MakeDo\\assets\\usb.png");
        Image prfScaledImage = prfImg.getImage().getScaledInstance(23, 23, Image.SCALE_SMOOTH);
        JLabel prf_imgLabel = new JLabel(new ImageIcon(prfScaledImage));

        prfTtlSub.add(prfTtl);
        prfTtlSub.add(prf_imgLabel);
        prfDash.add(prfTtlSub);
        

        peripheralsCheck.PeripheralSummary prfSummary = peripheralsCheck.getPeripheralSummary();

        prfDash.add(Box.createVerticalStrut(5));
        prfDash.add(new JLabel("Estimated Physical USB Ports in Use: " + prfSummary.totalUsbDevices));
        prfDash.add(Box.createVerticalStrut(5));

        for (peripheralsCheck.PeripheralInfo device : prfSummary.devices) {
                String statusText = device.isWorking ? "Working" : "Unknown Status";
                prfDash.add(new JLabel(" - " + device.name + " [" + statusText + "]"));
            }
    

        // ---------- Add to main dashboard ----------
        mainDash.add(hddDash);
        mainDash.add(cpuDash);
        mainDash.add(ramDash);
        mainDash.add(batteryDash);
        mainDash.add(screenDash);
        mainDash.add(osDash);
        mainDash.add(gpuDash);
        mainDash.add(prfDash);

        mainSubPanel.add(mainDash);
        mainDashboardWindow.add(mainSubPanel);
        mainDashboardWindow.setVisible(true);
    }
}

class Main {
    public static void main(String[] args) {
        mainDashboard dashboard = new mainDashboard();
        dashboard.showMainDashboard();
    }
}
