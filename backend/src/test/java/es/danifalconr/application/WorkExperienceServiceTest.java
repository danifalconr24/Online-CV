package es.danifalconr.application;

import es.danifalconr.domain.exception.EntityNotFoundException;
import es.danifalconr.domain.model.WorkExperience;
import es.danifalconr.domain.port.out.WorkExperienceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkExperienceServiceTest {

    @Mock
    WorkExperienceRepository workExperienceRepository;

    @InjectMocks
    WorkExperienceService workExperienceService;

    @Test
    void create_delegatesToRepositoryAndReturnsPersistedEntity() {
        WorkExperience input = new WorkExperience(null, "01/01/2021", "31/12/2021", false, "ACME Corp", "Software Engineer", null);
        WorkExperience persisted = new WorkExperience(1L, "01/01/2021", "31/12/2021", false, "ACME Corp", "Software Engineer", null);
        when(workExperienceRepository.save(input)).thenReturn(persisted);

        WorkExperience result = workExperienceService.create(input);

        assertEquals(1L, result.id());
        assertEquals("ACME Corp", result.company());
        verify(workExperienceRepository).save(input);
    }

    @Test
    void update_delegatesToRepositoryAndReturnsUpdatedEntity() {
        WorkExperience input = new WorkExperience(null, "01/01/2021", "31/12/2021", false, "Updated Corp", "Updated role", null);
        WorkExperience updated = new WorkExperience(1L, "01/01/2021", "31/12/2021", false, "Updated Corp", "Updated role", null);
        when(workExperienceRepository.update(1L, input)).thenReturn(updated);

        WorkExperience result = workExperienceService.update(1L, input);

        assertEquals("Updated Corp", result.company());
        assertEquals("Updated role", result.description());
        verify(workExperienceRepository).update(1L, input);
    }

    @Test
    void update_whenEntityNotFound_propagatesEntityNotFoundException() {
        WorkExperience input = new WorkExperience(null, "01/01/2021", null, false, "Company", "Desc", null);
        when(workExperienceRepository.update(999L, input))
                .thenThrow(new EntityNotFoundException("WorkExperience not found with id: 999"));

        assertThrows(EntityNotFoundException.class, () -> workExperienceService.update(999L, input));
    }

    @Test
    void delete_delegatesRemoveToRepository() {
        doNothing().when(workExperienceRepository).remove(1L);

        workExperienceService.delete(1L);

        verify(workExperienceRepository).remove(1L);
    }

    @Test
    void delete_whenEntityNotFound_propagatesEntityNotFoundException() {
        doThrow(new EntityNotFoundException("WorkExperience not found with id: 999"))
                .when(workExperienceRepository).remove(999L);

        assertThrows(EntityNotFoundException.class, () -> workExperienceService.delete(999L));
    }

    @Test
    void getAll_returnsAllExperiencesOrderedByRepository() {
        List<WorkExperience> experiences = List.of(
                new WorkExperience(2L, "01/01/2022", null, true, "Latest Corp", "Lead role", null),
                new WorkExperience(1L, "01/01/2020", "31/12/2021", false, "Old Corp", "Junior role", null)
        );
        when(workExperienceRepository.findAllOrderByCreatedAtDesc()).thenReturn(experiences);

        List<WorkExperience> result = workExperienceService.getAll();

        assertEquals(2, result.size());
        assertEquals("Latest Corp", result.get(0).company());
        assertEquals("Old Corp", result.get(1).company());
        verify(workExperienceRepository).findAllOrderByCreatedAtDesc();
    }

    @Test
    void updateLogo_encodesAndUpdatesLogo() {
        byte[] imageBytes = "fake-image".getBytes();
        String expectedBase64 = Base64.getEncoder().encodeToString(imageBytes);
        WorkExperience existing = new WorkExperience(1L, "2023-01", null, true, "ACME", "Dev", null);
        WorkExperience updatedResult = new WorkExperience(1L, "2023-01", null, true, "ACME", "Dev", expectedBase64);

        when(workExperienceRepository.getById(1L)).thenReturn(Optional.of(existing));
        when(workExperienceRepository.update(eq(1L), any())).thenReturn(updatedResult);

        WorkExperience result = workExperienceService.updateLogo(1L, imageBytes);

        assertEquals(expectedBase64, result.companyLogo());
        verify(workExperienceRepository).update(eq(1L), argThat(w -> expectedBase64.equals(w.companyLogo())));
    }

    @Test
    void updateLogo_whenEntityNotFound_throwsEntityNotFoundException() {
        when(workExperienceRepository.getById(99L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> workExperienceService.updateLogo(99L, new byte[0]));
    }
}
