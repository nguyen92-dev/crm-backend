package vn.io.nguyen32.crm.appuser.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import top.nguyennd.restsqlbackend.abstraction.exception.BusinessException;
import vn.io.nguyen32.crm.appuser.AppUserRepository;
import vn.io.nguyen32.crm.appuser.dto.AppUserBaseResDto;
import vn.io.nguyen32.crm.appuser.dto.AppUserReqDto;
import vn.io.nguyen32.crm.appuser.dto.UserMapper;
import vn.io.nguyen32.crm.appuser.entity.AppUser;
import vn.io.nguyen32.crm.rediscache.IRedisCacheService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static vn.io.nguyen32.crm.common.AppConstant.DEFAULT_ROLE;
import static vn.io.nguyen32.crm.rediscache.CommonKey.ENTITY_KEY;

@ExtendWith(MockitoExtension.class)
class AppUserServiceImplTest {

    @Mock
    private AppUserRepository repository;

    @Mock
    private UserMapper mapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private IRedisCacheService cacheService;

    @InjectMocks
    private AppUserServiceImpl appUserService;

    private AppUserReqDto reqDto;
    private AppUser savedEntity;

    @BeforeEach
    void setUp() {
        reqDto = new AppUserReqDto("testuser", "password123", "test@example.com", "Test User");
        savedEntity = AppUser.builder()
            .id(1L)
            .username("testuser")
            .password("encoded_password")
            .email("test@example.com")
            .fullName("Test User")
            .roleName(DEFAULT_ROLE)
            .build();
    }

    @Test
    void createUser_ShouldWorkCorrectly() {
        // Mock mapper toEntity
        when(mapper.toEntity(reqDto)).thenReturn(AppUser.builder()
                .username(reqDto.username())
                .password(reqDto.password())
            .build());
        
        // Mock passwordEncoder
        when(passwordEncoder.encode(reqDto.password())).thenReturn("encoded_password");

        // Mock redis set cache
        doNothing().when(cacheService).setCache(any(), any(), any(), any());
        
        // Mock repository save
        when(repository.save(any())).thenAnswer(invocation -> {
                AppUser u = invocation.getArgument(0);
                u.setId(1L);
                return u;
            });
        
        // Mock mapper toResDto
        when(mapper.toResDto(any())).thenAnswer(invocationOnMock -> {
            AppUser u = invocationOnMock.getArgument(0);
            return AppUserBaseResDto.builder()
                .id(u.getId())
                .username(u.getUsername())
                .email(u.getEmail())
                .build();
        });

        // Execute
        AppUserBaseResDto result = appUserService.createUser(reqDto);

        // Verify
        ArgumentCaptor<AppUser> captor = ArgumentCaptor.forClass(AppUser.class);
        verify(repository).save(captor.capture());

        AppUser savedUser = captor.getValue();

        ArgumentCaptor<String> cacheKeyCaptor = ArgumentCaptor.forClass(String.class);
        verify(cacheService).setCache(cacheKeyCaptor.capture(), eq(result), any(), any());
        assertNotNull(cacheKeyCaptor.getValue());

        String cacheKey = cacheKeyCaptor.getValue();
        assertEquals(ENTITY_KEY.formatted(AppUser.class.getSimpleName(), result.id()), cacheKey);

        verify(repository, atMost(2)).findAll((Specification<AppUser>) isNotNull());


        assertEquals("encoded_password", savedUser.getPassword());
        assertEquals(DEFAULT_ROLE, savedUser.getRoleName());

        assertEquals(savedUser.getUsername(), result.username());
    }

    @Test
    void createUser_ConflictUser_ShouldThrowException() {
        when(repository.findAll(any(Specification.class))).thenReturn(List.of(savedEntity));

        // Execute
        BusinessException businessException = assertThrows(BusinessException.class, () -> appUserService.createUser(reqDto));
        assertEquals(HttpStatus.CONFLICT, businessException.getStatusCode());

        // Verify
        verify(repository, atLeast(1)).findAll((Specification<AppUser>) isNotNull());
        verify(repository, never()).save(any());
        verify(passwordEncoder, never()).encode(any());
        verify(cacheService, never()).setCache(any(), any(), any(), any());
        verify(mapper, never()).toResDto(any());
    }
}
