package com.ABCIgnite;

import com.ABCIgnite.controller.ClassController;
import com.ABCIgnite.dto.request.ClassRequestDTO;
import com.ABCIgnite.dto.response.ClassResponseDTO;
import com.ABCIgnite.service.ClassService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClassControllerTest {

    private ClassService classService;
    private ClassController classController;

    @BeforeEach
    void setUp() {
        classService = mock(ClassService.class);
        classController = new ClassController(classService);
    }

    @Test
    void createClass_ValidRequest_ReturnsCreatedClass() {
        ClassRequestDTO request = new ClassRequestDTO(
                "Yoga",
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(10),
                LocalTime.of(9, 0),
                60,
                20
        );

        ClassResponseDTO response = new ClassResponseDTO(
                "class123",
                "Yoga",
                request.getStartDate(),
                request.getEndDate(),
                request.getStartTime(),
                request.getDuration(),
                request.getCapacity()
        );

        when(classService.createClass(request)).thenReturn(response);

        ResponseEntity<ClassResponseDTO> result = classController.createClass(request);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals("Yoga", result.getBody().getName());
        verify(classService, times(1)).createClass(request);
    }

    @Test
    void createClass_InvalidEndDate_ThrowsException() {
        ClassRequestDTO request = new ClassRequestDTO(
                "Yoga",
                LocalDate.now().plusDays(1),
                LocalDate.now().minusDays(1), // Invalid end date
                LocalTime.of(9, 0),
                60,
                20
        );

        when(classService.createClass(request))
                .thenThrow(new IllegalArgumentException("End date must be in the future"));

        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> classController.createClass(request)
        );

        assertEquals("End date must be in the future", exception.getMessage());
        verify(classService, times(1)).createClass(request);
    }

    @Test
    void getAllClasses_EmptyList_ReturnsEmptyResponse() {
        when(classService.getAllClasses()).thenReturn(Collections.emptyList());

        ResponseEntity<List<ClassResponseDTO>> result = classController.getAllClasses();

        assertEquals(200, result.getStatusCodeValue());
        assertTrue(result.getBody().isEmpty());
        verify(classService, times(1)).getAllClasses();
    }

    @Test
    void getAllClasses_ReturnsListOfClasses() {
        List<ClassResponseDTO> responseList = Arrays.asList(
                new ClassResponseDTO("class1", "Yoga", LocalDate.now(), LocalDate.now().plusDays(10), LocalTime.of(9, 0), 60, 20),
                new ClassResponseDTO("class2", "Pilates", LocalDate.now(), LocalDate.now().plusDays(5), LocalTime.of(10, 0), 45, 15)
        );

        when(classService.getAllClasses()).thenReturn(responseList);

        ResponseEntity<List<ClassResponseDTO>> result = classController.getAllClasses();

        assertEquals(200, result.getStatusCodeValue());
        assertEquals(2, result.getBody().size());
        verify(classService, times(1)).getAllClasses();
    }
}
