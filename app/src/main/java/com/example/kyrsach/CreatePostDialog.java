package com.example.kyrsach;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
public class CreatePostDialog extends DialogFragment {

    private EditText titleInput;
    private EditText contentInput;
    private Button createPostButton;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_create_post, container, false);

        titleInput = view.findViewById(R.id.post_title_input);
        contentInput = view.findViewById(R.id.post_content_input);
        createPostButton = view.findViewById(R.id.create_post_button);
        db = FirebaseFirestore.getInstance();

        createPostButton.setOnClickListener(v -> {
            String title = titleInput.getText().toString();
            String content = contentInput.getText().toString();

            if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
                Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                // Сохранение поста в Firestore в общем разделе "public_posts"
                Map<String, Object> post = new HashMap<>();
                post.put("title", title);
                post.put("content", content);

                db.collection("public_posts").add(post)
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(getActivity(), "Post created", Toast.LENGTH_SHORT).show();
                            dismiss(); // Закрываем диалог после успешного создания поста
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(getActivity(), "Failed to create post", Toast.LENGTH_SHORT).show();
                        });
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Изменение размера окна диалога
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}
