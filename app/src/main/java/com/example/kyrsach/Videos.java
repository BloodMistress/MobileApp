package com.example.kyrsach;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Videos extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> postList = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        postAdapter = new PostAdapter(postList);
        recyclerView.setAdapter(postAdapter);
        db = FirebaseFirestore.getInstance();

        // Добавляем слушатель для обновления данных в реальном времени из коллекции "public_posts"
        db.collection("public_posts")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e("Firestore Error", e.getMessage());
                            return;
                        }

                        postList.clear();
                        for (DocumentSnapshot snapshot : value.getDocuments()) {
                            Post post = snapshot.toObject(Post.class);
                            post.setId(snapshot.getId()); // Сохраняем ID документа для возможного удаления
                            postList.add(post);
                        }
                        postAdapter.notifyDataSetChanged(); // Уведомляем адаптер об изменении данных
                    }
                });

        // Обработка нажатия на кнопку для создания нового поста
        FloatingActionButton fabAddPost = findViewById(R.id.fab_add_post);
        fabAddPost.setOnClickListener(v -> {
            CreatePostDialog dialog = new CreatePostDialog();
            dialog.show(getSupportFragmentManager(), "CreatePostDialog");
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_calendar:
                    startActivity(new Intent(Videos.this, Calendar.class));
                    return true;
                case R.id.navigation_timer:
                    startActivity(new Intent(Videos.this, Timer.class));
                    return true;
                case R.id.navigation_videos:
                    return true;
                case R.id.navigation_account:
                    startActivity(new Intent(Videos.this, AccountActivity.class));
                    return true;

            }
            return false;
        });
        bottomNavigationView.setSelectedItemId(R.id.navigation_videos);
    }
}


