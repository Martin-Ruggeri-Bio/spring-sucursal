package ar.edu.um.programacion2.simple.service;

import ar.edu.um.programacion2.simple.model.Detail;
import ar.edu.um.programacion2.simple.repository.DetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DetailService {

    @Autowired
    private DetailRepository detailRepository;

    public void createDetail(Detail detail){
        this.detailRepository.save(detail);
    }
    public List<Detail> getDetailBySale(String saleId){
        return this.detailRepository.findBySale_Id(saleId);
    }
    public List<Detail> getDetailByClient(String clientId){
        return this.detailRepository.findByClient_Id(clientId);
    }
}
