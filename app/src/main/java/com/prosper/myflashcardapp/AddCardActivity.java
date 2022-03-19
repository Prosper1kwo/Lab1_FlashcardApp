package com.prosper.myflashcardapp;
import android.content.Intent;
import android.widget.EditText;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class AddCardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addcard_layout);


        findViewById(R.id.flashcard_cancel_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.flashcard_save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                String inputQuestion = ((EditText) findViewById(R.id.flashcard_question_editText)).getText().toString();
                String inputAnswer = ((EditText) findViewById(R.id.flashcard_answer_editText)).getText().toString();
                data.putExtra("question_key", inputQuestion);
                data.putExtra("answer_key", inputAnswer);
                setResult(RESULT_OK, data);
                finish();
            }
        });
    }

}
