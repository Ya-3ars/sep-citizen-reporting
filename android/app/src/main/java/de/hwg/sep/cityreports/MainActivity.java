package de.hwg.sep.cityreports;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView status = findViewById(R.id.statusText);
        Button testButton = findViewById(R.id.testButton);
        testButton.setOnClickListener(v -> checkBackend(status));
    }

    /** Hits GET /api/reports on a background thread and reports the HTTP status. */
    private void checkBackend(TextView status) {
        status.setText("Contacting backend…");
        executor.execute(() -> {
            String result;
            try {
                URL url = new URL(ApiConfig.BASE_URL + "/api/reports");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                int code = conn.getResponseCode();
                conn.disconnect();
                result = "Backend responded: HTTP " + code
                        + (code == 501 ? "\n(501 is expected — endpoint is still a stub. "
                        + "The connection works.)" : "");
            } catch (Exception e) {
                result = "Could not reach backend:\n" + e.getMessage()
                        + "\n\nIs the backend running on " + ApiConfig.BASE_URL + "?";
            }
            String finalResult = result;
            runOnUiThread(() -> status.setText(finalResult));
        });
    }
}
