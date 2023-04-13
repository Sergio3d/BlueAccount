package com.cumn.blueaccount.models;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class TransacRepository {    
    private ITransacDAO iItemDAO;
    private LiveData<List<TransacEntity>> ldList;

    /**
     * Constructor
     *
     * @param application app
     */
    public TransacRepository(Application application) {
        TransacRoomDatabase db = TransacRoomDatabase.getDatabase(application);
        iItemDAO = db.grupoDAO();
        ldList = iItemDAO.getAll();
    }

    public LiveData<List<TransacEntity>> getAll() {
        return ldList;
    }

    public long insert(TransacEntity item) {
        return iItemDAO.insert(item);
    }

    public void deleteAll() {
        iItemDAO.deleteAll();
    }

    public void delete(TransacEntity item)  {
        iItemDAO.delete(item);
    }
}
