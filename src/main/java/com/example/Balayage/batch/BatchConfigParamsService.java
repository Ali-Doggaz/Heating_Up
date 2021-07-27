package com.example.Balayage.batch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class BatchConfigParamsService {
    @Autowired
    BatchConfigParamsRepository batchConfigParamsRepository;

    @Transactional
    public List<BatchConfigParams> getConfigs(){
        return  batchConfigParamsRepository.findAll();
    }

    @Transactional
    public BatchConfigParams addConfig(BatchConfigParams newbatchConfigParams){
        return batchConfigParamsRepository.save(newbatchConfigParams);
    }

    public void deleteConfigById(Long id){
        batchConfigParamsRepository.deleteById(id);
    }
}
