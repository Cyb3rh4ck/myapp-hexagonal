package com.codigojava.myapp.application.port.in;

import com.codigojava.myapp.application.service.command.RegisterUserCommand;
import com.codigojava.myapp.application.service.model.UserResult;

public interface RegisterUserUseCase {
    UserResult register(RegisterUserCommand command);
}
