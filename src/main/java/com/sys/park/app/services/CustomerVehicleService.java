package com.sys.park.app.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sys.park.app.dtos.Customer.CustomerDto;
import com.sys.park.app.dtos.CustomerVehicle.CustomerVehicleDto;
import com.sys.park.app.dtos.Vehicle.VehicleDto;
import com.sys.park.app.models.CustomerVehicleModel;
import com.sys.park.app.models.VehicleModel;
import com.sys.park.app.repositories.CustomerVehicleRepository;
import com.sys.park.app.services.exceptions.BusinessRuleException;
import com.sys.park.app.services.exceptions.DataIntegrityException;
import com.sys.park.app.services.exceptions.NotFoundException;

@Service
public class CustomerVehicleService {
    @Autowired
    private CustomerVehicleRepository customerVehicleRepository;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ModelMapper modelMapper;

    public List<CustomerVehicleDto> findAll() {
        try {
            List<CustomerVehicleModel> customerTypeModelList = customerVehicleRepository.findAll();

            return customerTypeModelList.stream()
                    .map(customerVehicle -> modelMapper.map(customerVehicle, CustomerVehicleDto.class))
                    .collect(Collectors.toList());
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Não é possível consultar o Tipo do Cliente!");
        }
     }

    public CustomerVehicleModel insert(CustomerVehicleDto customerVehicleDto) {
        try {
            CustomerVehicleModel newCustomer = modelMapper.map(customerVehicleDto, CustomerVehicleModel.class);
                
            if(customerVehicleRepository.existsByIdCustomerAndIdVehicle(customerVehicleDto.getIdCustomer(),customerVehicleDto.getIdVehicle())) {
                throw new DataIntegrityException("Veiculo já existe para este cliente");
            }

            newCustomer = customerVehicleRepository.save(newCustomer);
            return newCustomer;
    
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Erro ao tentar inserir um cliente!");
        }
    }

    public CustomerVehicleDto findById(Long id) {
        try {
            CustomerVehicleModel customerVehicleModel = customerVehicleRepository.findById(id).get();
            return modelMapper.map(customerVehicleModel, CustomerVehicleDto.class);
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + CustomerVehicleModel.class.getName());
        }
    }

    public Page<CustomerVehicleDto> findByIdCustomer(Pageable pageable, Long idCustomer) {
        return customerVehicleRepository.findByIdCustomer(pageable, idCustomer).map(vehicle -> {
            CustomerVehicleDto customerVehicleDto = modelMapper.map(vehicle, CustomerVehicleDto.class);

            Integer value = Integer.parseInt(String.valueOf(vehicle.getId()));

            VehicleDto vehicleDto = vehicleService.findById(value);    

            customerVehicleDto.setPlate(vehicleDto.getPlate());
            customerVehicleDto.setMake(vehicleDto.getMake());
            customerVehicleDto.setModel(vehicleDto.getModel());
            customerVehicleDto.setIsActive(true);

            return customerVehicleDto;
        });
    }    
  
    public List<CustomerVehicleDto> findByIdVehicle(Pageable pageable, Long idCustomer) {
        try {
            Page<CustomerVehicleModel> customerVehicleModel = customerVehicleRepository.findByIdCustomer(pageable, idCustomer);
            
            return customerVehicleModel.stream()
            .map(customerVehicle -> modelMapper.map(customerVehicle, CustomerVehicleDto.class))
            .collect(Collectors.toList());
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Objeto não encontrado! Id: " + idCustomer + ", Tipo: " + CustomerVehicleModel.class.getName());
        }
    }  

    public CustomerVehicleDto findByIdCustomerAndIdVehicle(Long idCustomer, Long idVehicle) {
        try {
            CustomerVehicleModel customerVehicleModel = customerVehicleRepository.findByIdCustomerAndIdVehicle(idCustomer, idVehicle).get();
            return modelMapper.map(customerVehicleModel, CustomerVehicleDto.class);
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Objeto não encontrado! Id: " + idCustomer + ", Tipo: " + CustomerVehicleModel.class.getName());
        }
    }   

    public Boolean vehicleCustomerExist(Long idCustomer, Long idVehicle) {
        try {
            Optional<CustomerVehicleModel> customerVehicle = customerVehicleRepository.findByIdCustomerAndIdVehicle(idCustomer, idVehicle);

            if(customerVehicle.isPresent()) {
                return true;
            } else {
                return false;
            }
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Objeto não encontrado! Id: " + idCustomer + ", Tipo: " + CustomerVehicleModel.class.getName());
        }
    }   

    public VehicleDto addVehicle(Long idCustomer, VehicleDto vehicleDto) {
        VehicleModel vehicleModel = modelMapper.map(vehicleDto, VehicleModel.class);
        // CustomerModel customerModel = modelMapper.map(customerDto, CustomerModel.class);
        
        VehicleDto newVehicle = vehicleService.insert(vehicleModel);
        CustomerDto customer = customerService.findById(idCustomer);

        CustomerVehicleDto customerVehicleDto = new CustomerVehicleDto();

        customerVehicleDto.setIdCustomer(customer.getId());
        customerVehicleDto.setIdVehicle(newVehicle.getId());

        this.insert(customerVehicleDto);

        return newVehicle;
    }
}
