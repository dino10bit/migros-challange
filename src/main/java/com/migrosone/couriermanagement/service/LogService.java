package com.migrosone.couriermanagement.service;

import com.migrosone.couriermanagement.dto.Store;
import com.migrosone.couriermanagement.entity.CourierLocation;
import com.migrosone.couriermanagement.entity.StoreEntryLog;
import com.migrosone.couriermanagement.enumeration.Unit;
import com.migrosone.couriermanagement.repository.StoreEntryLogRepository;
import com.migrosone.couriermanagement.util.DistanceCalculator;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class LogService {

    private static final Double DEVIATION_IN_METERS = 100D;
    private static final Long SIXTY_SECONDS = 60L;

    private final StoreService storeService;

    private final StoreEntryLogRepository storeEntryLogRepository;

    public LogService(
            com.migrosone.couriermanagement.service.StoreService storeService,
            StoreEntryLogRepository storeEntryLogRepository) {
        this.storeService = storeService;
        this.storeEntryLogRepository = storeEntryLogRepository;
    }

    public void logStoreEntries(List<CourierLocation> locations) {
        locations.forEach(
                location -> {
                    Optional<Store> closestStore =
                            getIfCloseToAnyStore(
                                    location.getCourierId(),
                                    location.getLatitude(),
                                    location.getLongitude());
                    // TODO: too much time consuming
                    if (closestStore.isPresent()) {
                        Optional<StoreEntryLog> existingLog = getIfOlderRecordExists(location);
                        if (!existingLog.isPresent()) {
                            createLogRecord(location, closestStore.get());
                        } else {
                            log.info(
                                    "Courier already has a log which is more recent then a minute. courierId={}",
                                    location.getCourierId());
                        }
                    }
                });
    }

    private Optional<Store> getIfCloseToAnyStore(
            String courierId, Double latitude, Double longitude) {
        List<Store> stores = storeService.getStores();

        for (Store store : stores) {
            double distance =
                    DistanceCalculator.distance(
                            latitude, longitude, store.getLat(), store.getLng(), Unit.METER);

            if (DEVIATION_IN_METERS > distance) {
                log.info(
                        "Courier is near a store. courierId={}, storeName={}",
                        courierId,
                        store.getName());
                return Optional.of(store);
            }
        }

        return Optional.empty();
    }

    private Optional<StoreEntryLog> getIfOlderRecordExists(CourierLocation location) {
        return storeEntryLogRepository.findFirstByCourierIdAndTimeGreaterThan(
                location.getCourierId(), location.getTime() - SIXTY_SECONDS);
    }

    private void createLogRecord(CourierLocation location, Store store) {
        StoreEntryLog log = new StoreEntryLog();
        log.setCourierId(location.getCourierId());
        log.setStoreName(store.getName());
        log.setTime(location.getTime());
        log.setLatitude(location.getLatitude());
        log.setLongitude(location.getLongitude());

        storeEntryLogRepository.save(log);
    }
}
