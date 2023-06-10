package com.example.sungansungan12;
//조정동 담당
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import android.widget.Toast;

public class NaviActivity extends AppCompatActivity {
    private EditText searchEditText;
    private Button searchButton;
    private ImageButton homeButton;
    private ListView postListView;
    private TextView emptyTextView;

    private DatabaseReference databaseReference;
    private ArrayAdapter<String> adapter;
    private List<String> postList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navi);

        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        homeButton = findViewById(R.id.homeButton);
        postListView = findViewById(R.id.postListView);
        emptyTextView = findViewById(R.id.emptyTextView);

        postList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, postList);
        postListView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("posts");

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchPosts();
            }
        });

        postListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedPost = postList.get(position);
                Intent postShowIntent = new Intent(NaviActivity.this, PostShowActivity.class);
                postShowIntent.putExtra("selectedPost", selectedPost);
                startActivity(postShowIntent);

                // 선택한 게시물의 정보를 가져와서 PostDetailActivity로 전달합니다.
                DatabaseReference selectedPostRef = databaseReference.child(selectedPost);
                selectedPostRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // 게시물 정보를 가져옵니다.
                            String postId = dataSnapshot.getKey();  // 게시물 ID
                            String postTitle = dataSnapshot.child("name").getValue(String.class);  // 게시물 제목
                            String postContent = dataSnapshot.child("content").getValue(String.class);  // 게시물 내용

                            // PostDetailActivity로 전달할 인텐트를 생성합니다.
                            Intent postDetailIntent = new Intent(NaviActivity.this, PostDetailActivity.class);
                            postDetailIntent.putExtra("postId", postId);
                            postDetailIntent.putExtra("postTitle", postTitle);
                            postDetailIntent.putExtra("postContent", postContent);
                            startActivity(postDetailIntent);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // 데이터 로드 실패 시 처리할 내용 작성
                    }
                });
            }
        });




        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToHome();
            }
        });

        loadPosts();
    }

    private void loadPosts() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String postTitle = postSnapshot.child("name").getValue(String.class);
                    postList.add(postTitle);
                }
                adapter.notifyDataSetChanged();

                if (postList.isEmpty()) {
                    emptyTextView.setVisibility(View.VISIBLE);
                    postListView.setVisibility(View.GONE);
                } else {
                    emptyTextView.setVisibility(View.GONE);
                    postListView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // 데이터 로드 실패 시 처리할 내용 작성
            }
        });
    }

    private void searchPosts() {
        String searchTerm = searchEditText.getText().toString().trim();

        if (TextUtils.isEmpty(searchTerm)) {
            loadPosts();
        } else {
            databaseReference.orderByChild("name").startAt(searchTerm).endAt(searchTerm + "\uf8ff")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            postList.clear();
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                String postTitle = postSnapshot.child("name").getValue(String.class);
                                postList.add(postTitle);
                            }
                            adapter.notifyDataSetChanged();

                            if (postList.isEmpty()) {
                                emptyTextView.setVisibility(View.VISIBLE);
                                postListView.setVisibility(View.GONE);
                            } else {
                                emptyTextView.setVisibility(View.GONE);
                                postListView.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // 검색 실패 시 처리할 내용 작성
                        }
                    });
        }
    }

    private void navigateToHome() {
        // 홈으로 이동하는 로직을 작성하세요.
        Intent homeIntent = new Intent(this, HomeActivity.class);
        startActivity(homeIntent);
        finish();
    }
}
