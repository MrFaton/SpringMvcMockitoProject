package com.nixsolutions.ponarin.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.MultiValueMap;

import com.nixsolutions.ponarin.consatnt.View;
import com.nixsolutions.ponarin.dao.RoleDao;
import com.nixsolutions.ponarin.dao.UserDao;
import com.nixsolutions.ponarin.entity.Role;
import com.nixsolutions.ponarin.entity.User;
import com.nixsolutions.ponarin.form.UserForm;
import com.nixsolutions.ponarin.utils.UserFormUtils;
import com.nixsolutions.ponarin.utils.UserUtils;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

public class PersonManageControllerTest {
    @Mock
    private RoleDao roleDao;

    @Mock
    private UserDao userDao;

    @Mock
    private UserUtils userUtils;

    @InjectMocks
    private PersonManageController personManageController;

    private MockMvc mockMvc;

    private UserFormUtils userFormUtils = new UserFormUtils();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        this.mockMvc = MockMvcBuilders.standaloneSetup(personManageController)
                .build();

        when(userUtils.isLoginExists(anyString())).thenReturn(false);
        when(userUtils.isEmailExists(any(UserForm.class))).thenReturn(false);
        when(userUtils.getUserByForm(any(UserForm.class), any(Role.class)))
                .thenReturn(new User());
        when(userUtils.getFormByUser(any(User.class)))
                .thenReturn(new UserForm());

        when(roleDao.findByName(anyString())).thenReturn(new Role());
        when(userDao.findByLogin(anyString())).thenReturn(new User());
    }

    @Test
    public void testShowCreateForm() throws Exception {
        mockMvc.perform(get("/admin/create")).andExpect(status().isOk())
                .andExpect(model().attributeExists("userForm"))
                .andExpect(view().name(View.FROM_CREATE_EDIT));
    }

    @Test
    public void testCreateWithBadForm() throws Exception {
        MultiValueMap<String, String> values = userFormUtils
                .createParamsUserForm();
        values.get("password").set(0, "");
        mockMvc.perform(post("/admin/create").params(values))
                .andExpect(status().is(200))
                .andExpect(model().attributeExists("userForm"))
                .andExpect(view().name(View.FROM_CREATE_EDIT));
    }

    @Test
    public void testCreate() throws Exception {
        mockMvc.perform(post("/admin/create")
                .params(userFormUtils.createParamsUserForm()))
                .andExpect(status().is(302))
                .andExpect(model().attributeExists("userForm"))
                .andExpect(view().name(View.PAGE_REDIRECT_MAIN));
    }

    @Test
    public void testShowEditForm() throws Exception {
        mockMvc.perform(get("/admin/edit").param("person", "someLogin"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("edit"))
                .andExpect(model().attributeExists("userForm"))
                .andExpect(view().name(View.FROM_CREATE_EDIT));
    }

    @Test
    public void testEditWithBadForm() throws Exception {
        MultiValueMap<String, String> values = userFormUtils
                .createParamsUserForm();
        values.get("login").set(0, "");

        mockMvc.perform(post("/admin/edit").params(values))
                .andExpect(status().is(200))
                .andExpect(model().attributeExists("edit"))
                .andExpect(model().attributeExists("userForm"))
                .andExpect(view().name(View.FROM_CREATE_EDIT));
    }

    @Test
    public void testEdit() throws Exception {
        mockMvc.perform(post("/admin/edit")
                .params(userFormUtils.createParamsUserForm()))
                .andExpect(status().is(302))
                .andExpect(model().attributeExists("edit"))
                .andExpect(model().attributeExists("userForm"))
                .andExpect(view().name(View.PAGE_REDIRECT_MAIN));
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(get("/admin/delete")).andExpect(status().isOk())
                .andExpect(view().name(View.PAGE_REDIRECT_MAIN));
    }
}
