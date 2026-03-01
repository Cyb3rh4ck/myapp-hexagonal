package com.codigojava.myapp.application.port.in;

import com.codigojava.myapp.application.service.model.UserResult;

import java.util.UUID;

public interface GetUserUseCase {
    UserResult getById(UUID id);
}
