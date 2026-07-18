package me.carlosalmanzab.wardenpos.backend.features.auth.repository;

import java.util.UUID;
import me.carlosalmanzab.wardenpos.jooq.generated.tables.pojos.Employees;

public interface EmployeeRepository {
  Employees findById(UUID employeeId);
}
