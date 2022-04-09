package com.prosper.myflashcardapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
                View answerSideView = findViewById(R.id.answer);
                int cx = answerSideView.getWidth() / 2;
                int cy = answerSideView.getHeight() / 2;
                float finalRadius = (float) Math.hypot(cx, cy);
                Animator anim = ViewAnimationUtils.createCircularReveal(answerSideView, cx, cy, 0f, finalRadius);
                findViewById(R.id.flashcard_question).setVisibility(View.INVISIBLE);
                answerSideView.setVisibility(View.VISIBLE);

                anim.setDuration(3000);
                anim.start();
//                findViewById(R.id.flashcard_question).setVisibility(View.INVISIBLE);
//                findViewById(R.id.answer).setVisibility(View.VISIBLE);
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
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddCardActivity.class);
                startActivityForResult(intent, 100);
                overridePendingTransition(R.anim.right_left, R.anim.left_right);
            }
        });

        flashcardDatabase = new FlashcardDatabase(getApplicationContext());

        allFlashCards = flashcardDatabase.getAllCards();
        if (allFlashCards != null && allFlashCards.size() > 0) {
          //  Flashcard firstCard = allFlashCards.get(0);
            flashcardQuestion.setText(allFlashCards.get(0).getQuestion());
            flashcardAnswer.setText(allFlashCards.get(0).getAnswer());
        }


        findViewById(R.id.flashcard_next_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Animation leftOutAnim = AnimationUtils.loadAnimation(view.getContext(), R.anim.left_right);
                final Animation rightInAnim = AnimationUtils.loadAnimation(view.getContext(), R.anim.right_left);

                if (allFlashCards.size() == 0) {
                    return;
                }

                cardIndex++;

                if(cardIndex >= allFlashCards.size()) {
                    Snackbar.make(view,
                            "You've reached the end of the cards, going back to start.",
                            Snackbar.LENGTH_SHORT)
                            .show();
                    cardIndex = 0;

                }

                leftOutAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        // this method is called when the animation first starts

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        // this method is called when the animation is finished playing
                        findViewById(R.id.flashcard_question).startAnimation(rightInAnim);
                        Flashcard flashcard = allFlashCards.get(cardIndex);
                        flashcardQuestion.setText(flashcard.getQuestion());
                        flashcardAnswer.setText(flashcard.getAnswer());
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        // we don't need to worry about this method
                    }
                });

                findViewById(R.id.flashcard_question).startAnimation(rightInAnim);

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