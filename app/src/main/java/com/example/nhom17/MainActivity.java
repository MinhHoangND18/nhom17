package com.example.nhom17;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText searchBar;
    private ListView songListView;
    private Button recentSongsButton;  // Button chuyển đến màn hình bài hát gần đây

    // Danh sách các bài hát mẫu với tên bài hát và ca sĩ
    private List<String> allSongs;
    private ArrayAdapter<String> adapter;
    private List<String> filteredSongs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ các view
        searchBar = findViewById(R.id.searchBar);
        songListView = findViewById(R.id.songListView);
        recentSongsButton = findViewById(R.id.recentSongsButton);  // Ánh xạ button "Bài hát gần đây"

        // Danh sách bài hát mẫu (tên bài hát và ca sĩ)
        allSongs = new ArrayList<>();
        allSongs.add("Shape of You - Ed Sheeran");
        allSongs.add("Blinding Lights - The Weeknd");
        allSongs.add("Havana - Camila Cabello");
        allSongs.add("Faded - Alan Walker");
        allSongs.add("Someone Like You - Adele");
        allSongs.add("Rolling in the Deep - Adele");

        // Khởi tạo danh sách lọc bài hát
        filteredSongs = new ArrayList<>(allSongs);

        // Tạo ArrayAdapter để hiển thị danh sách bài hát
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, filteredSongs);
        songListView.setAdapter(adapter);

        // Sự kiện cho thanh tìm kiếm
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable editable) {
                // Lọc bài hát khi người dùng nhập từ khóa tìm kiếm
                String query = editable.toString().toLowerCase();

                // Lọc danh sách bài hát theo từ khóa (tên bài hát hoặc tên ca sĩ)
                filterSongs(query);
            }
        });

        // Sự kiện khi người dùng chọn bài hát
        songListView.setOnItemClickListener((parent, view, position, id) -> {
            String songName = filteredSongs.get(position);
            addSongToHistory(songName);  // Lưu bài hát vào lịch sử
            Toast.makeText(MainActivity.this, "Đã lưu bài hát: " + songName, Toast.LENGTH_SHORT).show();
        });

        // Sự kiện cho nút "Bài hát gần đây"
        recentSongsButton.setOnClickListener(v -> {
            // Mở HistoryActivity để hiển thị bài hát gần đây
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });
    }

    // Hàm lọc danh sách bài hát theo tên bài hát hoặc tên ca sĩ
    private void filterSongs(String query) {
        filteredSongs.clear();
        for (String song : allSongs) {
            // Kiểm tra xem tên bài hát hoặc ca sĩ có chứa từ khóa tìm kiếm hay không
            if (song.toLowerCase().contains(query)) {
                filteredSongs.add(song);
            }
        }
        // Cập nhật ListView với danh sách đã lọc
        adapter.notifyDataSetChanged();
    }

    // Hàm lưu bài hát vào SharedPreferences
    private void addSongToHistory(String songName) {
        SharedPreferences sharedPreferences = getSharedPreferences("History", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Lấy thời gian hiện tại
        long currentTime = System.currentTimeMillis();

        // Lưu bài hát và thời gian vào SharedPreferences
        editor.putLong(songName, currentTime);
        editor.apply();
    }
}
