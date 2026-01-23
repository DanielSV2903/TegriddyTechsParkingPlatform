package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.controller;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.ParkingLotData;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.OperationResult;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.ParkingLot;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.Rate;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.User;

import java.lang.ref.PhantomReference;

public class AdminController {
    private final User currentUser;
    private ParkingLotData parkingLotData;

    public AdminController(User currentUser) {
        this.currentUser = currentUser;
    }

    public OperationResult createUser(User user, UserController userController) {
        userController.addUser(user);
        return OperationResult.success("User created");
    }

    public OperationResult createParkingLot(ParkingLot lot) {
        parkingLotData.registerParkingLot(lot);
        return OperationResult.success("Parking lot created");
    }

    public OperationResult createRate(Rate rate) {
        return OperationResult.success("Rate created");
    }
}
