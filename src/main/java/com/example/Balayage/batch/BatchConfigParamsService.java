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
    public BatchConfigParams getConfig(){
        List<BatchConfigParams> configs =  batchConfigParamsRepository.findAll();
        //Si aucune config n'est trouvée, on ajoute la config par default ci-dessous
        if (configs.size()==0){
            BatchConfigParams batchConfigParams = new BatchConfigParams(1000, 1000,
                    2000,"* * 8 * * *");
            batchConfigParamsRepository.save(batchConfigParams);
            return batchConfigParams;
        }

        else{ return configs.get(0);}
    }

    @Transactional
    public void updateConfig(BatchConfigParams newbatchConfigParams){
        batchConfigParamsRepository.deleteAll();
        batchConfigParamsRepository.save(newbatchConfigParams);
    }
}
