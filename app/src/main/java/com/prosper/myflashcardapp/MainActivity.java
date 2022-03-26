package com.prosper.myflashcardapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.EditText;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView flashcardQuestion;
    TextView flashcardAnswer;

    FlashcardDatabase flashcardDatabase;
    List<Flashcard> allFlashCards;
    int cardIndex = 0;
    ImageView nextQuestion;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        flashcardAnswer = findViewById(R.id.answer);
        flashcardQuestion = findViewById(R.id.flashcard_question);
        nextQuestion = findViewById(R.id.flashcard_next_button);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.flashcard_question).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.flashcard_question).setVisibility(View.INVISIBLE);
                findViewById(R.id.answer).setVisibility(View.VISIBLE);
            }
        });

        findViewById(R.id.answer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.answer).setVisibility(View.INVISIBLE);
                findViewById(R.id.flashcard_question).setVisibility(View.VISIBLE);
            }
        });

        ImageView addQuestionImageView = findViewById(R.id.flashcard_add_button);
        addQuestionImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddCardActivity.class);
                startActivityForResult(intent, 100);
            }
        });

        flashcardDatabase = new FlashcardDatabase(getApplicationContext());

        allFlashCards = flashcardDatabase.getAllCards();
//
        if (allFlashCards != null && allFlashCards.size() > 0) {
            Flashcard firstCard = allFlashCards.get(0);
            flashcardQuestion.setText(firstCard.getQuestion());
            flashcardAnswer.setText(firstCard.getAnswer());
        }


        nextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allFlashCards.size() == 0 || allFlashCards == null) {
                    return;
                }

                cardIndex++;

                if(cardIndex >= allFlashCards.size()) {
                    Snackbar.make(v,
                            "You've reached the end of the cards, going back to start.",
                            Snackbar.LENGTH_SHORT)
                            .show();
                    cardIndex = 0;

                    Flashcard flashcard = allFlashCards.get(cardIndex);
                    flashcardQuestion.setText(flashcard.getAnswer());
                    flashcardAnswer.setText(flashcard.getQuestion());
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        flashcardAnswer = findViewById(R.id.answer);
        flashcardQuestion = findViewById(R.id.flashcard_question);

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100){
            // get data
            if(data != null){
                String question = data.getExtras().getString("question_key");
                String answer = data.getExtras().getString("answer_key");
                flashcardQuestion.setText(question);
                flashcardAnswer.setText(answer);

                Flashcard flashcard = new Flashcard(question, answer);
                flashcardDatabase.insertCard(flashcard);
                allFlashCards = flashcardDatabase.getAllCards();
            }
        }
    }
}