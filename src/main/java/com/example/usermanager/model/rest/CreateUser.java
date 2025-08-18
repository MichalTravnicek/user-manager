package com.example.usermanager.model.rest;

import jakarta.validation.GroupSequence;
import jakarta.validation.groups.Default;

@GroupSequence({CreateUser.Group.class, Default.class})
public interface CreateUser {
    interface Group {
    }
}
