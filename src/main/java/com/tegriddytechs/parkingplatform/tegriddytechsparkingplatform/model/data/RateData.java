package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.Rate;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.SpaceType;

import java.util.ArrayList;

public class RateData {

    private ArrayList<Rate> rates;

    public RateData() {
        this.rates = new ArrayList<>();
    }

    public ArrayList<Rate> getAllRates() {
        return rates;
    }

    public void registerRate(Rate rate) {
        rates.add(rate);
    }

    public Rate findRateById(int rateId) {
        for (Rate rate : rates) {
            if (rate.getRateId() == rateId) {
                return rate;
            }
        }
        return null;
    }

    public void updateRate(Rate updatedRate) {
        for (int i = 0; i < rates.size(); i++) {
            if (rates.get(i).getRateId() == updatedRate.getRateId()) {
                rates.set(i, updatedRate);
                return;
            }
        }
    }

    public void deleteRate(Rate rate) {
        rates.remove(rate);
    }

    public Rate findBySpaceType(SpaceType type) {
        return rates.stream().filter(r -> r.getVehicleType().getSpaceType() == type).findFirst().orElse(null);
    }

}
