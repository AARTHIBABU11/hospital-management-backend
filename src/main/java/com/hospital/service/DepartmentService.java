package com.hospital.service;

import com.hospital.entity.Department;
import com.hospital.exception.BadRequestException;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + id));
    }

    public Department createDepartment(Department department) {
        if (departmentRepository.existsByName(department.getName())) {
            throw new BadRequestException("Department already exists with name: " + department.getName());
        }
        return departmentRepository.save(department);
    }

    public Department updateDepartment(Long id, Department details) {
        Department dept = getDepartmentById(id);
        dept.setName(details.getName());
        dept.setDescription(details.getDescription());
        dept.setIcon(details.getIcon());
        dept.setHeadDoctor(details.getHeadDoctor());
        dept.setStatus(details.getStatus());
        return departmentRepository.save(dept);
    }

    public void deleteDepartment(Long id) {
        Department dept = getDepartmentById(id);
        departmentRepository.delete(dept);
    }
}
