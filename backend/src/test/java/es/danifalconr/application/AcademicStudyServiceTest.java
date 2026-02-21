package es.danifalconr.application;

import es.danifalconr.domain.exception.EntityNotFoundException;
import es.danifalconr.domain.model.AcademicStudy;
import es.danifalconr.domain.port.out.AcademicStudyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AcademicStudyServiceTest {

    @Mock
    AcademicStudyRepository academicStudyRepository;

    @InjectMocks
    AcademicStudyService academicStudyService;

    @Test
    void create_delegatesToRepositoryAndReturnsPersistedEntity() {
        AcademicStudy input = new AcademicStudy(null, "MIT", "Computer Science");
        AcademicStudy persisted = new AcademicStudy(1L, "MIT", "Computer Science");
        when(academicStudyRepository.save(input)).thenReturn(persisted);

        AcademicStudy result = academicStudyService.create(input);

        assertEquals(1L, result.id());
        assertEquals("MIT", result.schoolName());
        assertEquals("Computer Science", result.titleName());
        verify(academicStudyRepository).save(input);
    }

    @Test
    void update_delegatesToRepositoryAndReturnsUpdatedEntity() {
        AcademicStudy input = new AcademicStudy(null, "Stanford", "Software Engineering");
        AcademicStudy updated = new AcademicStudy(1L, "Stanford", "Software Engineering");
        when(academicStudyRepository.update(1L, input)).thenReturn(updated);

        AcademicStudy result = academicStudyService.update(1L, input);

        assertEquals("Stanford", result.schoolName());
        assertEquals("Software Engineering", result.titleName());
        verify(academicStudyRepository).update(1L, input);
    }

    @Test
    void update_whenEntityNotFound_propagatesEntityNotFoundException() {
        AcademicStudy input = new AcademicStudy(null, "School", "Title");
        when(academicStudyRepository.update(999L, input))
                .thenThrow(new EntityNotFoundException("AcademicStudy not found with id: 999"));

        assertThrows(EntityNotFoundException.class, () -> academicStudyService.update(999L, input));
    }

    @Test
    void delete_delegatesRemoveToRepository() {
        doNothing().when(academicStudyRepository).remove(1L);

        academicStudyService.delete(1L);

        verify(academicStudyRepository).remove(1L);
    }

    @Test
    void delete_whenEntityNotFound_propagatesEntityNotFoundException() {
        doThrow(new EntityNotFoundException("AcademicStudy not found with id: 999"))
                .when(academicStudyRepository).remove(999L);

        assertThrows(EntityNotFoundException.class, () -> academicStudyService.delete(999L));
    }

    @Test
    void getAll_returnsAllStudiesOrderedByRepository() {
        List<AcademicStudy> studies = List.of(
                new AcademicStudy(2L, "Harvard", "Law"),
                new AcademicStudy(1L, "MIT", "Computer Science")
        );
        when(academicStudyRepository.findAllOrderByCreatedAtDesc()).thenReturn(studies);

        List<AcademicStudy> result = academicStudyService.getAll();

        assertEquals(2, result.size());
        assertEquals("Harvard", result.get(0).schoolName());
        verify(academicStudyRepository).findAllOrderByCreatedAtDesc();
    }
}
