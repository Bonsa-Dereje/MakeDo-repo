package org.packages.OS;

public class osCheck {

    public static class OSInfo {
        public String name;
        public String version;
        public String architecture;
        public String buildNumber;

        public OSInfo(String name, String version, String architecture, String buildNumber) {
            this.name = name;
            this.version = version;
            this.architecture = architecture;
            this.buildNumber = buildNumber;
        }
    }

    public static OSInfo getOSInfo() {
        String name = System.getProperty("os.name");
        String version = System.getProperty("os.version");
        String architecture = System.getProperty("os.arch");
        String buildNumber = getBuildNumber();

        return new OSInfo(name, version, architecture, buildNumber);
    }

    private static String getBuildNumber() {
        String osName = System.getProperty("os.name").toLowerCase();
        String build = "";

        try {
            if (osName.contains("win")) {
                build = System.getenv("OS"); // fallback if version/build isn't separately exposed
            } else if (osName.contains("mac")) {
                Process process = Runtime.getRuntime().exec("sw_vers -buildVersion");
                java.io.BufferedReader reader = new java.io.BufferedReader(
                    new java.io.InputStreamReader(process.getInputStream()));
                build = reader.readLine();
                reader.close();
            } else if (osName.contains("nix") || osName.contains("nux")) {
                Process process = Runtime.getRuntime().exec("uname -r");
                java.io.BufferedReader reader = new java.io.BufferedReader(
                    new java.io.InputStreamReader(process.getInputStream()));
                build = reader.readLine();
                reader.close();
            }
        } catch (Exception e) {
            build = "Unknown";
        }

        return build != null ? build : "Unknown";
    }
}
