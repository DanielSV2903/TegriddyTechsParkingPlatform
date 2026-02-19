package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.controller;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.RateData;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.OperationResult;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.Rate;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.SpaceType;
import org.jdom2.JDOMException;

import java.io.IOException;
import java.util.List;

public class RateController {
    private RateData rateData;
    public RateController() throws IOException, JDOMException {
        rateData = new RateData();
    }


    public OperationResult addRate(Rate rate) throws IOException {
        if (rate==null) {
            return OperationResult.failure("Rate cannot be null");
        }
        if (rateData.findRateById(rate.getRateId()) != null) {
            return OperationResult.failure("Rate already exists");
        }
        rateData.registerRate(rate);
        return OperationResult.success("Rate added");
    }

    public OperationResult removeRate(Rate existing) throws IOException {
        if (existing == null) {
            return OperationResult.failure("Rate not found");
        }
        rateData.deleteRate(existing);
        return OperationResult.success("Rate removed");
    }

    public OperationResult updateRate(Rate rate) throws IOException {
        if (rate==null) {
            return OperationResult.failure("Rate cannot be null");
        }
        if (rateData.findRateById(rate.getRateId()) == null) {
            return OperationResult.failure("Rate not found");
        }
        rateData.updateRate(rate);
        return OperationResult.success("Rate updated");
    }

    public List<Rate> getAllRates() {
        return rateData.getAllRates();
    }

    public Rate findRateById(int rateId) {
        for (Rate rate :getAllRates()) {
            if (rate.getRateId() == rateId) {
                return rate;
            }
        }
        return null;
    }

    public Rate findBySpaceType(SpaceType spaceType) {
        return rateData.findBySpaceType(spaceType);
    }
}
