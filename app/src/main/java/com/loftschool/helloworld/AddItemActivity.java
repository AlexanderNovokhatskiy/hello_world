package com.loftschool.helloworld;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.loftschool.helloworld.fragment_budget.BudgetFragment;
import com.loftschool.helloworld.remote.MoneyApi;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AddItemActivity extends AppCompatActivity {
    public static final String KEY_AMOUNT = "amount";
    public static final String KEY_NAME = "name";

    private TextInputEditText mExpenseAmountEditText;
    private String mExpenseAmount = "";
    private TextInputEditText mExpenseNameEditText;
    private String mExpenseName = "";
    private Button mExpenseAddButton;
    private MoneyApi moneyApi;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

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

        // Находим тулбар (верхняя плашка на экране)
        Toolbar toolbar = findViewById(R.id.toolbar);
        // При нажатии на кнопочку "назад" вернёмся на предыдущий экран
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        moneyApi = ((LoftApp) getApplication()).moneyApi;

        mExpenseAddButton = findViewById(R.id.expenseAdd);
        mExpenseAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // Получаем заполненные поля
                String name = mExpenseNameEditText.getText().toString();
                int price = Integer.parseInt(mExpenseAmountEditText.getText().toString());
                Bundle arguments = getIntent().getExtras();
                String type = arguments.getString(BudgetFragment.TYPE);
                String token = getSharedPreferences(getString(R.string.app_name), 0).getString(LoftApp.AUTH_KEY, "");
                Disposable disposable = moneyApi.addItem(price, name, type, token)
                        // Подписываем функцию на новый поток
                        .subscribeOn(Schedulers.io())
                        // Указываем на каком потоке будем получать данные из функции
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> finish(),
                                error -> Toast.makeText(getApplicationContext(), R.string.something_went_wrong, Toast.LENGTH_SHORT)
                                        .show()
                        );
                compositeDisposable.add(disposable);
                // Закрываем нашу активити, здесь мы всё сделали
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }


    public void checkButtonEnabled() {
        mExpenseAddButton.setEnabled(!mExpenseAmount.isEmpty() && !mExpenseName.isEmpty());
    }
}