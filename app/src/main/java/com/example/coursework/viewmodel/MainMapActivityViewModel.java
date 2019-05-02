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
import com.example.coursework.model.helper.PlaceInfoHolder;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.List;

public class MainMapActivityViewModel extends AndroidViewModel {

    private DaoRepository daoRepository;
    private MutableLiveData<User> userLD;
    private MediatorLiveData<MapMediator> mediator;

    private MutableLiveData<GoogleMap> mapLD;
    private MutableLiveData<PlacesClient> placesClientLD;
    private MutableLiveData<PlaceInfoHolder> customPlaceLD;
    private MutableLiveData<Boolean> isInitCameraMoveComplete;
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

    public void setMapLD(GoogleMap map) {
        mapLD.setValue(map);
    }

    public void setPlacesClientLD(PlacesClient placesClientMutable) {
        placesClientLD.setValue(placesClientMutable);
    }

    public void setCustomPlaceLD(PlaceInfoHolder customPlace) {
        this.customPlaceLD.setValue(customPlace);
    }

    public void setIsInitCameraMoveComplete(Boolean isInitCameraMoveComplete) {
        this.isInitCameraMoveComplete.setValue(isInitCameraMoveComplete);
    }

    public MutableLiveData<Boolean> getIsInitCameraMoveComplete() {
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
        customPlaceLD = new MutableLiveData<>();
        isInitCameraMoveComplete = new MutableLiveData<>();
        isInitCameraMoveComplete.setValue(false);

        LiveData<List<Session>> recentSessionsLD = Transformations.switchMap(userLD, (User user) -> daoRepository.getRecentSessionsWithLocationForUser(10, user.getId()));

        mediator = new MediatorLiveData<>();
        mediator.setValue(new MapMediator());
        mediator.addSource(recentSessionsLD, sessions -> {
            MapMediator med = mediator.getValue();
            if (med != null)
                med.setRecentSessions(sessions);
            mediator.setValue(med);
        });
        mediator.addSource(mapLD, map ->{
            MapMediator med = mediator.getValue();
            if (med != null)
                med.setMap(map);
            mediator.setValue(med);
        });
        mediator.addSource(placesClientLD,placesClient -> {
            MapMediator med = mediator.getValue();
            if (med != null)
                med.setPlacesClient(placesClient);
            mediator.setValue(med);
        });
        mediator.addSource(customPlaceLD, customPlace ->{
            MapMediator med = mediator.getValue();
            if (med != null)
                med.setCustomPlace(customPlace);
            mediator.setValue(med);
        });
    }
}
