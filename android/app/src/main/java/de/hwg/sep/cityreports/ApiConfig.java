package de.hwg.sep.cityreports;

/** Central place for backend connection settings. */
public final class ApiConfig {

    // Dev setup uses `adb reverse tcp:8080 tcp:8080`, which tunnels the emulator's
    // own localhost:8080 to the host Mac's localhost:8080. Re-run that adb command
    // after each emulator cold boot (the tunnel does not survive a restart).
    // Alternative without adb: use "http://10.0.2.2:8080" (the emulator's host alias).
    // On a physical device: use the Mac's LAN IP, e.g. "http://192.168.x.x:8080".
    public static final String BASE_URL = "http://127.0.0.1:8080";

    private ApiConfig() {}
}
