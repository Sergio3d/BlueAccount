package com.cumn.blueaccount.models;
import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TransacViewModel extends AndroidViewModel {

    private TransacRepository mRepository;

    private LiveData<List<TransacEntity>> ldList;

    /**
     * Constructor
     *
     * @param application app
     */
    public TransacViewModel(Application application) {
        super(application);
        mRepository = new TransacRepository(application);
        ldList = mRepository.getAll();
    }

    /**
     * Obtiene todos los grupos
     *
     * @return lista de grupos
     */
    public LiveData<List<TransacEntity>> getAll() {
        return ldList;
    }

    public void insert(TransacEntity item) {
        mRepository.insert(item);
    }

    public void deleteAll() {
        mRepository.deleteAll();
    }

    public void delete(TransacEntity item) {
        mRepository.delete(item);
    }


}