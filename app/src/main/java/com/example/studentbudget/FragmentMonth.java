package com.example.studentbudget;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMonth extends Fragment {

    View view;
    MySharedPreferences sp;
    float budget;
    float spending;
    float spendingPercentage;
    float targetSavings;
    float savingsPercentage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_month, container, false);
        operations();
        return view;
    }

    private void operations() {
        sp = new MySharedPreferences(getActivity(), MySharedPreferences.PREFERENCE_MONTH_KEY);
        startOperations();
        editBudgetClickEvent();
    }

    private void startOperations() {
        getWeekData();
        setBudgetProgress();
        setSavingsProgress();
    }

    private void getWeekData() {
        budget = sp.readPreferenceData(MySharedPreferences.KEY_BUDGET);

        spending = sp.readPreferenceData(MySharedPreferences.KEY_SPENDING);
        spendingPercentage = (spending * 100) / budget;

        targetSavings = sp.readPreferenceData(MySharedPreferences.KEY_TARGET_SAVINGS);
        savingsPercentage = (targetSavings * 100) / budget;
    }

    private void setBudgetProgress() {
        int budgetProgress = Math.round(spending / budget);
        ProgressBar budgetProgressBar = view.findViewById(R.id.monthBudgetPBar);
        TextView monthBudgetInfoTextView = view.findViewById(R.id.monthBudgetInfoTextView);
        monthBudgetInfoTextView.setText(spendingPercentage + "%");
        budgetProgressBar.setProgress(budgetProgress);
    }

    private void setSavingsProgress() {
        ProgressBar savingsProgressbar = view.findViewById(R.id.monthSavingsPBar);
        TextView monthSavingsInfoTextView = view.findViewById(R.id.monthSavingsInfoTextView);
        float resultantSavings = targetSavings;

        monthSavingsInfoTextView.setText(savingsPercentage + "%");
        if (spending + targetSavings > budget) {
            float overflow = spending + targetSavings - budget;
            resultantSavings -= overflow;

            if  (100 - spendingPercentage > 0)
                monthSavingsInfoTextView.setText((100 - spendingPercentage) + "%");
            else
                monthSavingsInfoTextView.setText("0%");
        }

        int finalSavings = Math.round(resultantSavings);
        savingsProgressbar.setProgress(finalSavings);
        savingsProgressbar.setRotation(270 - (finalSavings*360/100));
    }

    private void editBudgetClickEvent() {
        Button weekEditBudgetButton = view.findViewById(R.id.monthEditBudgetButton);
        weekEditBudgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditBudgetActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        startOperations();
    }
}