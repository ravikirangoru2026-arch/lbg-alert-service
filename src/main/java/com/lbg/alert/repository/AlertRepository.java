package com.lbg.alert.repository;

import com.lbg.alert.entity.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long>,
        JpaSpecificationExecutor<Alert> {

    Optional<Alert> findByAlertRef(String alertRef);
    boolean existsByAlertRef(String alertRef);

    // Summary queries
    @Query("SELECT a.status AS groupKey, COUNT(a) AS count FROM Alert a GROUP BY a.status")
    List<SummaryProjection> countByStatus();

    @Query("SELECT a.riskBand AS groupKey, COUNT(a) AS count FROM Alert a GROUP BY a.riskBand")
    List<SummaryProjection> countByRiskBand();

    @Query("SELECT a.alertType AS groupKey, COUNT(a) AS count FROM Alert a GROUP BY a.alertType")
    List<SummaryProjection> countByAlertType();

    interface SummaryProjection {
        Object getGroupKey();
        Long getCount();
    }
}