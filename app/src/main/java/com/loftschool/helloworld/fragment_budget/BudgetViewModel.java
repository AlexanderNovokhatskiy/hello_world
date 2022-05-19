package com.loftschool.helloworld.fragment_budget;

import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.loftschool.helloworld.LoftApp;
import com.loftschool.helloworld.fragment_budget.models.MoneyItem;
import com.loftschool.helloworld.remote.MoneyApi;
import com.loftschool.helloworld.remote.MoneyRemoteItem;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class BudgetViewModel extends ViewModel {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final MutableLiveData<List<MoneyItem>> _moneyItemsList = new MutableLiveData<>();
    public LiveData<List<MoneyItem>> moneyItemsList = _moneyItemsList;

    private final MutableLiveData<String> _messageString = new MutableLiveData<>("");
    public LiveData<String> messageString = _messageString;

    private final MutableLiveData<Integer> _messageInt = new MutableLiveData<>(-1);
    public LiveData<Integer> messageInt = _messageInt;

    private final MutableLiveData<Boolean> _isRefreshing = new MutableLiveData<>(false);
    public LiveData<Boolean> isRefreshing = _isRefreshing;

    private final MutableLiveData<Boolean> _isEditMode = new MutableLiveData<>(false);
    public LiveData<Boolean> isEditMode = _isEditMode;

    private final MutableLiveData<Integer> _selectedCounter = new MutableLiveData<>(-1);
    public LiveData<Integer> selectedCounter = _selectedCounter;

    private final MutableLiveData<Boolean> _removeItemDoneSuccess = new MutableLiveData<>(false);
    public LiveData<Boolean> removeItemDoneSuccess = _removeItemDoneSuccess;

    @Override
    protected void onCleared() {
        compositeDisposable.dispose();
        super.onCleared();
    }

    public void loadIncomes(MoneyApi moneyApi, SharedPreferences sharedPreferences, String type) {
        String authToken = sharedPreferences.getString(LoftApp.AUTH_KEY, "");

        compositeDisposable.add(moneyApi.getItems(type, authToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(moneyRemoteItems -> {
                    List<MoneyItem> moneyItems = new ArrayList<>();

                    for (MoneyRemoteItem moneyRemoteItem : moneyRemoteItems) {
                        moneyItems.add(MoneyItem.getInstance(moneyRemoteItem));
                    }

                    _moneyItemsList.postValue(moneyItems);
                    _isRefreshing.postValue(false);
                }, throwable -> {
                    _messageString.postValue(throwable.getLocalizedMessage());
                    _isRefreshing.postValue(false);
                }));
    }


    public void removeItem(MoneyApi moneyApi, SharedPreferences sharedPreferences, List<MoneyItem> moneyItemList) {
        String authToken = sharedPreferences.getString(LoftApp.AUTH_KEY, "");
        for (MoneyItem moneyItem : moneyItemList) {
            if (moneyItem.isSelected()) {
                compositeDisposable.add(
                        moneyApi.remove(moneyItem.getId(), authToken)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        () -> {
                                            _removeItemDoneSuccess.postValue(true);
                                        },
                                        error -> {
                                            _removeItemDoneSuccess.postValue(false);
                                            _messageString.postValue(error.getLocalizedMessage());
                                        }
                                )
                );
            }
        }


    }

    public void setEditMode(boolean isEditMode) {
        _isEditMode.postValue(isEditMode);
    }

    public void setSelectedItemsCount(int selectedItemsCount) {
        _selectedCounter.postValue(selectedItemsCount);
    }

    public void resetSelectedCounter() {
        _selectedCounter.postValue(-1);
    }

}