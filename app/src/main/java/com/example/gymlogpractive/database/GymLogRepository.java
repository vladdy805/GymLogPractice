package com.example.gymlogpractive.database;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.gymlogpractive.database.entities.GymLog;
import com.example.gymlogpractive.MainActivity;
import com.example.gymlogpractive.database.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class GymLogRepository {

    private final GymLogDAO gymLogDAO;
    private final UserDAO userDAO;
    private final ArrayList<GymLog> allLogs;
    private static GymLogRepository repository;

    private GymLogRepository(Application application) {
        GymLogDatabase db = GymLogDatabase.getDatabase(application);
        this.gymLogDAO = db.gymLogDAO();
        this.userDAO = db.userDAO();
        this.allLogs = (ArrayList<GymLog>) this.gymLogDAO.getAllRecords();
    }

    /**
     * Gets all GymLog records.
     * Returns null if an error occurs.
     */
    public static GymLogRepository getRepository(Application application) {
        if (repository != null) {
            return repository;
        }
        Future<GymLogRepository> future = GymLogDatabase.databaseWriteExecutor.submit(
                new Callable<GymLogRepository>() {
                    @Override
                    public GymLogRepository call() throws Exception {
                        return new GymLogRepository(application);
                    }
                }
        );
        try {
            repository = future.get();
            return repository;
        } catch (InterruptedException | ExecutionException e) {
            Log.e(MainActivity.TAG, "Problem getting GymLogRepository, thread error.", e);
        }
        return null;
    }

    public ArrayList<GymLog> getAllLogs() {
        Future<ArrayList<GymLog>> future = GymLogDatabase.databaseWriteExecutor.submit(
                new Callable<ArrayList<GymLog>>() {
                    @Override
                    public ArrayList<GymLog> call() throws Exception {
                        return (ArrayList<GymLog>) gymLogDAO.getAllRecords();
                    }
                });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.e(MainActivity.TAG, "Problem when getting all GymLogs in the repository", e);
        }
        return null;
    }

    /**
     * Inserts a GymLog record.
     */
    public void insertGymLog(GymLog gymLog) {
        GymLogDatabase.databaseWriteExecutor.execute(() -> gymLogDAO.insert(gymLog));
    }
    /**
     * Returns LiveData with the User matching the given username.
     */
    public LiveData<User> getUserByUsername(String username) {
        return userDAO.getUserByUsername(username);
    }
    /**
     * Returns LiveData with the User matching the given user ID.
     */
    public LiveData<User> getUserByUserId(int userId) {
        return userDAO.getUserByUserId(userId);
    }

    public LiveData<List<GymLog>> getAllLogsByUserIdLiveData(int loggedInUserId) {
        return gymLogDAO.getAllRecordsByUserIdLiveData(loggedInUserId);
    }

    @Deprecated
    public ArrayList<GymLog> getAllLogsByUserId(int loggedInUserId) {
        Future<ArrayList<GymLog>> future = GymLogDatabase.databaseWriteExecutor.submit(
                new Callable<ArrayList<GymLog>>() {
                    @Override
                    public ArrayList<GymLog> call() throws Exception {
                        return (ArrayList<GymLog>) gymLogDAO.getAllRecordsByUserId(loggedInUserId);
                    }
                });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.e(MainActivity.TAG, "Problem when getting all GymLogs in the repository", e);
        }
        return null;
    }
}