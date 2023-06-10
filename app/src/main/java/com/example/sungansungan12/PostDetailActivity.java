package com.example.sungansungan12;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//조덩동 담당
public class PostDetailActivity extends AppCompatActivity {
    private TextView titleTextView;
    private TextView descriptionTextView;
    private TextView priceTextView;
    private TextView availableTextView;
    private ImageButton homeButton;
    private ImageView productImageView;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        titleTextView = findViewById(R.id.titleTextView);
        descriptionTextView = findViewById(R.id.contentTextView);
        productImageView = findViewById(R.id.photoImageView);

        // 인텐트에서 게시글 ID 가져오기
        Intent intent = getIntent();
        String postId = intent.getStringExtra("postId");

        // Firebase Realtime Database의 "posts" 노드에 대한 DatabaseReference 가져오기
        databaseReference = FirebaseDatabase.getInstance().getReference("posts").child(postId);

        // 게시글 정보를 가져와서 화면에 표시
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Post post = dataSnapshot.getValue(Post.class);
                    if (post != null) {
                        titleTextView.setText(post.getName());
                        descriptionTextView.setText(post.getDescription());

                        // 이미지를 Glide를 사용하여 표시
                        Glide.with(PostDetailActivity.this)
                                .load(post.getImageUrl())
                                .into(productImageView);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // 데이터 로드 실패 시 처리할 내용 작성
            }
        });

        // 홈 버튼 클릭 시 홈 화면으로 이동
        ImageButton homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(v -> {
            Intent homeIntent = new Intent(PostDetailActivity.this, HomeActivity.class);
            startActivity(homeIntent);
            finish(); // 현재 액티비티 종료
        });
    }
}