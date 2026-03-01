package com.codigojava.myapp.application.port.in;

import com.codigojava.myapp.application.service.command.AuthenticateCommand;
import com.codigojava.myapp.application.service.model.AuthResult;

public interface AuthenticateUserUseCase {
    AuthResult authenticate(AuthenticateCommand command);

}
