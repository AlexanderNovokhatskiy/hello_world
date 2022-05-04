package com.loftschool.helloworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AddItemActivity extends AppCompatActivity {
    public static final String KEY_AMOUNT = "amount";
    public static final String KEY_NAME = "name";

    private EditText mExpenseAmountEditText;
    private String mExpenseAmount = "";
    private EditText mExpenseNameEditText;
    private String mExpenseName = "";
    private Button mExpenseAddButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        mExpenseAmountEditText = findViewById(R.id.Amount);
        mExpenseAmountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mExpenseAmount = editable.toString();
                checkButtonEnabled();
            }
        });
        mExpenseNameEditText = findViewById(R.id.Name);
        mExpenseNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mExpenseName = editable.toString();
                checkButtonEnabled();
            }
        });
        mExpenseAddButton = findViewById(R.id.expenseAdd);
        mExpenseAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                if (!mExpenseAmount.isEmpty() && !mExpenseName.isEmpty()) {
                    setResult(
                            RESULT_OK,
                            new Intent().putExtra(KEY_AMOUNT, mExpenseAmount)
                                    .putExtra(KEY_NAME, mExpenseName));
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.empty_expense), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void checkButtonEnabled() {
        mExpenseAddButton.setEnabled(!mExpenseAmount.isEmpty() && !mExpenseName.isEmpty());
    }
}