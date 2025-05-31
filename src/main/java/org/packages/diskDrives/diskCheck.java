package org.packages.diskDrives;

import oshi.SystemInfo;
import oshi.hardware.HWDiskStore;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class diskCheck {

    public static class PartitionInfo {
        public String name;
        public double usedGB;
        public double totalGB;
        public double freeGB;

        public PartitionInfo(String name, double usedGB, double totalGB, double freeGB) {
            this.name = name;
            this.usedGB = usedGB;
            this.totalGB = totalGB;
            this.freeGB = freeGB;
        }
    }

    public static class DriveInfo {
        public String name;
        public String model;
        public int health;
        public int badSectors;
        public List<String> issues = new ArrayList<>();
        public List<PartitionInfo> partitions = new ArrayList<>();
    }

    public static List<DriveInfo> scanAllDrives() {
        List<DriveInfo> result = new ArrayList<>();

        SystemInfo si = new SystemInfo();
        OperatingSystem os = si.getOperatingSystem();
        FileSystem fs = os.getFileSystem();
        List<OSFileStore> fileStores = fs.getFileStores();

        Map<String, List<OSFileStore>> driveToPartitions = new LinkedHashMap<>();
        Map<String, String> volumeToPhysicalDrive = new HashMap<>();
        for (var store : si.getHardware().getDiskStores()) {
            for (var partition : store.getPartitions()) {
                String mount = partition.getMountPoint();
                if (mount != null && mount.length() >= 2) {
                    volumeToPhysicalDrive.put(mount.substring(0, 2).toUpperCase(), store.getName());
                }
            }
        }

        for (OSFileStore store : fileStores) {
            String mount = store.getMount();
            if (mount == null || mount.length() < 2) continue;
            String vol = mount.substring(0, 2).toUpperCase();

            String physicalDrive = volumeToPhysicalDrive.getOrDefault(vol, vol);
            driveToPartitions.computeIfAbsent(physicalDrive, k -> new ArrayList<>()).add(store);
        }

        List<String> smartDrives = getAvailableDrives();
        Set<String> detectedDrives = new HashSet<>();

        for (Map.Entry<String, List<OSFileStore>> entry : driveToPartitions.entrySet()) {
            String physicalDrive = entry.getKey();
            List<OSFileStore> partitions = entry.getValue();

            String smartDrive = null;
            for (OSFileStore partition : partitions) {
                String vol = partition.getMount().substring(0, 2).toUpperCase();
                if (smartDrives.contains(vol)) {
                    smartDrive = vol;
                    break;
                }
            }
            if (smartDrive == null) {
                smartDrive = physicalDrive;
            }

            DriveInfo info = getHealthScore(smartDrive);
            detectedDrives.add(smartDrive.toUpperCase());

            info.name = smartDrive;
            info.model = "Unknown";
            info.badSectors = 0;

            for (String s : info.issues) {
                if (s.toLowerCase().contains("model number") || s.toLowerCase().contains("device model")) {
                    info.model = s.substring(s.indexOf(":") + 1).trim();
                }
                if (s.startsWith("Reallocated_Sector_Ct:")) {
                    String[] parts = s.split(":");
                    if (parts.length > 1) {
                        String[] tokens = parts[1].trim().split(" ");
                        try {
                            info.badSectors = Integer.parseInt(tokens[0]);
                        } catch (NumberFormatException ignored) {}
                    }
                }
            }

            for (OSFileStore store : partitions) {
                long total = store.getTotalSpace();
                long usable = store.getUsableSpace();
                long used = total - usable;
                info.partitions.add(new PartitionInfo(
                        store.getName(),
                        used / 1e9,
                        total / 1e9,
                        usable / 1e9
                ));
            }

            result.add(info);
        }

        return result;
    }

    public static List<String> getAvailableDrives() {
        List<String> drives = new ArrayList<>();
        for (char drive = 'C'; drive <= 'Z'; drive++) {
            String path = drive + ":";
            try {
                Process process = new ProcessBuilder("smartctl", "-i", path).start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.toLowerCase().contains("smart support is")) {
                        drives.add(path);
                        break;
                    }
                }
                reader.close();
                process.destroy();
            } catch (Exception ignored) {}
        }
        return drives;
    }

    public static DriveInfo getHealthScore(String drive) {
        DriveInfo info = new DriveInfo();
        info.name = drive;
        int health = 100;
        try {
            String[] attributes = {
                    "Reallocated_Sector_Ct", "Current_Pending_Sector", "Offline_Uncorrectable",
                    "UDMA_CRC_Error_Count", "Reported_Uncorrect", "Spin_Retry_Count", "Command_Timeout"
            };

            Process process = new ProcessBuilder("smartctl", "-A", drive).start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            boolean criticalFailure = false;

            while ((line = reader.readLine()) != null && !criticalFailure) {
                for (String attr : attributes) {
                    if (line.contains(attr)) {
                        String[] parts = line.trim().split("\\s+");
                        int rawValue;
                        try {
                            rawValue = Integer.parseInt(parts[parts.length - 1]);
                        } catch (NumberFormatException e) {
                            continue;
                        }

                        int penalty = 0;
                        switch (attr) {
                            case "Reallocated_Sector_Ct":
                                if (rawValue > 10000) penalty = 100;
                                else if (rawValue > 1000) penalty = 50;
                                else if (rawValue > 0) penalty = Math.min(3, rawValue / 500);
                                break;
                            case "Current_Pending_Sector":
                            case "Offline_Uncorrectable":
                                if (rawValue > 50) penalty = 20;
                                else if (rawValue > 0) penalty = 5;
                                break;
                            case "UDMA_CRC_Error_Count":
                                if (rawValue > 5000) penalty = 5;
                                else if (rawValue > 2000) penalty = 2;
                                break;
                            case "Reported_Uncorrect":
                                if (rawValue > 1000) penalty = 5;
                                else if (rawValue > 100) penalty = 2;
                                break;
                            case "Command_Timeout":
                                if (rawValue > 2000) penalty = 5;
                                else if (rawValue > 1000) penalty = 2;
                                else if (rawValue > 300) penalty = 1;
                                break;
                            case "Spin_Retry_Count":
                                if (rawValue > 100) penalty = 5;
                                else if (rawValue > 10) penalty = 2;
                                break;
                        }

                        if (penalty >= 100) {
                            health = 0;
                            info.issues.add(attr + ": " + rawValue + " (Critical failure - drive likely unusable)");
                            criticalFailure = true;
                            break;
                        } else if (penalty > 0) {
                            health -= penalty;
                            info.issues.add(attr + ": " + rawValue + " (Penalty: -" + penalty + ")");
                        } else if (rawValue > 0) {
                            info.issues.add(attr + ": " + rawValue + " (No penalty)");
                        }
                    }
                }
            }
            reader.close();
            process.destroy();

            if (!criticalFailure) health = Math.max(0, Math.min(health, 100));

            process = new ProcessBuilder("smartctl", "-i", drive).start();
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((line = reader.readLine()) != null) {
                if (line.contains("Serial Number")) {
                    info.issues.add(line.trim());
                }
                if (line.contains("Device Model") || line.contains("Model Number")) {
                    info.issues.add(line.trim());
                }
            }
            reader.close();
            process.destroy();

        } catch (Exception e) {
            info.issues.add("Failed to read SMART data: " + e.getMessage());
            info.health = -1;
            return info;
        }

        info.health = health;
        return info;
    }

    public static void main(String[] args) {
        List<DriveInfo> drives = scanAllDrives();
        for (DriveInfo drive : drives) {
            System.out.println("Drive: " + drive.name);
            System.out.println("Model: " + drive.model);
            System.out.println("Bad Sectors: " + drive.badSectors);
            System.out.println("Health: " + drive.health + "/100");
            for (String issue : drive.issues) {
                System.out.println("  " + issue);
            }
            for (PartitionInfo p : drive.partitions) {
                System.out.printf("  %s: %.2f GB used / %.2f GB total (%.2f GB free)\n",
                        p.name, p.usedGB, p.totalGB, p.freeGB);
            }
            System.out.println("--------------------------------------------------");
        }
    }
}
