package com.loftschool.helloworld.fragment_balance;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.loftschool.helloworld.R;

public class BalanceView extends View {

    private int expenses = 1;
    private int incomes = 1;

    private final Paint expensePaint = new Paint();
    private final Paint incomePaint = new Paint();

    public BalanceView(Context context) {
        super(context);
        init();
    }

    public BalanceView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BalanceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public BalanceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void update(int expenses, int incomes) {
        this.expenses = expenses;
        this.incomes = incomes;

        invalidate();
    }

    private void init() {
        expensePaint.setColor(ContextCompat.getColor(getContext(), R.color.primary_color));
        incomePaint.setColor(ContextCompat.getColor(getContext(), R.color.accent_color));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int total = expenses + incomes;

        float expenseAngle = 360f * expenses / total;
        float incomesAngle = 360f * incomes / total;

        int space = 4;
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                space,
                getResources().getDisplayMetrics()
        );

        int size = Math.min(getWidth(), getHeight()) - px * 2;
        int xMargin = (getWidth() - size) / 2;
        int yMargin = (getHeight() - size) / 2;

        canvas.drawArc(xMargin - px, yMargin,
                getWidth() - xMargin - px,
                getHeight() - yMargin, 180 - expenseAngle / 2,
                expenseAngle, true, expensePaint);

        canvas.drawArc(xMargin + px, yMargin,
                getWidth() - xMargin + px,
                getHeight() - yMargin, 360 - incomesAngle / 2,
                incomesAngle, true, incomePaint);
    }
}
