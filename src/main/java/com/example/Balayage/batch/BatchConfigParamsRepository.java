package com.example.Balayage.batch;

import com.example.Balayage.regles.statsExceptions.StatsException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BatchConfigParamsRepository extends JpaRepository<BatchConfigParams, Long> {

}
