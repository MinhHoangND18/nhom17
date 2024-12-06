package com.example.nhom17;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class HistoryActivity extends AppCompatActivity {

    private ListView recentSongsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_activity);

        recentSongsListView = findViewById(R.id.recentSongsListView);

        // Lấy danh sách các bài hát đã nghe
        List<String> recentSongs = getRecentSongs();

        // Hiển thị bài hát trong ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, recentSongs);
        recentSongsListView.setAdapter(adapter);
    }

    // Lấy danh sách các bài hát đã nghe từ SharedPreferences
    private List<String> getRecentSongs() {
        SharedPreferences sharedPreferences = getSharedPreferences("History", MODE_PRIVATE);
        Map<String, ?> allSongs = sharedPreferences.getAll(); // Lấy tất cả bài hát

        // Tạo danh sách chứa tên bài hát và thời gian nghe
        List<String> recentSongs = new ArrayList<>();
        for (Map.Entry<String, ?> entry : allSongs.entrySet()) {
            String songName = entry.getKey();
            long timestamp = (long) entry.getValue();

            // Thêm tên bài hát và thời gian vào danh sách
            recentSongs.add(songName + " (Last played: " + timestamp + ")");
        }

        // Sắp xếp danh sách theo thời gian nghe (mới nhất lên đầu)
        Collections.sort(recentSongs, (o1, o2) -> {
            // Chuyển đổi string thành timestamp (chỉ cần ví dụ đơn giản)
            long time1 = Long.parseLong(o1.split(": ")[1]);
            long time2 = Long.parseLong(o2.split(": ")[1]);
            return Long.compare(time2, time1);
        });

        return recentSongs;
    }
}
