package com.ABCIgnite.controller;

import com.ABCIgnite.dto.request.ClassRequestDTO;
import com.ABCIgnite.dto.response.ClassResponseDTO;
import com.ABCIgnite.entities.ClassEntity;
import com.ABCIgnite.service.ClassService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/classes")
public class ClassController {

    private final ClassService classService;

    public ClassController(ClassService classService) {
        this.classService = classService;
    }

    @PostMapping
    public ResponseEntity<ClassResponseDTO> createClass(@Valid @RequestBody ClassRequestDTO classRequestDTO) {
        ClassResponseDTO classResponseDTO = classService.createClass(classRequestDTO);
        return ResponseEntity.ok(classResponseDTO);
    }

    @GetMapping
    public ResponseEntity<List<ClassResponseDTO>> getAllClasses() {
        return ResponseEntity.ok(classService.getAllClasses());
    }
}
