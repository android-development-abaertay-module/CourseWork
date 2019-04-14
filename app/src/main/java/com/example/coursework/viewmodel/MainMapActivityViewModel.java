package com.example.coursework.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;

import com.example.coursework.model.Session;
import com.example.coursework.model.User;
import com.example.coursework.model.data.DaoRepository;
import com.example.coursework.model.helper.MapMediator;
import com.example.coursework.model.helper.PlaceInfoHoulder;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.List;

public class MainMapActivityViewModel extends AndroidViewModel {

    private DaoRepository daoRepository;
    private MutableLiveData<User> userLD;
    private LiveData<List<Session>> recentSessionsLD;
    private MutableLiveData<GoogleMap> mapLD;
    private MutableLiveData<PlacesClient> placesClientLD;
    private MediatorLiveData<MapMediator> mediator;
    private MediatorLiveData<PlaceInfoHoulder> customPlaceLD;
    private MediatorLiveData<Boolean> isInitCameraMoveComplete;
    private MutableLiveData<Integer> mapTypeLD;
    private MutableLiveData<Boolean> isPollyVisibleLD;

    public MutableLiveData<Boolean> isPollyVisible() {
        return isPollyVisibleLD;
    }

    public void setIsPollyVisibleLD(boolean isVisible) {
        this.isPollyVisibleLD.setValue(isVisible);
    }

    public MutableLiveData<Integer> getMapTypeLD() {
        return mapTypeLD;
    }

    public void setMapTypeLD(int mapType) {
        this.mapTypeLD.setValue(mapType);
    }

    public void setUserLD(User user) {
        userLD.setValue(user);
    }
    public MutableLiveData<User> getUserLD(){
        return userLD;
    }

    public LiveData<List<Session>> getRecentSessionsLD() {
        return recentSessionsLD;
    }

    public void setMapLD(GoogleMap map) {
        mapLD.setValue(map);
    }

    public void setPlacesClientLD(PlacesClient placesClientMutable) {
        placesClientLD.setValue(placesClientMutable);
    }

    public void setCustomPlaceLD(PlaceInfoHoulder customPlace) {
        this.customPlaceLD.setValue(customPlace);
    }

    public void setIsInitCameraMoveComplete(Boolean isInitCameraMoveComplete) {
        this.isInitCameraMoveComplete.setValue(isInitCameraMoveComplete);
    }

    public MediatorLiveData<Boolean> getIsInitCameraMoveComplete() {
        return isInitCameraMoveComplete;
    }

    public MediatorLiveData<MapMediator> getMediator(){
        return mediator;
    }
    public MainMapActivityViewModel(@NonNull Application application) {
        super(application);
        daoRepository = new DaoRepository(application);
        mapTypeLD = new MutableLiveData<>();
        mapTypeLD.setValue(1);
        isPollyVisibleLD = new MutableLiveData<>();
        isPollyVisibleLD.setValue(false);
        userLD = new MutableLiveData<>();
        mapLD = new MutableLiveData<>();
        placesClientLD = new MutableLiveData<>();
        customPlaceLD = new MediatorLiveData<>();
        isInitCameraMoveComplete = new MediatorLiveData<>();
        isInitCameraMoveComplete.setValue(false);

        recentSessionsLD = Transformations.switchMap(userLD, (User user) -> {
            return daoRepository.getRecentSessionsWithLocationForUser(10, user.getId());
        });

        mediator = new MediatorLiveData<>();
        mediator.setValue(new MapMediator());
        mediator.addSource(recentSessionsLD, sessions -> {
            MapMediator med = mediator.getValue();
            med.setRecentSessions(sessions);
            mediator.setValue(med);
        });
        mediator.addSource(mapLD, map ->{
            MapMediator med = mediator.getValue();
            med.setMap(map);
            mediator.setValue(med);
        });
        mediator.addSource(placesClientLD,placesClient -> {
            MapMediator med = mediator.getValue();
            med.setPlacesClient(placesClient);
            mediator.setValue(med);
        });
        mediator.addSource(customPlaceLD, customPlace ->{
            MapMediator med = mediator.getValue();
            med.setCustomPlace(customPlace);
            mediator.setValue(med);
        });
    }
}
