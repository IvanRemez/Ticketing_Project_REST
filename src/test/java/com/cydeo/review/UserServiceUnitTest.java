package com.cydeo.review;

import com.cydeo.dto.RoleDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Role;
import com.cydeo.entity.User;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.KeycloakService;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import com.cydeo.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ProjectService projectService;
    @Mock
    private TaskService taskService;
    @Mock
    private KeycloakService keycloakService;

    @InjectMocks
    private UserServiceImpl userService;

    @Spy
    private UserMapper userMapper = new UserMapper(new ModelMapper());

    User user;
    UserDTO userDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUserName("user");
        user.setPassWord("Abc1");
        user.setEnabled(true);
        user.setRole(new Role("Manager"));

        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");
        userDTO.setUserName("user");
        userDTO.setPassWord("Abc1");
        userDTO.setEnabled(true);

        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setDescription("Manager");

        userDTO.setRole(roleDTO);
    }

    private List<User> getUsers() {
        User user2 = new User();
        user2.setId(2L);
        user2.setFirstName("Emily");

        return List.of(user, user2);
    }

    private List<UserDTO> getUserDTOs() {
        UserDTO userDTO2 = new UserDTO();
        userDTO2.setId(2L);
        userDTO2.setFirstName("Emily");
        return List.of(userDTO, userDTO2);
    }

// TEST - public List<UserDTO> listAllUsers()
    @Test
    void should_list_all_users() {
// STUB - provide behavior to the Mock
        when(userRepository.findAllByIsDeletedOrderByFirstNameDesc(false))
                .thenReturn(getUsers());

        List<UserDTO> expectedList = getUserDTOs();
        List<UserDTO> actualList = userService.listAllUsers();

//        Assertions.assertEquals(expectedList,actualList);
        //AssertJ
        assertThat(actualList).usingRecursiveComparison().isEqualTo(expectedList);
        //                          ^^ compares Object VALUES instead of Objects themselves
    }

// TEST - public UserDTO findByUserName(String username)
    @Test
    void should_find_user_by_username() {
// STUB
        when(userRepository.findByUserNameAndIsDeleted(anyString(), anyBoolean()))
                .thenReturn(user);

        UserDTO actualUserDTO = userService.findByUserName("user");

        assertThat(actualUserDTO).usingRecursiveComparison().isEqualTo(userDTO);
    }

    @Test
    void should_throw_exception_when_user_NOT_found() {
// STUB
        when(userRepository.findByUserNameAndIsDeleted(anyString(), anyBoolean()))
                .thenReturn(null);

        Throwable throwable = catchThrowable(() ->
                userService.findByUserName("user"));

        assertThat(throwable).isInstanceOf(NoSuchElementException.class);
        assertThat(throwable).hasMessage("User not found.");
    }

    @Test
    void should_throw_exception_when_user_NOT_found_ALT() {

// No Stub needed, 1st line will be null by default since we're mocking userRepo

        Throwable throwable = catchThrowable(() ->
                userService.findByUserName("someUser"));

        assertThat(throwable).isInstanceOf(NoSuchElementException.class);
        assertThat(throwable).hasMessage("User not found.");
    }

// TEST - public UserDTO save(UserDTO user)
    @Test
    void should_save_user() {

        when(userRepository.save(user)).thenReturn(user);

        UserDTO actualDTO = userService.save(userDTO);

        assertThat(actualDTO).usingRecursiveComparison()
                .ignoringExpectedNullFields().isEqualTo(userDTO);

        verify(keycloakService,atLeastOnce()).userCreate(any());
    }

}