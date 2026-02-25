package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml.repositories.RateXmlRepository;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.Rate;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.SpaceType;
import org.jdom2.JDOMException;

import java.io.IOException;
import java.util.ArrayList;

public class RateData extends RateXmlRepository {
    private ArrayList<Rate> rates;

    public RateData() throws IOException, JDOMException {
        super();
        this.rates = new ArrayList<>();
        reload();
    }
     private void reload(){
        rates.clear();
        rates.addAll(super.findAll());
     }

    public ArrayList<Rate> getAllRates() {
        return rates;
    }

    public void registerRate(Rate rate) throws IOException {
        if (findRateById(rate.getRateId()) != null) {
            throw new IllegalArgumentException("Rate already exists");
        }
        super.insert(rate);
        rates.add(rate);
    }

    public Rate findRateById(int rateId) {
        return findById(rateId).orElse(null);
    }

    public void updateRate(Rate updatedRate) throws IOException {
       if (findRateById(updatedRate.getRateId()) == null) {
           throw new IllegalArgumentException("Rate not found");
       }
        super.update(updatedRate);
        rates.remove(findRateById(updatedRate.getRateId()));
        rates.add(updatedRate);
    }

    public void deleteRate(Rate rate) throws IOException {
        if (findRateById(rate.getRateId()) == null) {
            throw new IllegalArgumentException("Rate not found");
        }
        super.delete(rate);
        rates.remove(rate);
    }

    public Rate findBySpaceType(SpaceType type) {
        reload(); // Recargar para asegurar que tengamos los datos más recientes
        return rates.stream()
                .filter(r -> r.getVehicleType() != null
                        && r.getVehicleType().getSpaceType() != null
                        && r.getVehicleType().getSpaceType() == type)
                .findFirst()
                .orElse(null);
    }

}
