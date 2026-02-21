package es.danifalconr.application;

import es.danifalconr.domain.exception.EntityNotFoundException;
import es.danifalconr.domain.model.GenericInfo;
import es.danifalconr.domain.port.out.GenericInfoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenericInfoServiceTest {

    @Mock
    GenericInfoRepository genericInfoRepository;

    @InjectMocks
    GenericInfoService genericInfoService;

    @Test
    void getLatest_returnsLatestEntryFromRepository() {
        GenericInfo expected = new GenericInfo(1L, "Java Software Engineer");
        when(genericInfoRepository.findLatest()).thenReturn(expected);

        GenericInfo result = genericInfoService.getLatest();

        assertEquals(1L, result.id());
        assertEquals("Java Software Engineer", result.aboutMe());
        verify(genericInfoRepository).findLatest();
    }

    @Test
    void save_delegatesToRepositoryAndReturnsPersistedEntity() {
        GenericInfo input = new GenericInfo(null, "Full-Stack Developer");
        GenericInfo persisted = new GenericInfo(1L, "Full-Stack Developer");
        when(genericInfoRepository.save(input)).thenReturn(persisted);

        GenericInfo result = genericInfoService.save(input);

        assertEquals(1L, result.id());
        assertEquals("Full-Stack Developer", result.aboutMe());
        verify(genericInfoRepository).save(input);
    }

    @Test
    void update_delegatesToRepositoryAndReturnsUpdatedEntity() {
        GenericInfo input = new GenericInfo(null, "Updated bio text");
        GenericInfo updated = new GenericInfo(1L, "Updated bio text");
        when(genericInfoRepository.update(1L, input)).thenReturn(updated);

        GenericInfo result = genericInfoService.update(1L, input);

        assertEquals("Updated bio text", result.aboutMe());
        verify(genericInfoRepository).update(1L, input);
    }

    @Test
    void update_whenEntityNotFound_propagatesEntityNotFoundException() {
        GenericInfo input = new GenericInfo(null, "Some bio");
        when(genericInfoRepository.update(999L, input))
                .thenThrow(new EntityNotFoundException("GenericInfo not found with id: 999"));

        assertThrows(EntityNotFoundException.class, () -> genericInfoService.update(999L, input));
    }
}
