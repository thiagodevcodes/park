package com.sys.park.app.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sys.park.app.models.CustomerModel;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerModel, Integer> {
    /*
        @Query(value = "SELECT p.name, p.phone, p.email, p.cpf, c.payment_day, ct.name " +
                   "FROM customer c " +
                   "JOIN person p ON c.id_person = p.id " +
                   "JOIN customer_type ct ON c.id_customer_type = ct.id " +
                   "WHERE ct.name = 'Mensalista'", nativeQuery = true)
        List<CustomerModel> findCustomersMensal();
     */
    List<CustomerModel> findByIdCustomerType(Integer idCustomerType);
    Optional<CustomerModel> findByIdPerson(Integer idPerson);
}
